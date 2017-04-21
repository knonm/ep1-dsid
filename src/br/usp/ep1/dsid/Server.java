package br.usp.ep1.dsid;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements PartRepository {
  
  private Map<Long, Part> parts;
  
  /**
   * TODO esta tentando usar Map aqui para Part enquanto usa List em tudo, n funfa
   */
  
  public Server() {
    this.parts = new HashMap<Long, Part>();
	
  }
  
  /**
   * TODO nao esta achando o .max da collection
   */
  public void addPart(Part p){
	  /*
    long id = Collection.max(this.parts.keySet()) + 1;
    p.setId(id);
    this.parts.put(id, p);
    */
  }
  
  public Part getPart(long id) {
    return this.parts.get(id);
  }
  
  public Map<Part> getPartList() {
	  return this.parts;
  }
  
  public static void main(String[] args) {
    
    String stubId = (args.length < 1) ? "1" : args[0];
    String stubName = "PartRepository_" + stubId;
    try {
      Server obj = new Server();
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
