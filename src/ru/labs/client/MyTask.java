package ru.labs.client;

import ru.labs.inter.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nafis
 * @since 13.03.2022
 */
public class MyTask implements Task<String>, Serializable {
    private final String[] args;

    public MyTask(String[] args){
        this.args = args;
    }
    @Override
    public String execute() {
        List<Integer> evenList = new ArrayList<>();
        List<Integer> oddList = new ArrayList<>();

        int arg;
        for (String str : args) {
            try {
                arg = Integer.parseInt(str);

                if (arg % 2 == 0) {
                    evenList.add(arg);
                } else {
                    oddList.add(arg);
                }

            } catch (NumberFormatException ex) {
                System.out.println("Argument list contains non-number character: " + str);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }

        evenList.removeIf(v -> v >= 0);
        oddList.removeIf(v -> v < 0);

        return "List of even and negative numbers: " + evenList + "List of odd and positive numbers: " + oddList;
    }
}
