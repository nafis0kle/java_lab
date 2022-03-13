package ru.labs.server;

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
    public final int PORT;
    private DataOutputStream dos;
    private ServerSocket servSocket;
    private final String pathFile;
    private Socket socket;
    private int[][] intArr = {{1,2,7},{3,4,8},{5,6,9}};
    private double[][] doubleArr = {{1.1,2.2,7.7},{3.3,4.4,8.8},{5.5,6.6,9.9}};
    private String[][] stringArr = {{"1", "2","7"}, {"3", "4","8"}, {"5", "6","9"}};
    private final List<Integer> secureIntArr = new ArrayList<>();
    private final List<Integer> secureDoubleArr = new ArrayList<>();
    private final List<Integer> secureStringArr = new ArrayList<>();

    public Server( String[] args, Integer port, String pathFile) {
        PORT = port;
        this.pathFile = pathFile;

        initSecureIndexes(args);

        try {
            servSocket = new ServerSocket(PORT);
            servSocket.setSoTimeout(10000);
        } catch (IOException e) {
            System.err.println("Не удаётся открыть сокет для сервера: " + e);
        }
    }

    public void go() {
        class Listener implements Runnable {
            public Listener (Socket aSocket) {
                socket = aSocket;
            }

            public void run() {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathFile)) ;
                     BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    System.out.println("Слушатель запущен");

                    dos = new DataOutputStream(socket.getOutputStream());

                    try {
                        String operation = br.readLine();
                        bw.write(operation + "\n");
                        String arrNumber = br.readLine();
                        bw.write(arrNumber + "\n");
                        Integer indexEx = Integer.parseInt(br.readLine());
                        bw.write(indexEx + "\n");
                        Integer indexIn = Integer.parseInt(br.readLine());
                        bw.write(indexIn + "\n");

                        switch (operation) {
                            case "read" -> readArray(arrNumber, indexEx, indexIn);
                            case "record" -> {
                                if(isIndexAvailable(indexEx, indexIn, arrNumber)) {
                                    String newValue = br.readLine();
                                    bw.write(newValue + "\n");
                                    recordArray(arrNumber, indexEx, indexIn, newValue);
                                } else {
                                    dos.writeBytes("The index " + indexEx + ":" + indexIn +
                                            " is secure\n");
                                }
                            }
                            case "get" -> {
                                sendArrays(arrNumber);
                            }
                            default -> {
                                System.out.println("Illegal operation");
                                dos.writeBytes("Illegal operation\n");
                            }
                        }
                    }   catch (NumberFormatException e){
                        dos.writeBytes("Illegal value\n");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        dos.writeBytes("Illegal index\n");
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

    private void sendArrays(String arrNumber) throws IOException,ArrayIndexOutOfBoundsException{
        switch (arrNumber) {
            case "0" -> {
                dos.writeBytes(Arrays.deepToString(intArr));
            }
            case "1" -> {
                dos.writeBytes(Arrays.deepToString(doubleArr));
            }
            case "2" -> {
                dos.writeBytes(Arrays.deepToString(stringArr));
            }
            default -> dos.writeBytes("Illegal arr number\n");
        }
    }

    private void readArray(String arrNumber, Integer indexEx, Integer indexIn) throws IOException, ArrayIndexOutOfBoundsException {
        String result;

        switch (arrNumber) {
            case "0" -> {
                result = intArr[indexEx][indexIn] + "\n";
                dos.writeBytes(result);
            }
            case "1" -> {
                result = doubleArr[indexEx][indexIn] + "\n";
                dos.writeBytes(result);
            }
            case "2" -> {
                result = stringArr[indexEx][indexIn] + "\n";
                dos.writeBytes(result);
            }
            default -> dos.writeBytes("Illegal arr number\n");
        }
    }

    private void recordArray(String arrNumber, Integer indexEx, Integer indexIn, String newValue) throws IOException, ArrayIndexOutOfBoundsException {
        String result;

        switch (arrNumber) {
            case "0" -> {
                int newInt = Integer.parseInt(newValue);
                intArr[indexEx][indexIn] = newInt;
                result = Arrays.deepToString(intArr) + "\n";
                dos.writeBytes(result);
            }
            case "1" -> {
                double newDouble = Double.parseDouble(newValue);
                doubleArr[indexEx][indexIn] = newDouble;
                result = Arrays.deepToString(doubleArr) + "\n";
                dos.writeBytes(result);
            }
            case "2" -> {
                stringArr[indexEx][indexIn] = newValue;
                result = Arrays.deepToString(stringArr) + "\n";
                dos.writeBytes(result);
            }
            default -> dos.writeBytes("Illegal arr number\n");
        }
    }

    private void initSecureIndexes(String[] args) throws NumberFormatException {
        int arrType = 0;
        for (String arg : args) {
            if (Objects.equals(arg, ";")) {
                ++arrType;
                continue;
            }
            switch (arrType) {
                case 0 -> secureIntArr.add(Integer.parseInt(arg));
                case 1 -> secureDoubleArr.add(Integer.parseInt(arg));
                case 2 -> secureStringArr.add(Integer.parseInt(arg));
            }
        }
    }

    private boolean isIndexAvailable(Integer indexEx, Integer indexIn, String arrNumber){
        List<Integer> secureIndexes = null;

        switch (arrNumber){
            case "0" -> secureIndexes = secureIntArr;
            case "1" -> secureIndexes = secureDoubleArr;
            case "2" -> secureIndexes = secureStringArr;
        }

        for (int i = 0; i < secureIndexes.size(); i += 2) {
            if(Objects.equals(secureIndexes.get(i), indexEx)){
                if(Objects.equals(secureIndexes.get(i + 1), indexIn)){
                    return false;
                }
            }
        }

        return true;
    }
}
