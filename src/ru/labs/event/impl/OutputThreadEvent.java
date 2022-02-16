package ru.labs.event.impl;

import ru.labs.event.IEvent;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author nafis
 * @since 16.02.2022
 */
public class OutputThreadEvent implements IEvent {

    @Override
    public void handler(BufferedWriter bw) throws IOException {
        String str = "---There was a call to an external file\n";
        bw.write(str);
        System.out.println(str);
    }
}
