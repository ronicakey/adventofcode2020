package adventOfCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08 {

    private static class Instruction {
        final int value;
        final boolean isNopOrJmp;
        String command;

        public Instruction(String instruction) {
            String[] in = instruction.split("\\s");
            command = in[0];
            isNopOrJmp = "nop".equals(command)|| "jmp".equals(command);
            value = Integer.parseInt(in[1]);
        }

        public int getValue() {
            return value;
        }

        public String getCommand() {
            return command;
        }

        public boolean isNopOrJmp() {
            return isNopOrJmp;
        }

        public void switchCommand() {
            if (isNopOrJmp) command = "nop".equals(command) ? "jmp" : "nop";
        }
    }

    private static List<Instruction> instructions;
    private static int numInstructions;
    private static int accumulator = 0;

    private static void loadInstructions(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            instructions = stream.map(Instruction::new).collect(Collectors.toList());
            numInstructions = instructions.size();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static boolean readInstructions() {
        Set<Integer> ids = new HashSet<>();
        int id = 0;
        accumulator = 0;

        while (id < numInstructions && ids.add(id)) {
            Instruction in = instructions.get(id);
            switch (in.getCommand()) {
                case "acc":
                    accumulator += in.getValue();
                    id += 1;
                    break;
                case "nop":
                    id += 1;
                    break;
                case "jmp":
                    id += in.getValue();
                    break;
            }
        }
        return id == numInstructions;
    }

    private static void checkInstructions() {
        for (int id = 0; id < numInstructions; id++) {
            if (hasNoCycle(id)) {
                return;
            }
        }
    }

    private static boolean hasNoCycle(int id) {
        Instruction in = instructions.get(id);
        boolean isCorrect = false;
        if (in.isNopOrJmp()) {
            in.switchCommand();
            isCorrect = readInstructions();
            if (!isCorrect) {
                in.switchCommand();
            }
        }
        return isCorrect;
    }

    public static void main(String[] args) {
        loadInstructions("day08.txt");
        //part 1
        readInstructions();
        System.out.println(accumulator);
        //part 2
        checkInstructions();
        System.out.println(accumulator);
    }
}
