package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day05 {

    private static List<Integer> seats;

    private static void loadSeats(String filename)  {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            seats = stream
                    .mapToInt(n ->
                            Integer.parseInt(n.replaceAll("[BR]", "1")
                                    .replaceAll("[FL]", "0"),2))
                    .boxed()
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static int findSeatId()  {
        int actualSum = (seats.get(seats.size() - 1) + seats.get(0)) * (seats.size() + 1) / 2;
        int currentSum = seats.stream().mapToInt(i -> i).sum();
        return actualSum - currentSum;
    }

    public static void main(String[] args) {
        loadSeats("day05.txt");
        //part 1
        System.out.println(seats.get(seats.size() - 1));
        //part 2
        System.out.println(findSeatId());
    }
}
