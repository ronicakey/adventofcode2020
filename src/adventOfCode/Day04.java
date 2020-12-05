package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

    private static final Map<String, String> PATTERNS =
            Map.of("byr", "byr:(19[2-9]\\d|200[0-2])\\b",
                    "iyr", "iyr:(201\\d|2020)\\b",
                    "eyr", "eyr:(202\\d|2030)\\b",
                    "hgt", "hgt:(((1[5-8]\\d|19[0-3])cm)|((59|6\\d|7[0-6])in))\\b",
                    "hcl", "hcl:#[0-9a-f]{6}\\b",
                    "ecl", "ecl:(amb|b(rn|lu)|g(ry|rn)|hzl|oth)\\b",
                    "pid", "pid:[0-9]{9}\\b");
    private static final Pattern FIELD = Pattern.compile("([b-y]{3}):");
    private static final Pattern FIELDS =
            Pattern.compile("(" + PATTERNS.get("byr") + "|"
                    + PATTERNS.get("iyr") + "|"
                    + PATTERNS.get("eyr") + "|"
                    + PATTERNS.get("hgt") + "|"
                    + PATTERNS.get("hcl") + "|"
                    + PATTERNS.get("ecl") + "|"
                    + PATTERNS.get("pid") + ")");

    private static List<String> ids;

    private static void loadIds(String filename)  {
        try {
            ids = Files.readAllLines(Paths.get(filename));
            ids.add("\n");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static int countValidIds(Pattern pattern) {
        int count = 0;
        Set<String> id = new HashSet<>(PATTERNS.keySet());
        Matcher matcher;

        for (String line : ids) {
            if (line.isBlank()) {
                if (id.isEmpty()) count++;
                id = new HashSet<>(PATTERNS.keySet());
            } else {
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    id.remove(matcher.group().substring(0, 3));
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        loadIds("day04.txt");
        //part 1
        System.out.println(countValidIds(FIELD));
        //part 2
        System.out.println(countValidIds(FIELDS));

    }
}
