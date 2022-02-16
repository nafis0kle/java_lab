package ru.labs.event;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author nafis
 * @since 16.02.2022
 */
public interface IEvent {
    void handler(BufferedWriter bw) throws IOException;
}
