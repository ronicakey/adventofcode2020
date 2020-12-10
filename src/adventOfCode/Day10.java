package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 {

    private static List<Integer> adapters;
    private static int maxId;
    private static long[] arrangements;

    private static void loadAdapters(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            adapters = new ArrayList<>();
            adapters.add(0);
            stream.map(Integer::parseInt)
                    .sorted()
                    .forEach(n -> adapters.add(n));
            adapters.add(adapters.get(adapters.size() - 1) + 3);
            maxId = adapters.size() - 1;
            arrangements = new long[maxId];
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static long countDifferences() {
        Map<Integer, Long> differences = IntStream.range(0, maxId)
                .mapToObj(i -> adapters.get(i + 1) - adapters.get(i))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return differences.get(1) * differences.get(3);
    }

    private static long countArrangements(int id) {
        if (id == maxId)
            return 1;
        if (arrangements[id] == 0) {
            arrangements[id] = IntStream.rangeClosed(id + 1, Math.min(id + 3, maxId))
                    .filter(i -> adapters.get(i) <= adapters.get(id) + 3)
                    .mapToLong(Day10::countArrangements)
                    .sum();
        }
        return arrangements[id];
    }

    public static void main(String[] args) {
        loadAdapters("day10.txt");
        //part 1
        System.out.println(countDifferences());
        //part 2
        System.out.println(countArrangements(0));
    }
}
