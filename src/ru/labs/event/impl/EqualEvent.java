package ru.labs.event.impl;

import ru.labs.event.IEvent;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author nafis
 * @since 16.02.2022
 */
public class EqualEvent implements IEvent {

    @Override
    public void handler(BufferedWriter bw) throws IOException {
        String str = "---Equal event handed\n";
        bw.write(str);
        System.out.println(str);
    }
}
