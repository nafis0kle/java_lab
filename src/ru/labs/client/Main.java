package ru.labs.client;

import java.io.IOException;
import java.util.Properties;

/**
 * @author nafis
 * @since 16.02.2022
 */
public class Main {
    public static final int CLIENT_COUNT = 1;

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("client.properties"));
        System.out.println(properties.getProperty("server.port"));

        for(int i = 1; i <= CLIENT_COUNT; i++){
            Client client = new Client(
                    properties.getProperty("server.host"),
                    properties.getProperty("server.port"),
                    properties.getProperty("output.file.path"));
            Thread t = new Thread(client);
            t.start();
        }
    }
}
