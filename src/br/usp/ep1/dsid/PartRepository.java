package br.usp.ep1.dsid;

import java.util.ArrayList;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PartRepository extends Remote {
  
  public String getId() throws RemoteException;
  public String getHost() throws RemoteException;
  public void addPart(Part p) throws RemoteException, CloneNotSupportedException;
  public Part getPart(int id) throws RemoteException;
  public ArrayList<Part> getPartList() throws RemoteException;
}
