package br.usp.ep1.dsid;

import java.util.Iterator;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PartRepository extends Remote {
  
  public String getId() throws RemoteException;
  public void addPart(Part p) throws RemoteException, CloneNotSupportedException;
  public Part getPart(int id) throws RemoteException;
  public Iterator<Part> getPartList() throws RemoteException;
}
