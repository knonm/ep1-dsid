package br.usp.ep1.dsid;

import java.util.List;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Part extends Remote {
  
  public long getId() throws RemoteException;
  public void setId(long id) throws RemoteException;
  
  public String getName() throws RemoteException;
  public void setName(String name) throws RemoteException;
  
  public String getDesc() throws RemoteException;
  public void setDesc(String desc) throws RemoteException;
  
  public List<Part> getSubPart() throws RemoteException;
  public void setSubPart(List<Part> subPart) throws RemoteException;
  
  public int getQuant() throws RemoteException;
  public void setQuant(int subPart) throws RemoteException;
}
