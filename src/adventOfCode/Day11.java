package adventOfCode;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day11 {

    enum Direction {
        U(-1, 0), D(1, 0), L(0, -1),  R(0, 1),
        UL(-1, -1), UR(-1, 1), DL(1,-1), DR(1,1);

        private int y;
        private int x;

        Direction(int y, int x) {
            this.y = y;
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }
    }

    private static char[][] seats;
    private static char[][] temp;
    private static int width;
    private static int height;

    private static void loadSeats(String filename)  {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            seats = stream.map(String::toCharArray).toArray(char[][]::new);
            width = seats[0].length;
            height = seats.length;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void printSeats(char[][] grid) {
        Arrays.stream(grid).forEach(s -> System.out.println(String.valueOf(s)));
    }

    private static long getNumOccupiedSeats() {
        return Arrays.stream(seats).flatMapToInt(i -> CharBuffer.wrap(i).chars())
                .filter(i -> (char) i =='#')
                .count();
    }

    private static long findEquilibrium(boolean visible) {
        int count = Integer.MAX_VALUE;
        while (count > 0) {
            count = updateState(visible);
        }
        return getNumOccupiedSeats();
    }

    private static int updateState(boolean visible) {
        temp = Arrays.stream(seats).map(char[]::clone).toArray(char[][]::new);
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char seat = seats[y][x];
                if (seat == 'L' || seat == '#') {
                    count = visible ?
                            count + checkVisibleSeats(y, x, seat) :
                            count + checkNeighborSeats(y, x, seat);
                }
            }
        }
        if (count > 0)
            seats = Arrays.stream(temp).map(char[]::clone).toArray(char[][]::new);
        return count;
    }

    private static int checkNeighborSeats(int row, int col, char seat) {
        int countOccupied = 0;
        int xS = (col - 1) < 0 ? col : col - 1;
        int yS = (row - 1) < 0 ? row : row - 1;
        int xE = (col + 1) >= width ? col : col + 1;
        int yE = (row + 1) >= height ? row : row + 1;
        for (int y = yS; y <= yE; y++) {
            for (int x = xS; x <= xE; x++) {
                if (!(y == row && x == col)) {
                    if (seats[y][x] == '#') countOccupied++;
                }
            }
        }
        if (seat == 'L' && countOccupied == 0) {
            temp[row][col] = '#';
            return 1;
        }
        if (seat == '#' && countOccupied >= 4) {
            temp[row][col] = 'L';
            return 1;
        }
        return 0;
    }

    private static int checkVisibleSeats(int row, int col, char seat) {
        int countOccupied = 0;
        for (Direction dir : Direction.values()) {
            int y = row, x = col;
            while (true) {
                y += dir.getY();
                x += dir.getX();
                if (y < 0 || x < 0 || y >= height || x >= width)
                    break;
                char nei = seats[y][x];
                if (nei != '.') {
                    if (nei == '#') countOccupied++;
                    break;
                }
            }
        }
        if (seat == 'L' && countOccupied == 0) {
            temp[row][col] = '#';
            return 1;
        }
        if (seat == '#' && countOccupied >= 5) {
            temp[row][col] = 'L';
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        //part 1
        loadSeats("day11.txt");
        System.out.println(findEquilibrium(false));
        //part 2
        loadSeats("day11.txt");
        System.out.println(findEquilibrium(true));
    }
}
