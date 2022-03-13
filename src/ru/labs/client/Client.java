package ru.labs.client;
import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * @author nafis
 * @since 16.02.2022
 */

public class Client implements Runnable{
    private final int PORT;
    private final String HOST;
    private final String pathFile;

    public Client(String host, String port, String pathFile){
        PORT = Integer.parseInt(port);
        HOST = host;
        this.pathFile = pathFile;
    }

    public void run(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathFile))) {
            String line = "------------------------------------------\n";
            Random random = new Random();
            String operation;
            String message;
            String newValue = "";
            Scanner sc = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                Socket socket = new Socket(HOST, PORT);
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());

                System.out.println("1. Get arrays\n2.Start\n3.Exit");
                String command = sc.nextLine();

                switch (command) {
                    case "1" -> {
                        System.out.println("Enter array number (0 - int, 1 - double, 2 - string): ");
                        String arrNumber = sc.nextLine();
                        serverOutput.writeBytes("get\n");
                        serverOutput.writeBytes(arrNumber + "\n");
                        serverOutput.writeBytes( "0\n");
                        serverOutput.writeBytes("0\n");
                        serverOutput.writeBytes("0\n");

                        String str = br.readLine();
                        System.out.println("Result: " + str);
                        bw.write(str + "\n");

                        System.out.println(line);
                        bw.write(line);
                    }

                    case "2" -> {

                        System.out.println("Enter array number (0 - int, 1 - double, 2 - string): ");
                        String arrNumber = sc.nextLine();
                        System.out.println("Enter external index (0-2): ");
                        String indexEx = sc.nextLine();
                        System.out.println("Enter internal index (0-2): ");
                        String indexIn = sc.nextLine();
                        System.out.println("Enter operation type (read, record): ");
                        operation = sc.nextLine();

                        if (Objects.equals(operation, "record")) {
                            System.out.println("Enter new value: ");
                            newValue = sc.nextLine();
                            message = "Operation: " + operation + ". Extended index: " + indexEx +
                                    ". Internal index: " + indexIn + ". New value: " + newValue;
                        } else {
                            message = "Operation: " + operation + ". Extended index: " + indexEx +
                                    ". Internal index: " + indexIn;
                        }

                        System.out.println(message);
                        bw.write(message + "\n");

                        serverOutput.writeBytes(operation + "\n");
                        serverOutput.writeBytes(arrNumber + "\n");
                        serverOutput.writeBytes(indexEx + "\n");
                        serverOutput.writeBytes(indexIn + "\n");
                        serverOutput.writeBytes(newValue + "\n");

                        String str = br.readLine();
                        System.out.println("Result: " + str);
                        bw.write(str + "\n");

                        System.out.println(line);
                        bw.write(line);
                    }
                    case "3" -> exit = true;
                    default -> System.out.println("Illegal command");
                }
                Thread.sleep(3000);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Исключение: " + e);
        }
    }

    private String getRandomVal(Random random){
        int n = random.nextInt(3);
        if(n == 0){
            return "0\n";
        } else if (n == 1){
            return "1\n";
        } else {
            return "2\n";
        }
    }
}
