package br.usp.ep1.dsid;

import java.util.Scanner;

import java.util.Iterator;
import java.util.ArrayList;
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
	  try {
        Iterator<Part> it = this.partRepo.getPartList().iterator();
        Part p;
    
        while(it.hasNext()) {
          p = it.next();
          System.out.println(p.getId() + ": " + p.getName());
        }
	  } catch(NullPointerException e){
		  System.out.println("There are no parts registered in this repository");
	  }
}
  
  public void getp(int id) throws RemoteException {
	try {
      this.currPart = this.partRepo.getPart(id);
	} catch(NullPointerException e){
		  System.out.println("There isn't a part registered with this id");
	  }
  }
  
  public void showp() throws RemoteException {
    this.showp(this.currPart, 0);
  }
  
  private void showp(Part p, int depth) throws RemoteException {
    String tab = "";
    
    for(int i = 0; i < depth; i++) {
      tab += "  ";
    }
    
    System.out.println(tab + "Id: " + p.getId());
    System.out.println(tab + "Name: " + p.getName());
    System.out.println(tab + "Description: " + p.getDesc());
    
    if (p.getSubPart().size() > 0) {
      System.out.println(tab + "Sub Parts:");
      
      for(Part s : p.getSubPart().values().toArray(new PartImpl[0])) {
        System.out.println(tab + "  " + "Part quantity: " + p.getQuant().get(s.getId()));
        this.showp(s, depth+1);
      }
    }
  }
  
  public void clearlist() {
    this.currSubPart.clear();
  }
  
  public void addsubpart(int n) throws RemoteException {
	try {
      int id = this.currPart.getId();
      if(this.currSubPart.containsKey(id)) {
        int quant = this.currSubPartQuant.get(id);
        this.currSubPartQuant.replace(id, quant+n);
      } else {
        this.currSubPart.put(this.currPart.getId(), this.currPart);
        this.currSubPartQuant.put(id, n);
      }
	} catch(NullPointerException e){
		  System.out.print("There isn't a part registered yet in this server. Please register one before adding subparts");
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
      
      Scanner reader = new Scanner(System.in);
      Client client = new Client(registry);
      String opt = "";
      
      while(opt != "exit") {
        System.out.println("\n" + "Enter an option or -h for help: ");
        opt = reader.nextLine();
        switch(opt) {
          case "bind":
            System.out.println("Enter the server name: ");
            String repo = reader.nextLine();
            client.bind(repo);
            break;
          case "listp":
            client.listp();
            break;
          case "getp":
            System.out.println("Enter the part id: ");
            int id = reader.nextInt();
            client.getp(id);
            break;
          case "showp":
            client.showp();
            break;
          case "clearlist":
            client.clearlist();
            break;
          case "addsubpart":
            System.out.println("Enter how many subparts do you want to add: ");
            int n = reader.nextInt();
            client.addsubpart(n);
            break;
          case "addp":
            System.out.println("Enter the part name: ");
            String name = reader.nextLine();
            System.out.println("Enter the description: ");
            String desc = reader.nextLine();
            client.addp(name, desc);
            break;
		  case "-h": 
			System.out.println("Entries and what they do:" + "\n" 
								+ "bind, to add new server" + "\n"
								+ "listp, show the list of parts in the current server" + "\n"
								+ "getp, show the part by the id" + "\n"
								+ "clearlist, clear the parts list" + "\n"
								+ "addsubpart, add subparts to the current part" + "\n"
								+ "addp, add a new part to the registry" + "\n"
								+ "exit, to exit program"); 
			break;
		  case "exit":
			System.exit(0);
			break;
		  case "":
			break;
		default:
			System.err.println("Command not found: ");
			break;
        }
      }
       
    } catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
