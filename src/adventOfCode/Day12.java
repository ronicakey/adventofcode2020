package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 {

    enum Direction {
        E(1, 0), N(0, -1), W(-1, 0), S(0, 1);

        private final int x;
        private final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vector getAsVector() {
            return new Vector(x, y);
        }
    }

    static class Vector {
        private int x = 0;
        private int y = 0;

        public Vector() {}

        public Vector(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(Vector direction, int distance) {
            x += direction.x * distance;
            y += direction.y * distance;
        }

        public void rotate(String dir, int angle) {
            int times = angle / 90;
            if ("R".equals(dir)) {
                rotateClockwise(times);
            } else {
                rotateCounterClockwise(times);
            }
        }

        private void rotateClockwise(int times) {
            IntStream.range(0, times).forEach(i -> {
                int temp = x;
                x = -y;
                y = temp;
            });
        }

        private void rotateCounterClockwise(int times) {
            IntStream.range(0, times).forEach(i -> {
                int temp = x;
                x = y;
                y = -temp;
            });
        }

        public int getManhattanDistance() {
            return Math.abs(x) + Math.abs(y);
        }
    }

    private static final Pattern COMMAND = Pattern.compile("(?<action>[NSEWLRF])(?<value>\\d{0,3})");
    private static final Vector waypoint1 = new Vector(1, 0);
    private static final Vector ship1 = new Vector();
    private static final Vector waypoint2 = new Vector(10, -1);
    private static final Vector ship2 = new Vector();

    private static void readCommands(String filename)  {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(Day12::updatePosition);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void updatePosition(String command) {
        Matcher matcher = COMMAND.matcher(command);
        if (matcher.matches()) {
            String action = matcher.group("action");
            int value = Integer.parseInt(matcher.group("value"));
            switch (action) {
                case "N":
                case "S":
                case "E":
                case "W":
                    ship1.move(Direction.valueOf(action).getAsVector(), value);
                    waypoint2.move(Direction.valueOf(action).getAsVector(), value);
                    return;
                case "F":
                    ship1.move(waypoint1, value);
                    ship2.move(waypoint2, value);
                    return;
                case "L":
                case "R":
                    waypoint1.rotate(action, value);
                    waypoint2.rotate(action, value);
                    return;
            }
        }
        throw new RuntimeException("Invalid command: " + command);
    }

    public static void main(String[] args) {
        readCommands("day12.txt");
        //part 1
        System.out.println(ship1.getManhattanDistance());
        //part 2
        System.out.println(ship2.getManhattanDistance());
    }
}
