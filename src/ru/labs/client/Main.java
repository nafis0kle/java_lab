package ru.labs.client;

import ru.labs.compute.Compute;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author nafis
 * @since 19.05.2022
 */

public class Main {
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute comp = (Compute) registry.lookup(name);

            MyTask task = new MyTask(args);

            String result = comp.doTask(task);

            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Main exception:");
            e.printStackTrace();
        }
    }
}
