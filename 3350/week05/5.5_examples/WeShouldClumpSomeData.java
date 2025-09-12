import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class WeShouldClumpSomeData {

    // === "Business rules" sprinkled around, all passing the same clump of data ===
    public static boolean isOffProbation(LocalDate startDate,
                                         LocalDate probationEndDate,
                                         LocalDate today) {
        if (startDate == null || probationEndDate == null || today == null) return false;
        return !today.isBefore(probationEndDate);
    }

    public static boolean isEligibleForBenefits(LocalDate startDate,
                                                LocalDate probationEndDate,
                                                LocalDate today,
                                                int minMonthsForBenefits) {
        if (startDate == null || today == null) return false;
        long monthsAtCompany = ChronoUnit.MONTHS.between(startDate, today);
        boolean passedProbation = isOffProbation(startDate, probationEndDate, today);
        return passedProbation && monthsAtCompany >= minMonthsForBenefits;
    }

    public static boolean isPromotable(LocalDate startDate,
                                       LocalDate today,
                                       int minYears,
                                       int minProjectsCompleted,
                                       int projectsCompleted,
                                       double performanceRatingRequired,
                                       double lastPerformanceRating) {
        if (startDate == null || today == null) return false;
        long years = ChronoUnit.YEARS.between(startDate, today);
        return years >= minYears
                && projectsCompleted >= minProjectsCompleted
                && lastPerformanceRating >= performanceRatingRequired;
    }

    public static int accruedPtoDays(LocalDate startDate,
                                     LocalDate today,
                                     int baseDaysPerYear,
                                     int seniorityBonusPerYearAfter2Years) {
        if (startDate == null || today == null) return 0;
        long years = ChronoUnit.YEARS.between(startDate, today);
        int bonus = (int)Math.max(0, years - 2) * seniorityBonusPerYearAfter2Years;
        return baseDaysPerYear + bonus;
    }

    public static boolean isEligibleForStock(LocalDate startDate,
                                             LocalDate today,
                                             LocalDate grantCliffDate,
                                             int minFullYears) {
        if (startDate == null || today == null || grantCliffDate == null) return false;
        long years = ChronoUnit.YEARS.between(startDate, today);
        boolean pastCliff = !today.isBefore(grantCliffDate);
        return pastCliff && years >= minFullYears;
    }

    public static double retentionBonus(LocalDate startDate,
                                        LocalDate today,
                                        LocalDate fiscalYearEnd,
                                        double baseBonus,
                                        double seniorityMultiplierPerYear,
                                        boolean criticalSkillFlag) {
        if (StreamAnyNull(startDate, today, fiscalYearEnd)) return 0.0;
        long yearsByFYEnd = ChronoUnit.YEARS.between(startDate, fiscalYearEnd);
        double mult = 1.0 + (yearsByFYEnd * seniorityMultiplierPerYear);
        if (criticalSkillFlag) mult += 0.25; // magic number alert
        return baseBonus * mult;
    }

    // === Client code: repeatedly assembles the same clump ===
    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(2022, 3, 14);
        LocalDate probationEnd = startDate.plusMonths(6);
        LocalDate today = LocalDate.now();
        LocalDate fiscalYearEnd = LocalDate.of(today.getYear(), 12, 31);
        LocalDate grantCliff = startDate.plusYears(1);

        // These constants float around (another smell).
        int minMonthsForBenefits = 3;
        int minYearsForPromo = 2;
        int minProjects = 4;
        double minPerf = 3.7;
        int basePto = 15;
        int seniorityPto = 2;

        int projectsCompleted = 5;
        double lastRating = 3.9;
        boolean criticalSkill = true;

        System.out.println("Off probation? " +
                isOffProbation(startDate, probationEnd, today));

        System.out.println("Benefits eligible? " +
                isEligibleForBenefits(startDate, probationEnd, today, minMonthsForBenefits));

        System.out.println("Promotable? " +
                isPromotable(startDate, today, minYearsForPromo, minProjects, projectsCompleted, minPerf, lastRating));

        System.out.println("Accrued PTO days: " +
                accruedPtoDays(startDate, today, basePto, seniorityPto));

        System.out.println("Stock eligible? " +
                isEligibleForStock(startDate, today, grantCliff, 1));

        System.out.println("Retention bonus estimate: " +
                retentionBonus(startDate, today, fiscalYearEnd, 2500.0, 0.08, criticalSkill));
    }

    // tiny helper just to keep the clump prominent elsewhere
    private static boolean StreamAnyNull(Object... objs) {
        for (Object o : objs) if (Objects.isNull(o)) return true;
        return false;
    }
}

/* Introduce an EmploymentTimeline (or EmploymentContext) 
 * value object that holds startDate, today, probationEnd, 
 * grantCliff, fiscalYearEnd and exposes derived values 
 * (tenureYears, tenureMonths).
 */
