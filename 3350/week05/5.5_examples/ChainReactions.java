package hr.shotgun;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * NOTE: This code is intentionally bad to demonstrate Shotgun Surgery.
 * The same business concepts are duplicated across many classes.
 */
public class  {
    public static void main(String[] args) {
        Employee e = new Employee("Alice", "CONTRACTOR", LocalDate.of(2025, 3, 10));
        TimesheetService ts = new TimesheetService();
        PayrollService ps = new PayrollService();
        AccessService ac = new AccessService();
        ExpensePolicy ex = new ExpensePolicy();
        BadgePrinter bp = new BadgePrinter();
        ReportingService rs = new ReportingService();
        OnboardingService ob = new OnboardingService();
        NotificationService no = new NotificationService();

        List<WorkEntry> week = Arrays.asList(
            new WorkEntry(DayOfWeek.MONDAY, 9),
            new WorkEntry(DayOfWeek.TUESDAY, 8),
            new WorkEntry(DayOfWeek.WEDNESDAY, 10),
            new WorkEntry(DayOfWeek.THURSDAY, 8),
            new WorkEntry(DayOfWeek.FRIDAY, 7)
        );

        double pay = ps.calculateWeeklyPay(e, 50.0, week);
        System.out.println("Pay: " + pay);

        System.out.println("Door access: " + ac.allowedZones(e));
        System.out.println("Expense cap: " + ex.maxReimbursable(e));
        System.out.println("Badge: " + bp.printBadge(e));
        System.out.println("Report row: " + rs.toCsvRow(e, week, pay));
        System.out.println("Onboarding tasks: " + ob.tasksFor(e));
        no.sendWelcome(e);
    }
}

// ===== Domain-ish bits (anemic) =====
class Employee {
    final String name;
    final String type; // "FULL_TIME", "CONTRACTOR", "INTERN"
    final LocalDate startDate;

    Employee(String name, String type, LocalDate startDate) {
        this.name = name;
        this.type = type;
        this.startDate = startDate;
    }
}

class WorkEntry {
    final DayOfWeek day;
    final int hours;
    WorkEntry(DayOfWeek day, int hours) { this.day = day; this.hours = hours; }
}

// ===== Scattered policy duplication below =====

/** Calculates pay with overtime for FULL_TIME only; contractors flat rate; interns fixed stipend logic scattered. */
class PayrollService {
    double calculateWeeklyPay(Employee e, double hourly, List<WorkEntry> week) {
        int total = week.stream().mapToInt(w -> w.hours).sum();

        // OVERTIME POLICY sprinkled everywhere: threshold=40, rate=1.5
        if ("FULL_TIME".equals(e.type)) {
            int ot = Math.max(0, total - 40);
            int reg = total - ot;
            return reg * hourly + ot * hourly * 1.5;
        } else if ("CONTRACTOR".equals(e.type)) {
            // Contractors: never overtime (duplicated assumption in ReportingService)
            return total * hourly;
        } else if ("INTERN".equals(e.type)) {
            // Interns: stipend if >= 20 hours this week (duplicated in ReportingService)
            return total >= 20 ? 300.0 : 0.0;
        }
        return 0.0;
    }
}

/** Timesheet validation with role rules (duplicated thresholds and types). */
class TimesheetService {
    boolean isValidWeek(Employee e, List<WorkEntry> week) {
        int total = week.stream().mapToInt(w -> w.hours).sum();
        // Max hours: FULL_TIME 60, CONTRACTOR 50, INTERN 30 (magic numbers reappear elsewhere)
        if ("FULL_TIME".equals(e.type)) return total <= 60;
        if ("CONTRACTOR".equals(e.type)) return total <= 50;
        if ("INTERN".equals(e.type)) return total <= 30;
        return false;
    }
}

/** Door access differs by type; literals duplicated. */
class AccessService {
    List<String> allowedZones(Employee e) {
        if ("FULL_TIME".equals(e.type)) return Arrays.asList("LOBBY","OFFICE","LAB","SERVER_ROOM");
        if ("CONTRACTOR".equals(e.type)) return Arrays.asList("LOBBY","OFFICE");
        if ("INTERN".equals(e.type)) return Arrays.asList("LOBBY","OFFICE","LAB");
        return Collections.emptyList();
    }
}

/** Expense rules scattered: contractors capped, interns no travel, FTE higher cap. */
class ExpensePolicy {
    double maxReimbursable(Employee e) {
        if ("FULL_TIME".equals(e.type)) return 2000.0;     // duplicated in ReportingService totals
        if ("CONTRACTOR".equals(e.type)) return 500.0;     // duplicated in OnboardingService text
        if ("INTERN".equals(e.type)) return 100.0;
        return 0.0;
    }
}

/** Printing badges leaks policy (probation, titles, style) that shows up elsewhere too. */
class BadgePrinter {
    String printBadge(Employee e) {
        String title = "Employee";
        if ("CONTRACTOR".equals(e.type)) title = "Contractor"; // would need to change in NotificationService & ReportingService too
        if ("INTERN".equals(e.type)) title = "Intern";

        // Probation length duplicated here and in OnboardingService: 90 days
        boolean onProbation = e.startDate.plusDays(90).isAfter(LocalDate.now());

        return String.format("%s\n%s\nProbation: %s",
                e.name, title, onProbation ? "YES" : "NO");
    }
}

/** Reporting reimplements pay/benefit assumptions and role names. */
class ReportingService {
    String toCsvRow(Employee e, List<WorkEntry> week, double pay) {
        int total = week.stream().mapToInt(w -> w.hours).sum();
        String role = "FULL_TIME".equals(e.type) ? "FTE"
                     : "CONTRACTOR".equals(e.type) ? "CONTRACT"
                     : "INTERN".equals(e.type) ? "INTERN" : "UNKNOWN";

        // Re-declared overtime policy (threshold=40) and intern stipend rule (>=20 hrs)
        boolean countedOvertime = "FULL_TIME".equals(e.type) && total > 40;
        boolean stipendWeek = "INTERN".equals(e.type) && total >= 20;

        // Re-declared expense caps
        double expCap = "FULL_TIME".equals(e.type) ? 2000.0
                        : "CONTRACTOR".equals(e.type) ? 500.0
                        : "INTERN".equals(e.type) ? 100.0 : 0.0;

        // Re-declared probation (90 days)
        boolean onProbation = e.startDate.plusDays(90).isAfter(LocalDate.now());

        return String.join(",",
            e.name,
            role,
            Integer.toString(total),
            Boolean.toString(countedOvertime),
            Boolean.toString(stipendWeek),
            String.format(Locale.US, "%.2f", pay),
            String.format(Locale.US, "%.2f", expCap),
            Boolean.toString(onProbation)
        );
    }
}

/** Onboarding repeats policy phrasing and numbers. */
class OnboardingService {
    List<String> tasksFor(Employee e) {
        List<String> tasks = new ArrayList<>();
        tasks.add("Sign NDA");
        if ("CONTRACTOR".equals(e.type)) {
            tasks.add("Upload 1099 form");
            tasks.add("Note: Travel reimbursements capped at $500.");
        } else if ("FULL_TIME".equals(e.type)) {
            tasks.add("Enroll in benefits (eligible after 90 days)."); // 90-day duplication
        } else if ("INTERN".equals(e.type)) {
            tasks.add("Intern handbook");
        }
        return tasks;
    }
}

/** Notifications echo role names & rules again. */
class NotificationService {
    void sendWelcome(Employee e) {
        String subject;
        String body;
        if ("CONTRACTOR".equals(e.type)) {
            subject = "Welcome, Contractor!";
            body = "Hi " + e.name + ",\nYour contractor access is limited to LOBBY/OFFICE. " +
                   "Overtime does not apply. Expense cap is $500.\n";
        } else if ("FULL_TIME".equals(e.type)) {
            subject = "Welcome aboard!";
            body = "Hi " + e.name + ",\nYou’re eligible for overtime > 40 hours and higher expense caps.\n";
        } else if ("INTERN".equals(e.type)) {
            subject = "Welcome, Intern!";
            body = "Hi " + e.name + ",\nWeekly stipend available if you log >= 20 hours.\n";
        } else {
            subject = "Welcome";
            body = "Hi " + e.name + ".";
        }
        System.out.println("[EMAIL] " + subject + "\n" + body);
    }
}
