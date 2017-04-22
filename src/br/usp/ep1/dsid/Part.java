package br.usp.ep1.dsid;

import java.util.Map;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.io.Serializable;

public interface Part extends Remote, Cloneable, Serializable {
  
  public int getId() throws RemoteException;
  public void setId(int id) throws RemoteException;
  
  public PartRepository getRepo() throws RemoteException;
  public void setRepo(PartRepository repo) throws RemoteException;
  
  public String getName() throws RemoteException;
  public void setName(String name) throws RemoteException;
  
  public String getDesc() throws RemoteException;
  public void setDesc(String desc) throws RemoteException;
  
  public Map<Integer, Part> getSubPart() throws RemoteException;
  public void setSubPart(Map<Integer, Part> subPart) throws RemoteException;
  
  public Map<Integer, Integer> getQuant() throws RemoteException;
  public void setQuant(Map<Integer, Integer> quant) throws RemoteException;
  
  public Part clone() throws RemoteException, CloneNotSupportedException;
}
