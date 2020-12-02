package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day01 {

    private static final int N = 2020;

    private static List<Integer> getNumbers(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.map(Integer::parseInt)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
        }
        return new ArrayList<>();
    }

    private static long getPairProduct(String filename) {
        List<Integer> list = getNumbers(filename);
        int len = list.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = len - 1; j > i; j--) {
                int sum = list.get(i) + list.get(j);
                if (sum == N) return list.get(i) * list.get(j);
                else if (sum < N) break;
            }
        }
        return -1L;
    }

    private static long getTripletProduct(String filename) {
        List<Integer> list = getNumbers(filename);
        int len = list.size();
        for (int i = 0; i < len - 2; i++) {
            int j = i + 1;
            int k = len - 1;
            while (j < k) {
                int sum = list.get(i) + list.get(j) + list.get(k);
                if (sum == N) return list.get(i) * list.get(j) * list.get(k);
                else if (sum < N) j++;
                else k--;
            }
        }
        return -1L;
    }

    public static void main(String[] args) {
        //part 1
        System.out.println(getPairProduct("day01.txt"));
        //part 2
        System.out.println(getTripletProduct("day01.txt"));
    }
}
