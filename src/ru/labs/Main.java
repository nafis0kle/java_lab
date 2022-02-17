package ru.labs;

/**
 * @author nafis
 * @since 03.02.2022
 */

public class Main {
    public static void main(String[] args) {
        Server server = new Server(args);
        server.go();
    }
}
