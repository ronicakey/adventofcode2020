package adventOfCode;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 {

    private static long timestamp;
    private static List<BigInteger> buses = new ArrayList<>();
    private static List<BigInteger> remainders = new ArrayList<>();

    private static void readNotes(String filename)  {
        try (Scanner input = new Scanner(new File(filename))) {
            timestamp = Long.parseLong(input.nextLine());
            parseBuses(input.nextLine());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void parseBuses(String notes) {
        String[] schedule = Arrays.stream(notes.split(",")).toArray(String[]::new);
        IntStream.range(0, schedule.length).forEach(e -> {
            String bus = schedule[e];
            if (Pattern.compile("\\d+").matcher(bus).matches()) {
                BigInteger time = new BigInteger(bus);
                buses.add(time);
                remainders.add(time.subtract(BigInteger.valueOf(e)));
            }
        });
    }

    private static long getEarliestBus() {
        return IntStream.range(0, buses.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity() , i -> getWaitingTime(buses.get(i).intValue())))
                .entrySet().stream()
                .min(Comparator.comparingLong(Map.Entry::getValue))
                .map(e -> buses.get(e.getKey()).intValue() * e.getValue())
                .orElse(-1L);
    }

    private static long getWaitingTime(int bus) {
        long closest = bus * (timestamp / bus);
        if (closest < timestamp)  closest += bus;
        return closest - timestamp;
    }

    private static BigInteger getEarliestTimestamp() {
        BigInteger product = buses.stream().reduce(BigInteger.ONE, BigInteger::multiply);
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < buses.size(); i++) {
            BigInteger productByI = product.divide(buses.get(i));
            result = result.add(remainders.get(i)
                    .multiply(productByI.modInverse(buses.get(i)))
                    .multiply(productByI));
        }
        return result.mod(product);
    }

    public static void main(String[] args) {
        readNotes("day13.txt");
        //part 1
        System.out.println(getEarliestBus());
        //part 2
        System.out.println(getEarliestTimestamp());
    }
}
