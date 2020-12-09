package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day09 {

    private static final int N = 25;

    private static List<Long> numbers;
    private static List<Long> subset;

    private static void getNumbers(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            numbers = stream.map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static long findInvalidNum() {
        for (int i = N; i < numbers.size(); i++) {
            if (!isSum(i)) {
                return numbers.get(i);
            }
        }
        return -1;
    }

    private static boolean isSum(int numId) {
        long number = numbers.get(numId);
        subset = new ArrayList<>(numbers.subList(numId - N, numId));
        Collections.sort(subset);
        int l = 0, r = N - 1;
        while (l < r) {
            long sum = subset.get(l) + subset.get(r);
            if (sum == number) return true;
            else if (sum < number) l++;
            else r--;
        }
        return false;
    }

    private static long findContSet(long number) {
        int min = 0, max = 0;
        long sum = numbers.get(min);
        while (max != number) {
            if (sum == number) {
                return getSum(min, max);
            } else if (sum < number) {
                max++;
                sum += numbers.get(max);
            } else {
                sum -= numbers.get(min);
                min++;
            }
        }
        return -1L;
    }

    private static long getSum(int minId, int maxId) {
        subset = new ArrayList<>(numbers.subList(minId, maxId + 1));
        Collections.sort(subset);
        return subset.get(0) + subset.get(subset.size() - 1);
    }

    public static void main(String[] args) {
        getNumbers("day09.txt");
        //part 1
        long invalidNum = findInvalidNum();
        System.out.println(invalidNum);
        //part 2
        System.out.println(findContSet(invalidNum));
    }
}
