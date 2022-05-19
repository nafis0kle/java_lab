package ru.labs.server;

import ru.labs.compute.Compute;
import ru.labs.compute.Task;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author nafis
 * @since 19.05.2022
 */
public class RemoteEngine implements Compute {
    public RemoteEngine() {
        super();
    }

    public <T> T doTask(Task<T> t) {
        return t.execute();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            String name = "Compute";
            Compute engine = new RemoteEngine();
            Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("RemoteEngine bound");
        } catch (Exception e) {
            System.err.println("RemoteEngine exception:");
            e.printStackTrace();
        }
    }
}
