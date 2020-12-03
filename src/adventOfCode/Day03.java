package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day03 {

    private static final int[][] SLOPES = {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};

    private static char[][] grid;
    private static int width;
    private static int height;

    private static void loadMap(String filename)  {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            grid = stream.map(String::toCharArray).toArray(char[][]::new);
            width = grid[0].length;
            height = grid.length;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void printMap(char[][] grid) {
        Arrays.stream(grid).forEach(s -> System.out.println(String.valueOf(s)));
    }

    private static long checkSlopes() {
        return Arrays.stream(SLOPES).map(Day03::slide)
                .reduce(1L, (a, b) -> a * b);
    }

    private static long slide(int[] slope) {
        //char[][] copyGrid = Arrays.stream(grid).map(char[]::clone).toArray(char[][]::new);
        long count = 0L;
        int x = 0, y = 0;
        while (y < height - 1) {
            x = (x + slope[0]) % width;
            y += slope[1];
            if (grid[y][x] == '#') {
                //copyGrid[y][x] = 'X';
                count++;
            } /*else {
                copyGrid[y][x] = 'O';
            }*/
        }
        //printMap(copyGrid);
        return count;
    }

    public static void main(String[] args) {
        loadMap("day03.txt");
        //part 1
        System.out.println(slide(new int[]{3, 1}));
        //part 2
        System.out.println(checkSlopes());
    }
}
