package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 {

    private static final Pattern BITMASK = Pattern.compile("mask\\s=\\s(?<mask>[X01]{36})");
    private static final Pattern UPDATE = Pattern.compile("mem\\[(?<address>\\d+)\\]\\s=\\s(?<number>\\d+)");
    private static final Map<Long, Long> MEMORY = new HashMap<>();
    private static final Map<Long, Long> MEMORY2 = new HashMap<>();

    private static List<String> program;

    private static void readProgram(String filename)  {
        try {
            program = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void parseProgram() {
        long[] masks = new long[3];
        List<Integer> xs = new ArrayList<>();
        String mask;
        Matcher maskMatcher, updateMatcher;
        for (String line : program) {
            maskMatcher = BITMASK.matcher(line);
            updateMatcher = UPDATE.matcher(line);
            if (maskMatcher.matches()) {
                mask = maskMatcher.group("mask");
                updateMasks(mask, masks, xs);
            } else if (updateMatcher.matches()) {
                long number = Long.parseLong(updateMatcher.group("number"));
                long address = Long.parseLong(updateMatcher.group("address"));
                MEMORY.put(address, number & masks[0] | masks[1]);
                updateMemory2(xs, address | masks[2], number);
            }
        }
    }

    private static void updateMasks(String mask, long[] masks, List<Integer> xPositions) {
        int len = mask.length();
        StringBuilder builderZero = new StringBuilder();
        StringBuilder builderOne = new StringBuilder();
        StringBuilder builderAddress = new StringBuilder();
        xPositions.clear();
        IntStream.range(0, len)
                .forEach(i -> {
                    char c = mask.charAt(i);
                    if (c == 'X') {
                        builderZero.append('1');
                        builderOne.append('0');
                        builderAddress.append('1');
                        xPositions.add(len - 1 - i);
                    } else {
                        builderZero.append(c);
                        builderOne.append(c);
                        builderAddress.append(c);
                    }
                });
        masks[0] = Long.parseLong(builderZero.toString(), 2);
        masks[1] = Long.parseLong(builderOne.toString(), 2);
        masks[2] = Long.parseLong(builderAddress.toString(), 2);
    }

    private static void updateMemory2(List<Integer> xs, long maskedAddress, long number) {
        Set<Long> addresses = new HashSet<>(){{add(maskedAddress);}};
        xs.stream().mapToLong(i -> 1L << i).forEach(
                i -> addresses.addAll(addresses.stream().map(address -> address & ~i)
                    .collect(Collectors.toSet()))
        );
        addresses.forEach(a -> MEMORY2.put(a, number));
    }

    private static long getSum(Map<Long, Long> memory) {
        return memory.values().stream().mapToLong(i -> i).sum();
    }

    public static void main(String[] args) {
        readProgram("day14.txt");
        parseProgram();
        //part 1
        System.out.println(getSum(MEMORY));
        //part 2
        System.out.println(getSum(MEMORY2));
    }
}
