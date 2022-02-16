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
    private static final int MAX_VALUE = 10;
    private final Listener listener;
    private final String[] args;
    private int forbiddenValue;

    public LabsWorker(String[] args, Listener listener){
        this.args = args;
        this.listener = listener;
    }

    public void doLab() {
        String outputPath;

        System.out.println("Enter file path to init N number: ");
        Scanner scanner = new Scanner(System.in);
        String inputPath = scanner.next();

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))){
            forbiddenValue = Integer.parseInt(br.readLine());
            outputPath = br.readLine();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))){
                boolean result = findAndOutputLists(bw);
                IEvent event = new GetResultEvent();
                listener.generateEvent(event, bw);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean findAndOutputLists(BufferedWriter bw) throws IOException {
        List<Integer> evenList = new ArrayList<>();
        List<Integer> oddList = new ArrayList<>();
        String outputStr;
        int arg;

        for(String str : args) {
            try {
                arg = Integer.parseInt(str);
                checkMaxValue(arg);
                checkForbiddenValue(arg, forbiddenValue, bw);

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
        outputStr = "List of even and negative numbers: " + evenList;
        outputStrToConsoleAndFile(bw, outputStr);

        oddList.removeIf(v -> v < 0);
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

    private void checkMaxValue(int arg){
        if (arg > MAX_VALUE) {
            String str = "Each argument must be less than " + MAX_VALUE;
            throw new IllegalArgumentException(str);
        }
    }

    private void checkForbiddenValue(int arg, int forbiddenValue, BufferedWriter bw) throws IOException {
        if (arg == forbiddenValue) {
            IEvent iEvent = new EqualEvent();
            listener.generateEvent(iEvent, bw);

            String str = "The number " + forbiddenValue + " is illegal";
            throw new IllegalArgumentException(str);
        }
    }
}
