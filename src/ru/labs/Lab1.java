package ru.labs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nafis
 * @since 03.02.2022
 */

public class Lab1 {
    private final static int MAX_VALUE = 10;
    private final static int BANNED_VALUE = 5;

    public static void main(String[] args) {
        List<Integer> evenList = new ArrayList<>();
        List<Integer> oddList = new ArrayList<>();

        int arg;
        for(String str : args){
            try {
                arg = Integer.parseInt(str);

                if(arg > MAX_VALUE){
                    throw new IllegalArgumentException("Each argument must be less than " + MAX_VALUE);
                } else if (arg == BANNED_VALUE){
                    throw new IllegalArgumentException("The number " + BANNED_VALUE + " is illegal");
                }

                if(arg % 2 == 0){
                    evenList.add(arg);
                } else {
                    oddList.add(arg);
                }

            } catch (NumberFormatException ex){
                System.out.println("Argument list contains non-number character: " + str);
            } catch (IllegalArgumentException ex){
                System.out.println(ex.getMessage());
            }
        }

        evenList.removeIf(v -> v >= 0);
        oddList.removeIf(v -> v < 0);

        System.out.println("List of even and negative numbers: " + evenList);
        System.out.println("List of odd and positive numbers: " + oddList);
    }
}
