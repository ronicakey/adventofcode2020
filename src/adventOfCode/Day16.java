package adventOfCode;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16 {

    private static final Pattern NOTE =
            Pattern.compile(".*\\s(?<min1>\\d+)-(?<max1>\\d+)\\sor\\s(?<min2>\\d+)-(?<max2>\\d+)");
    private static final Pattern USELESS = Pattern.compile("(your|nearby)\\stickets?:");

    private static List<String> notes;
    private static final List<List<Integer>> RANGES = new ArrayList<>();
    private static final List<List<Integer>> TICKETS = new ArrayList<>();
    private static List<Integer> myTicket;
    private static int errorRate;

    private static void readNotes(String filename)  {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            notes = stream.filter(s -> !s.isBlank()).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void parseNotes() {
        Matcher noteMatcher, uselessMatcher;
        List<Integer> ticket;
        List<String> uselessNotes = new ArrayList<>();
        for (String line : notes) {
            noteMatcher = NOTE.matcher(line);
            uselessMatcher = USELESS.matcher(line);
            if (noteMatcher.matches()) {
                RANGES.add(List.of(Integer.parseInt(noteMatcher.group("min1")),
                        Integer.parseInt(noteMatcher.group("max1")),
                        Integer.parseInt(noteMatcher.group("min2")),
                        Integer.parseInt(noteMatcher.group("max2"))));
            } else if (uselessMatcher.matches()) {
                uselessNotes.add(line);
            } else {
                ticket = Arrays.stream(line.trim().split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                if (uselessNotes.size() < 2) {
                    myTicket = ticket;
                } else{
                    if (isValidTicket(ticket)) TICKETS.add(ticket);
                }
            }
        }
    }

    private static boolean isValidTicket(List<Integer> ticket) {
        for (int field : ticket) {
            if (RANGES.stream().noneMatch(range -> isInsideRange(field, range))) {
                errorRate += field;
                return false;
            }
        }
        return true;
    }

    private static boolean isInsideRange(int field, List<Integer> range) {
        return (field >= range.get(0) && field <= range.get(1)) ||
                (field >= range.get(2) && field <= range.get(3));
    }

    private static long getDeparture() {
        Map<Integer, Set<Integer>> possibleOrders = getOrders();
        Map<Integer, Integer> finalOrder = checkOrder(possibleOrders);
        return finalOrder.entrySet().stream().filter(e -> e.getKey() < 6)
                .mapToLong(e -> myTicket.get(e.getValue())).reduce(1L, (x, y) -> x * y);
    }

    private static Map<Integer, Set<Integer>> getOrders() {
        Map<Integer, Set<Integer>> orders = new HashMap<>();
        for (int i = 0; i < myTicket.size(); i++) {
            int fid = i;
            for (int j = 0; j < RANGES.size(); j++) {
                int rid = j;
                if (TICKETS.stream().mapToInt(t -> t.get(fid)).allMatch(f -> isInsideRange(f, RANGES.get(rid)))) {
                    Set<Integer> positions = orders.getOrDefault(rid, new HashSet<>());
                    positions.add(fid);
                    orders.put(rid, positions);
                }
            }
        }
        return orders;
    }

    private static Map<Integer, Integer> checkOrder(Map<Integer, Set<Integer>> orders) {
        Map<Integer, Integer> finalOrder = new HashMap<>();
        Set<Integer> ids = new HashSet<>();
        List<Integer> removeList = new ArrayList<>();
        while (!orders.isEmpty()) {
            orders.entrySet().stream().filter(e -> e.getValue().size() == 1)
                    .forEach(e -> {
                        int rid = e.getKey();
                        int id = e.getValue().stream().findAny().get();
                        finalOrder.put(rid, id);
                        ids.add(id);
                        removeList.add(rid);
                    });
            removeList.forEach(orders::remove);
            orders.forEach((key, value) -> {
                value.removeAll(ids);
                orders.put(key, value);
            });
            removeList.clear();
            ids.clear();
        }
        return finalOrder;
    }

    public static void main(String[] args) {
        readNotes("day16.txt");
        //part 1
        parseNotes();
        System.out.println(errorRate);
        //part 2
        System.out.println(getDeparture());
    }
}