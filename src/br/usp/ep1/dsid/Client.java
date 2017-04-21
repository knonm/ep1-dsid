package br.usp.ep1.dsid;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {
  
  private Registry registry;
  private PartRepository partRepo;
  private Part currPart;
  private Map<Integer, Part> currSubPart;
  private Map<Integer, Integer> currSubPartQuant;
  
  public Client(Registry registry) {
    this.registry = registry;
    this.currSubPart = new HashMap<Integer, Part>();
    this.currSubPartQuant = new HashMap<Integer, Integer>();
  }
  
  public void bind(String repoName) throws RemoteException, NotBoundException {
	  this.partRepo = (PartRepository) this.registry.lookup(repoName);
  }
  
  public void listp() throws RemoteException {
    Iterator<Part> it = this.partRepo.getPartList();
    Part p;
    
    while(it.hasNext()) {
      p = it.next();
      System.out.println(p.getId() + ": " + p.getName());
    }
  }
  
  public void getp(int id) throws RemoteException {
    this.currPart = this.partRepo.getPart(id);
  }
  
  public void showp(Part p, int depth) throws RemoteException {
    String tab = "";
    
    for(int i = 0; i < depth; i++) {
      tab += "  ";
    }
    
    System.out.println(tab + "Id: " + p.getId());
    System.out.println(tab + "Name: " + p.getName());
    System.out.println(tab + "Description: " + p.getDesc());
    System.out.println(tab + "Part quantity: " + p.getQuant());
    
    if (p.getSubPart().size() > 0) {
      System.out.println(tab + "Sub Parts:");
      
      for(Part s : p.getSubPart().values().toArray(new PartImpl[0])) {
        System.out.println(tab + "  " + "Part quantity: " + p.getQuant().get(s.hashCode()));
        this.showp(s, depth+1);
      }
    }
  }
  
  public void clearlist() {
    this.currSubPart.clear();
  }
  
  public void addsubpart(int n) throws RemoteException {
    int id = this.currPart.getId();
    if(this.currSubPart.containsKey(id)) {
      int quant = this.currSubPartQuant.get(id);
      this.currSubPartQuant.replace(id, quant+n);
    } else {
      this.currSubPart.put(this.currPart.getId(), this.currPart);
      this.currSubPartQuant.put(id, n);
    }
  }
  
  public void addp(String name, String desc) throws RemoteException, CloneNotSupportedException {
    Part p = new PartImpl();
    p.setName(name);
    p.setDesc(desc);
    p.setSubPart(this.currSubPart);
    p.setQuant(this.currSubPartQuant);
    this.partRepo.addPart(p);
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
