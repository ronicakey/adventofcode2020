package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Day06 {

    private static List<String> answers;

    private static void loadAnswers(String filename)  {
        try {
            answers = Files.readAllLines(Paths.get(filename));
            answers.add("\n");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void countAnswers() {
        int distinctCount = 0;
        int commonCount = 0;
        List<Set<Integer>> list = new ArrayList<>();

        for (String line : answers) {
            if (line.isBlank()) {
                distinctCount += list.stream().skip(1)
                        .collect(() -> new HashSet<>(list.get(0)), Set::addAll, Set::addAll)
                        .size();
                commonCount += list.stream().skip(1)
                        .collect(() -> new HashSet<>(list.get(0)), Set::retainAll, Set::retainAll)
                        .size();
                list.clear();
            } else {
                list.add(stringToSet(line));
            }
        }
        //part1
        System.out.println(distinctCount);
        //part2
        System.out.println(commonCount);
    }

    private static Set<Integer> stringToSet(String s) {
        return s.chars().boxed().collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        loadAnswers("day06.txt");
        countAnswers();
    }
}
