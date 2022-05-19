package ru.labs.inter;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author nafis
 * @since 13.03.2022
 */

public interface Compute extends Remote {
    <T> T doTask(Task<T> task) throws RemoteException;
}
