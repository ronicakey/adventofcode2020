package adventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day15 {

    private static Map<Long, Long> numbers;
    private static long turn;
    private static long lastNum;

    private static void readFirstNumbers(String filename) {
        try (Scanner input = new Scanner(new File(filename))) {
            long[] nums = Arrays.stream(input.nextLine().trim().split(","))
                    .mapToLong(Long::parseLong)
                    .toArray();
            turn = nums.length;
            lastNum = nums[(int)turn - 1];
            numbers = IntStream.range(0, nums.length - 1).boxed()
                    .collect(Collectors.toMap(i -> nums[i], i -> (long)i + 1));
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    private static void play(int times) {
        while (turn < times) {
            getNext();
        }
    }

    private static void getNext() {
        if (numbers.containsKey(lastNum)) {
            long temp = turn - numbers.get(lastNum);
            numbers.put(lastNum, turn++);
            lastNum = temp;
        } else {
            numbers.put(lastNum, turn++);
            lastNum = 0;
        }
    }

    public static void main(String[] args) {
        readFirstNumbers("day15.txt");
        //part 1
        play(2020);
        System.out.println(lastNum);
        //part 2
        play(30000000);
        System.out.println(lastNum);
    }
}
