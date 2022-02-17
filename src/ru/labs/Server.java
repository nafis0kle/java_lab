package ru.labs;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author nafis
 * @since 16.02.2022
 */

public class Server {
        public static final int PORT = 8080;
        private ServerSocket servSocket;
        private Socket socket;
        private int[][] intArr = {{1,2},{3,4},{5,6}};
        private double[][] doubleArr = {{1.1,2.2},{3.3,4.4},{5.5,6.6}};
        private String[][] stringArr = {{"1", "2"}, {"3", "4"}, {"5", "6"}};
        private final List<Integer> secureIntArr = new ArrayList<>();
        private final List<Integer> secureDoubleArr = new ArrayList<>();
        private final List<Integer> secureStringArr = new ArrayList<>();

        public Server( String[] args) {
            int arrType = 0;
            for (String arg : args) {
                if (Objects.equals(arg, ";")) {
                    ++arrType;
                }

                switch (arrType) {
                    case 0 -> secureIntArr.add(Integer.parseInt(arg));
                    case 1 -> secureDoubleArr.add(Integer.parseInt(arg));
                    case 2 -> secureStringArr.add(Integer.parseInt(arg));
                }
            }

            try{
                servSocket = new ServerSocket(PORT);
                servSocket.setSoTimeout(10000);
            } catch (IOException e) {
                System.err.println("Не удаётся открыть сокет для сервера: " + e.toString());
            }
        }

        public void go() {
            class Listener implements Runnable {

                public Listener (Socket aSocket) {
                    socket = aSocket;
                }

                public void run() {
                    try {
                        System.out.println("Слушатель запущен");
                        InputStream is = socket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));

                        String operation = br.readLine();
                        String arrNumber = br.readLine();
                        Integer indexEx = Integer.parseInt(br.readLine());
                        Integer indexIn = Integer.parseInt(br.readLine());


                        switch (operation) {
                            case "read" -> {
                                readArray(arrNumber, indexEx, indexIn);
                            }
                            case "record" -> {
                                String newValue = br.readLine();
                                recordArray(arrNumber, indexEx, indexIn, newValue);
                            }
                            default -> {
                                System.out.println("Illegal operation");
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Сервер запущен...");

            while (true) {
                try {
                    Socket socket = servSocket.accept();
                    Listener listener = new Listener(socket);
                    Thread thread = new Thread(listener);
                    thread.start();
                } catch(IOException e) {
                    System.err.println("Исключение: " + e);
                }
            }
        }

        private void readArray(String arrNumber, Integer indexEx, Integer indexIn) throws IOException {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String result;

            switch (arrNumber) {
                case "1" -> {
                    result = intArr[indexEx][indexIn] + "\n";
                    output.writeBytes(result);
                }
                case "2" -> {
                    result = doubleArr[indexEx][indexIn] + "\n";
                    output.writeBytes(result);
                }
                case "3" -> {
                    result =stringArr[indexEx][indexIn] + "\n";
                    output.writeBytes(result);
                }
                default -> System.out.println("illegal arr number");
            }
        }

        private void recordArray(String arrNumber, Integer indexEx, Integer indexIn, String newValue) throws IOException {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String result;

            switch (arrNumber) {
                case "1" -> {
                    int newInt = Integer.parseInt(newValue);
                    intArr[indexEx][indexIn] = newInt;
                    result = Arrays.deepToString(intArr) + "\n";
                    output.writeBytes(result);
                }
                case "2" -> {
                    double newDouble = Double.parseDouble(newValue);
                    doubleArr[indexEx][indexIn] = newDouble;
                    result = Arrays.deepToString(doubleArr) + "\n";
                    output.writeBytes(result);
                }
                case "3" -> {
                    stringArr[indexEx][indexIn] = newValue;
                    result = Arrays.deepToString(stringArr) + "\n";
                    output.writeBytes(result);
                }
                default -> System.out.println("Illegal arr number");
            }
        }
}
