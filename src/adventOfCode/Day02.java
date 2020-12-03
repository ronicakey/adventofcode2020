package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day02 {

    private static final Pattern POLICY = Pattern.compile("(?<lower>[0-9]{1,2})-" +
            "(?<upper>[0-9]{1,2})\\s" +
            "(?<character>[a-z]):\\s" +
            "(?<password>[a-z]+)");

    private static long countPasswords(String filename, Function<String, Boolean> method)  {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.filter(s -> method.apply(s)).count();
        } catch (IOException e) {
            System.out.println(e);
        }
        return -1L;
    }

    private static boolean isValidPasswordRegex(String str) {
        Matcher matcher = POLICY.matcher(str);
        if (matcher.matches()) {
            long count = matcher.group("password").chars()
                    .filter(ch -> ch == matcher.group("character").charAt(0))
                    .count();
            return count >= Long.parseLong(matcher.group("lower")) &&
                    count <= Long.parseLong(matcher.group("upper"));
        }
        throw new RuntimeException("Invalid Policy: " + str);
    }

    private static boolean isValidPasswordRegex2(String str) {
        Matcher matcher = POLICY.matcher(str);
        if (matcher.matches()) {
            String password = matcher.group("password");
            char character = matcher.group("character").charAt(0);
            return password.charAt(Integer.parseInt(matcher.group("lower")) - 1) == character ^
                    password.charAt(Integer.parseInt(matcher.group("upper")) - 1) == character;
        }
        throw new RuntimeException("Invalid Policy: " + str);
    }


    public static void main(String[] args) {
        //part 1
        System.out.println(countPasswords("day02.txt", Day02::isValidPasswordRegex));
        //part 2
        System.out.println(countPasswords("day02.txt", Day02::isValidPasswordRegex2));
    }
}
