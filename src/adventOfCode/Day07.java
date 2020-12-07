package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day07 {

    private static final Pattern CONTAINER = Pattern.compile("(?<container>[a-z]+\\s[a-z]+)\\sbags contain");
    private static final Pattern CARGO = Pattern.compile("(\\s(?<count>\\d)\\s(?<cargo>[a-z]+\\s[a-z]+)\\sbags?,?)");
    private static final Pattern NOCARGO = Pattern.compile("\\sno\\sother\\sbags");
    private static final Pattern RULE =
            Pattern.compile(CONTAINER + "(?<containts>" + CARGO + "+|" + NOCARGO + ").");
    private static final String BAG = "shiny gold";

    private static Map<String, Set<String>> outerBags = new HashMap<>();
    private static Map<String, Map<String, Integer>> innerBags = new HashMap<>();

    private static void loadRules(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(Day07::parseRule);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void parseRule(String rule) {
        Matcher matcher = RULE.matcher(rule);
        if (matcher.matches()) {
            String container = matcher.group("container");
            String containts = matcher.group("containts");
            if (!" no other bags".equals(containts)) {
                updateOuterBags(container, containts);
                updateInnerBags(container, containts);
            }
        } else {
            throw new RuntimeException("Invalid Rule!");
        }
    }

    private static void updateOuterBags(String container, String containts) {
        Matcher cargoMatcher = CARGO.matcher(containts);
        while (cargoMatcher.find()) {
            String cargo = cargoMatcher.group("cargo");
            Set<String> outer = outerBags.getOrDefault(cargo, new HashSet<>());
            outer.add(container);
            outerBags.put(cargo, outer);
        }
    }

    private static void updateInnerBags(String container, String containts) {
        Matcher cargoMatcher = CARGO.matcher(containts);
        Map<String, Integer> inner = innerBags.getOrDefault(container, new HashMap<>());
        while (cargoMatcher.find()) {
            String cargo = cargoMatcher.group("cargo");
            inner.put(cargo, Integer.parseInt(cargoMatcher.group("count")));
            innerBags.put(container, inner);
        }
    }

    private static int countOuterBags() {
        Set<String> colors = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.add(BAG);
        while (!queue.isEmpty()) {
            String container = queue.remove();
            for (String color : outerBags.getOrDefault(container, new HashSet<>())) {
                if (colors.add(color)) queue.add(color);
            }
        }
        return colors.size();
    }

    private static int countInnerBags() {
        int count = 0;
        Deque<SimpleEntry<String, Integer>> queue = new ArrayDeque<>();
        queue.add(new SimpleEntry<>(BAG, 1));
        while (!queue.isEmpty()) {
            SimpleEntry<String, Integer> containt = queue.remove();
            count += containt.getValue();
            if (innerBags.containsKey(containt.getKey())) {
                for (Entry<String, Integer> color : innerBags.get(containt.getKey()).entrySet()) {
                    queue.add(new SimpleEntry<>(color.getKey(), containt.getValue() * color.getValue()));
                }
            }
        }
        return count - 1;
    }

    public static void main(String[] args) {
        loadRules("day07.txt");
        //part 1
        System.out.println(countOuterBags());
        //part 2
        System.out.println(countInnerBags());
    }
}
