package br.usp.ep1.dsid;

import java.util.List;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements PartRepository {
  
  private List<Part> parts;
  
  public Server() {
    this.parts = new ArrayList<Part>();
  }
  
  public void addPart(Part p) {
    this.parts.add(p);
  }
  
  public Part getPart(int i) {
    return this.parts.get(i);
  }
  
  public List<Part> getPartList() {
    return this.parts;
  }
  
  public static void main(String[] args) {
    
    String stubId = (args.length < 1) ? "1" : args[0];
    try {
      Server obj = new Server();
      PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject
        (obj, 0);
      
      // Bind the remote object's stub in the RMI registry
      // Running on port 1099 by default
      Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2001);
      registry.bind("PartRepository_" + stubId, stub);
      
      System.out.println("Server ready");
    } catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
