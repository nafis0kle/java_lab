package ru.labs;

import ru.labs.event.IEvent;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author nafis
 * @since 16.02.2022
 */
public class Listener {
    void generateEvent(IEvent event, BufferedWriter bw) throws IOException {
        event.handler(bw);
    }
}
