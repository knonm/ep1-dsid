package br.usp.ep1.dsid;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements PartRepository {
  
  private String id;
  private Map<Integer, Part> parts;
  
  public Server(String id) {
    this.parts = new HashMap<Integer, Part>();
    this.id = id;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void addPart(Part part) throws RemoteException, CloneNotSupportedException {
    Part p = part.clone();
    
    p.setRepo(this);
    
    this.parts.put(p.getId(), p);
  }
  
  public Part getPart(int id) {
    return this.parts.get(id);
  }
  
  public Iterator<Part> getPartList() {
    return this.parts.values().iterator();
  }
  
  public static void main(String[] args) {
    
    String stubId = (args.length < 1) ? "1" : args[0];
    String stubName = "PartRepository_" + stubId;
    try {
      Server obj = new Server(stubName);
      PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject
        (obj, 0);
      
      // Bind the remote object's stub in the RMI registry
      // Running on port 1099 by default
      Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2001);
      registry.bind(stubName, stub);
      
      System.out.println("Server " + stubId + " ready (stub " + stubName + ")");
    } catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
