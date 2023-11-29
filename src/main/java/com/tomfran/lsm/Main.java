package com.tomfran.lsm;

import com.tomfran.lsm.tree.LSMTree;
import com.tomfran.lsm.types.ByteArrayPair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    static final String DIRECTORY = "LSM-data";

    public static void main(String[] args) throws InterruptedException {

        if (new File(DIRECTORY).exists())
            deleteDir();

        LSMTree tree = new LSMTree(5, 2, DIRECTORY);

        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        String intro = """
                         
                         |      __|   \\  |           __ __|             \s
                         |    \\__ \\  |\\/ |   ____|      |   _| -_)   -_)\s
                        ____| ____/ _|  _|             _| _| \\___| \\___|\s
                       """;

        String help = """
                      Commands:
                        - s/set  <key> <value> : insert a key-value pair;
                        - g/get  <key>         : get a key value;
                        - d/del  <key>         : delete a key;
                        - e/exit               : stop the console;
                        - d/help               : show this message.
                      """;

        System.out.println(intro);
        System.out.println(help);

        boolean exit = false;

        while (!exit) {
            System.out.print("> ");
            String command = scanner.nextLine();

            try {
                String[] parts = command.split(" ");

                switch (parts[0]) {
                    case "s", "set" -> {
                        tree.add(new ByteArrayPair(parts[1].getBytes(), parts[2].getBytes()));
                        System.out.println("ok");
                    }
                    case "d", "del" -> {
                        tree.delete(parts[1].getBytes());
                        System.out.println("ok");
                    }
                    case "g", "get" -> {
                        byte[] value = tree.get(parts[1].getBytes());
                        System.out.println((value == null || value.length == 0) ? "not found" : new String(value));
                    }
                    case "h", "help" -> System.out.println(help);
                    case "e", "exit" -> exit = true;
                    default -> System.out.println("Unknown command");
                }
            } catch (Exception e) {
                System.out.printf("### error while executing command: \"%s\"\n", command);
            }
        }
        tree.stop();
        scanner.close();

        deleteDir();
    }

    static private void deleteDir() {
        try (Stream<Path> f = Files.walk(Path.of(DIRECTORY))) {
            f.map(Path::toFile).forEach(File::delete);
        } catch (Exception ignored) {}
    }

}
