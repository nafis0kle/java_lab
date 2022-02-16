package ru.labs.event.impl;

import ru.labs.event.IEvent;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author nafis
 * @since 16.02.2022
 */
public class GetResultEvent implements IEvent {
    @Override
    public void handler(BufferedWriter bw) throws IOException {
        String str = "---Got result of function\n";
        bw.write(str);
        System.out.println(str);
    }
}
