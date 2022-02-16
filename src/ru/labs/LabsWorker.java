package ru.labs;

import ru.labs.event.impl.EqualEvent;
import ru.labs.event.impl.GetResultEvent;
import ru.labs.event.IEvent;
import ru.labs.event.impl.OutputThreadEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author nafis
 * @since 16.02.2022
 */
public class LabsWorker {
    private static final Listener listener = new Listener();
    private final static int MAX_VALUE = 10;
    private final String[] args;

    public LabsWorker(String[] args){
        this.args = args;
    }

    public void doLab() {
        int N;
        String outputPath;

        System.out.println("Enter file path to init N number: ");
        Scanner scanner = new Scanner(System.in);
        String inputPath = scanner.next();

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))){
            N = Integer.parseInt(br.readLine());
            outputPath = br.readLine();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))){
                boolean result = findAndOutputLists(N, args, bw);
                IEvent event = new GetResultEvent();
                listener.generateEvent(event, bw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Boolean findAndOutputLists(Integer N, String[] args, BufferedWriter bw) throws IOException {
        List<Integer> evenList = new ArrayList<>();
        List<Integer> oddList = new ArrayList<>();
        String outputStr;

        int arg;
        for(String str : args) {
            try {
                arg = Integer.parseInt(str);

                if (arg > MAX_VALUE) {
                    outputStr = "Each argument must be less than " + MAX_VALUE;
                    throw new IllegalArgumentException(outputStr);

                } else if (arg == N) {
                    IEvent iEvent = new EqualEvent();
                    listener.generateEvent(iEvent, bw);

                    outputStr = "The number " + N + " is illegal";
                    throw new IllegalArgumentException(outputStr);

                }

                if (arg % 2 == 0) {
                    evenList.add(arg);
                } else {
                    oddList.add(arg);
                }

            } catch (NumberFormatException ex){
                String message = "Illegal character" + ex.getMessage();
                outputStrToConsoleAndFile(bw, message);

            } catch (IllegalArgumentException ex){
                outputStrToConsoleAndFile(bw, ex.getMessage());
            }
        }

        evenList.removeIf(v -> v >= 0);
        oddList.removeIf(v -> v < 0);

        outputStr = "List of even and negative numbers: " + evenList;
        outputStrToConsoleAndFile(bw, outputStr);

        outputStr = "List of odd and positive numbers: " + oddList;
        outputStrToConsoleAndFile(bw, outputStr);

        return true;
    }

    private void outputStrToConsoleAndFile(BufferedWriter bw, String str) throws IOException {
        bw.write(str);
        System.out.println(str);
        IEvent event2 = new OutputThreadEvent();
        listener.generateEvent(event2, bw);
    }
}
