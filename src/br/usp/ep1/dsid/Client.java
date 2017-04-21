package br.usp.ep1.dsid;

import java.util.Iterator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
  
  private PartRepository partRepo;
  private Part currPart;
  
  private Client() {}
  
  public void bind(String repoName) {
    this.partRepo = (PartRepository) registry.lookup(repoName);
  }
  
  public void listp() {
    Iterator<Part> it = this.partRepo.getPartList().values().iterator();
    Part p;
    
    while(it.hasNext()) {
      p = it.next();
      System.out.println(p.getId() + ": " + p.getName());
    }
  }
  
  public void getp(long id) {
    this.currPart = this.partRepo.getPart(id);
  }
  
  public void showp(Part p, int depth) {
    String tab = "";
    
    for(int i = 0; i < depth; i++) {
      tab += "  ";
    }
    
    System.out.println(tab + "Id: " + p.getId());
    System.out.println(tab + "Name: " + p.getName());
    System.out.println(tab + "Description: " + p.getDesc());
    System.out.println(tab + "Part quantity: " + p.getQuant());
    
    if (p.getSubPart() != null && p.getSubPart().length > 0) {
      System.out.println(tab + "Sub Parts:");
      
      for(Part s : p.getSubPart().toArray()) {
        this.showp(s, depth+1);
      }
    }
  }
  
  public void clearlist() {
    this.currPart.getSubPart().clear();
  }
  
  public void addsubpart(int n) {
    Part p = this.currPart.clone();
    p.setQuant(n);
    this.currPart.getSubPart().add(p);
  }
  
  public static void main(String[] args) {
    
    String host = (args.length < 1) ? null : args[0];
    int port = (args.length < 2) ? 2001 : Integer.valueOf(args[1]);
    String stubId = (args.length < 3) ? "1" : args[2];
    
    String stubName = "PartRepository_" + stubId;
    try {
      // Running on port 1099 by default
      Registry registry = LocateRegistry.getRegistry(host, port);
      PartRepository stub = (PartRepository) registry.lookup(stubName);
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
