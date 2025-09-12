import java.util.*;

public class ImTooLong {
    public static void interactiveDataMess() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome! Let's collect some info and do way too much with it.");

        // Question 1: name
        System.out.print("What's your full name? ");
        String name = sc.nextLine();

        // Question 2: age
        System.out.print("How old are you (as an integer)? ");
        int age = 0;
        try {
            age = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("That didn't look like an int. We'll just use 0.");
        }

        // Question 3: favorite word
        System.out.print("What's your favorite word? ");
        String favoriteWord = sc.nextLine();

        // Question 4: city
        System.out.print("What city do you live in? ");
        String city = sc.nextLine();

        // Question 5: a list of numbers
        System.out.print("Enter some integers separated by spaces (at least 5 recommended): ");
        String numLine = sc.nextLine();
        String[] numParts = numLine.trim().isEmpty() ? new String[0] : numLine.trim().split("\\s+");
        int[] numbers = new int[numParts.length];
        for (int i = 0; i < numParts.length; i++) {
            try {
                numbers[i] = Integer.parseInt(numParts[i]);
            } catch (Exception e) {
                numbers[i] = 0; // sigh
            }
        }

        // Question 6 (extra): target to search
        System.out.print("Pick a target number to search for in your list: ");
        int target = 0;
        try {
            target = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) { /* nothing to see here */ }

        // Question 7 (extra): another sentence for text stats
        System.out.print("Type a short sentence about yourself: ");
        String bio = sc.nextLine();

        // begin "processing" (all in one method, because why not)
        // numeric stats
        int sum = 0;
        int min = numbers.length > 0 ? numbers[0] : 0;
        int max = numbers.length > 0 ? numbers[0] : 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
            if (numbers[i] < min) min = numbers[i];
            if (numbers[i] > max) max = numbers[i];
        }
        double avg = numbers.length == 0 ? 0.0 : (sum * 1.0) / numbers.length;

        // median (naive: copy and sort using bubble sort because we’re committing crimes)
        int[] copy = Arrays.copyOf(numbers, numbers.length);
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy.length - 1; j++) {
                if (copy[j] > copy[j + 1]) {
                    int tmp = copy[j];
                    copy[j] = copy[j + 1];
                    copy[j + 1] = tmp;
                }
            }
        }
        double median = 0.0;
        if (copy.length > 0) {
            if (copy.length % 2 == 1) {
                median = copy[copy.length / 2];
            } else {
                median = (copy[copy.length / 2 - 1] + copy[copy.length / 2]) / 2.0;
            }
        }

        // mode (super inefficient)
        Integer mode = null;
        int modeCount = 0;
        for (int i = 0; i < numbers.length; i++) {
            int c = 0;
            for (int j = 0; j < numbers.length; j++) {
                if (numbers[i] == numbers[j]) c++;
            }
            if (c > modeCount) {
                modeCount = c;
                mode = numbers[i];
            }
        }

        // linear search for target
        int foundIndex = -1;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == target && foundIndex == -1) {
                foundIndex = i;
            }
        }

        // frequency map (again, here, not elsewhere)
        Map<Integer, Integer> freq = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            int v = numbers[i];
            if (!freq.containsKey(v)) {
                freq.put(v, 1);
            } else {
                freq.put(v, freq.get(v) + 1);
            }
        }

        // string stats: vowels, consonants, spaces, digits (for both favoriteWord and bio, redundantly)
        String both = favoriteWord + " " + bio;
        int vowels = 0, consonants = 0, spaces = 0, digits = 0, others = 0;
        for (int i = 0; i < both.length(); i++) {
            char ch = Character.toLowerCase(both.charAt(i));
            if ("aeiou".indexOf(ch) >= 0) vowels++;
            else if (ch >= 'a' && ch <= 'z') consonants++;
            else if (ch >= '0' && ch <= '9') digits++;
            else if (Character.isWhitespace(ch)) spaces++;
            else others++;
        }

        // find substring occurrences of favoriteWord inside bio (naive search)
        int occurrences = 0;
        if (!favoriteWord.isEmpty()) {
            for (int i = 0; i + favoriteWord.length() <= bio.length(); i++) {
                if (bio.regionMatches(true, i, favoriteWord, 0, favoriteWord.length())) {
                    occurrences++;
                }
            }
        }

        // build a tiny histogram string (sorted keys)
        List<Integer> sortedKeys = new ArrayList<>(freq.keySet());
        Collections.sort(sortedKeys);
        StringBuilder hist = new StringBuilder();
        for (int k = 0; k < sortedKeys.size(); k++) {
            Integer key = sortedKeys.get(k);
            hist.append(String.format("%4d | ", key));
            int bars = freq.get(key);
            for (int b = 0; b < bars; b++) hist.append('#');
            hist.append(" (").append(bars).append(")\n");
        }

        // miscellaneous “derived” values to make this longer and messier
        int yearBorn = 0;
        try {
            // obviously wrong if birthday hasn't happened this year, but we don't care in this mess
            yearBorn = java.time.LocalDate.now().getYear() - age;
        } catch (Exception e) { /* shrug */ }

        String upperCity = city.toUpperCase(Locale.ROOT);
        boolean palWord = new StringBuilder(favoriteWord).reverse().toString().equalsIgnoreCase(favoriteWord);

        // pretend “score” that combines text and number stats arbitrarily
        double weirdScore = avg + median + (mode == null ? 0 : mode * 0.123) + (vowels * 0.5) - (digits * 0.33)
                + (foundIndex >= 0 ? 7.77 : -3.14) + (palWord ? 5 : 0) + (occurrences * 2) + (copy.length * 0.01);

        // print absolutely everything, with redundancy
        System.out.println("\n=== RESULTS ===");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age + " (estimated birth year: " + yearBorn + ")");
        System.out.println("Favorite word: " + favoriteWord + " (palindrome? " + palWord + ")");
        System.out.println("City: " + city + " (uppercased: " + upperCity + ")");
        System.out.println("Bio: " + bio);
        System.out.println("Numbers (" + numbers.length + "): " + Arrays.toString(numbers));
        System.out.println("Sorted copy: " + Arrays.toString(copy));
        System.out.println("Sum = " + sum + ", Min = " + min + ", Max = " + max + ", Avg = " + avg);
        System.out.println("Median = " + median + ", Mode = " + (mode == null ? "n/a" : mode + " (count " + modeCount + ")"));
        System.out.println("Target: " + target + " -> first index = " + foundIndex);
        System.out.println("Character stats across favoriteWord + bio:");
        System.out.println("  vowels=" + vowels + ", consonants=" + consonants + ", digits=" + digits + ", spaces=" + spaces + ", others=" + others);
        System.out.println("Occurrences of favorite word inside bio (case-insensitive): " + occurrences);

        System.out.println("\nFrequency histogram of your numbers:");
        if (hist.length() == 0) {
            System.out.println("(no numbers to show)");
        } else {
            System.out.print(hist.toString());
        }

        // print top 3 frequent values (without using a proper comparator because… chaos)
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(freq.entrySet());
        // selection sort for top-3 on counts
        for (int i = 0; i < entries.size(); i++) {
            int best = i;
            for (int j = i + 1; j < entries.size(); j++) {
                if (entries.get(j).getValue() > entries.get(best).getValue()) best = j;
            }
            if (best != i) {
                Map.Entry<Integer, Integer> tmp = entries.get(i);
                entries.set(i, entries.get(best));
                entries.set(best, tmp);
            }
        }
        System.out.println("\nTop frequent values (up to 3):");
        for (int i = 0; i < Math.min(3, entries.size()); i++) {
            System.out.println("  #" + (i + 1) + " -> " + entries.get(i).getKey() + " (x" + entries.get(i).getValue() + ")");
        }

        // more arbitrary string munging for length
        String[] nameParts = name.trim().isEmpty() ? new String[0] : name.trim().split("\\s+");
        String initials = "";
        for (int i = 0; i < nameParts.length; i++) {
            if (!nameParts[i].isEmpty()) initials += Character.toUpperCase(nameParts[i].charAt(0));
        }
        System.out.println("\nYour initials: " + (initials.isEmpty() ? "(none)" : initials));
        System.out.println("Weird score (completely made up): " + String.format(Locale.ROOT, "%.3f", weirdScore));

        // and a final redundant summary because this method refuses to end
        System.out.println("\n=== SUMMARY ===");
        System.out.println(name + " from " + city + " (age " + age + ") entered " + numbers.length + " numbers.");
        System.out.println("Basic stats -> min: " + min + ", max: " + max + ", avg: " + String.format(Locale.ROOT, "%.2f", avg) + ", median: " + median);
        System.out.println("Text stats -> vowels: " + vowels + ", consonants: " + consonants + ", digits: " + digits + ", spaces: " + spaces + ", others: " + others);
        System.out.println("Search -> target " + target + (foundIndex >= 0 ? " found at index " + foundIndex : " not found"));
        System.out.println("Thanks for participating in this extremely refactorable experience!");
    }

    // small runner if you want to try it:
    public static void main(String[] args) {
        interactiveDataMess();
    }
}
