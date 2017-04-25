package br.usp.ep1.dsid;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements PartRepository {
  
  private String id;
  private String host;
  private Map<Integer, Part> parts;
  
  public Server(String id, String host) {
    this.parts = new HashMap<Integer, Part>();
    this.id = id;
    this.host = host;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getHost() {
    return this.host;
  }
  
  public void addPart(Part part) throws RemoteException, CloneNotSupportedException {
    Part p = part.clone();
    
    p.setRepo(this);
    
    this.parts.put(p.getId(), p);
  }
  
  public Part getPart(int id) {
    return this.parts.get(id);
  }
  
  public ArrayList<Part> getPartList() {
    return new ArrayList<Part>(this.parts.values());
  }
  
  public static void main(String[] args) {
    String stubId = (args.length < 1) ? "1" : args[0];
    String host = (args.length < 2) ? "127.0.0.1" : args[1];
    
    String stubName = "PartRepository_" + stubId;
    
    try {
      Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2001);

      Server obj = new Server(stubName, host);
      PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject
        (obj, 0);
      
      // Bind the remote object's stub in the RMI registry
      // Running on port 1099 by default
      registry.bind(stubName, stub);
      
      System.out.println("Server '" + stubName + "' ready");  
    } catch (AlreadyBoundException abe) {
      System.err.println("Server '" + stubName + "' already exists!");
    } catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
