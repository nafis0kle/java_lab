package ru.labs;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author nafis
 * @since 03.02.2022
 */

public class Main {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("server.properties"));

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter port: ");
        Integer port = sc.nextInt();

        Server server = new Server(args, port, properties.getProperty("output.file.path"));
        server.go();
    }
}
