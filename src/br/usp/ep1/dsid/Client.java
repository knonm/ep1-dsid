package br.usp.ep1.dsid;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
  
  private Client() {}
  
  public static void main(String[] args) {
    
    String host = (args.length < 1) ? null : args[0];
    String stubId = (args.length < 2) ? "1" : args[1];
    try {
      // Running on port 1099 by default
      Registry registry = LocateRegistry.getRegistry(host, 2001);
      PartRepository stub = (PartRepository) registry.lookup("PartRepository_" + stubId);
      Part p = new PartImpl();
      p.setName("TestRMI");
      stub.addPart(p);
      Part response = stub.getPart(0);
      System.out.println("response: " + response.getName());  
    } catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
