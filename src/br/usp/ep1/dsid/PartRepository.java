package br.usp.ep1.dsid;

import java.util.List;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PartRepository extends Remote {
  
  public void addPart(Part p) throws RemoteException;
  public Part getPart(int i) throws RemoteException;
  public List<Part> getPartList() throws RemoteException;
}
