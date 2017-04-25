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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
  
  private boolean validateRepo() {
    boolean ok = this.partRepo != null;
    if(!ok) System.out.println("You are not connected to a repository." + "\n"
        + "Use the command 'listrepo' to list the repositories (servers)"
        + " and command 'bind' to connect to a repository (server).");
    return ok;
  }
  
  private boolean validatePart() {
    boolean ok = this.currPart != null;
    if(!ok) System.out.println("Get a part from a repository first (command 'getp').");
    return ok;
  }
  
  public void listrepo() throws RemoteException {
    try {
      PartRepository repo;
      for(String repoName : this.registry.list()) {
        repo = (PartRepository) this.registry.lookup(repoName);
        int qtdParts = repo.getPartList().size();
        System.out.println("Server hostname: " + repo.getHost());
        System.out.println("Server name: " + repo.getId());
        System.out.println("Server part quantity: " + qtdParts);
        System.out.println();
      }
    } catch (NotBoundException e) {
      System.out.println("Repository doesn't exists. Use the command 'listrepo' to list the repositories (servers).");
    }
  }
  
  public void bind(String host, String repoName) throws RemoteException {
    try {
      if(this.partRepo != null && !this.partRepo.getHost().equals(host)) {
          this.registry = LocateRegistry.getRegistry(host, 2001);
      }
      this.partRepo = (PartRepository) this.registry.lookup(repoName);
      System.out.println("You are now connected to " + this.partRepo.getId());
    } catch (NotBoundException e) {
      System.out.println("Repository doesn't exists. Use the command 'listrepo' to list the repositories (servers).");
    }
  }
  
  public void currrepo() throws RemoteException {
    if(this.validateRepo()) {
      System.out.println("Server host (current): " + this.partRepo.getHost());
      System.out.println("Server name (current): " + this.partRepo.getId());
      System.out.println("Server part quantity: " + this.partRepo.getPartList().size());
      System.out.println();
    }
  }
  
  public void listp() throws RemoteException {
    if(this.validateRepo()) {
      try {
          Iterator<Part> it = this.partRepo.getPartList().iterator();
          Part p;
          
          while(it.hasNext()) {
            p = it.next();
            System.out.println("Part Id: " + p.getId());
            System.out.println("Part Name: " + p.getName());
            System.out.println();
          }
        } catch(NullPointerException e){
          System.out.println("There are no parts registered in this repository");
        }
    }
  }
  
  public void getp(int id) throws RemoteException {
    if(this.validateRepo()) {
      try {
        this.currPart = this.partRepo.getPart(id);
      } catch(NullPointerException e){
        System.out.println("There isn't a part registered with this id");
      }
    }
  }
  
  public void showp() throws RemoteException {
    if(this.validatePart()) {
      this.showp(this.currPart, 0);
    }
  }
  
  private void showp(Part p, int depth) throws RemoteException {
    String tab = "";
    
    for(int i = 0; i < depth; i++) {
      tab += "  ";
    }
    
    // Verifica se peca eh primitiva (nao possui subparts) ou
    // eh agregada (possui subparts). 
    boolean partType = p.getSubPart().size() > 0;
    
    System.out.println(tab + "Id: " + p.getId());
    System.out.println(tab + "Name: " + p.getName());
    System.out.println(tab + "Description: " + p.getDesc());
    System.out.println(tab + "Server: " + p.getRepo().getId());
    System.out.println(tab + "Part type: " + (partType ? "Aggregate" : "Primitive"));
    
    if (partType) {
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
    if(this.validatePart()) {
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
  }
  
  public void addp(String name, String desc) throws RemoteException, CloneNotSupportedException {
    if(this.validateRepo()) {
      Part p = new PartImpl(name, desc);
      p.setSubPart(this.currSubPart);
      p.setQuant(this.currSubPartQuant);
      this.partRepo.addPart(p);
      System.out.println("Part added: ID " + p.getId());
    }
  }
  
  public static void main(String[] args) {
    String host = (args.length < 1) ? null : args[0];
    int port = (args.length < 2) ? 2001 : Integer.valueOf(args[1]);
    
    try {
      // Running on port 1099 by default
      Scanner reader = new Scanner(System.in);
      Client client = new Client(LocateRegistry.getRegistry(host, port));
      String opt = "";
      
      while(opt != "exit") {
        System.out.println("===============================================");
        System.out.print("Enter an option or 'help' for help: ");
        opt = reader.nextLine();
        System.out.println();
        switch(opt) {
          case "listrepo":
            client.listrepo();
            break;
          case "currrepo":
            client.currrepo();
            break;
          case "bind":
            System.out.print("Enter the server host: ");
            String h = reader.nextLine();
            System.out.print("Enter the server name: ");
            String repo = reader.nextLine();
            System.out.println();
            client.bind(h, repo);
            break;
          case "listp":
            client.listp();
            break;
          case "getp":
            System.out.print("Enter the part id: ");
            int id = reader.nextInt();
            System.out.println();
            reader.nextLine();
            client.getp(id);
            break;
          case "showp":
            client.showp();
            break;
          case "clearlist":
            client.clearlist();
            break;
          case "addsubpart":
            System.out.print("Enter how many subparts do you want to add: ");
            int n = reader.nextInt();
            System.out.println();
            reader.nextLine();
            client.addsubpart(n);
            break;
          case "addp":
            System.out.print("Enter the part name: ");
            String name = reader.nextLine();
            System.out.print("Enter the description: ");
            String desc = reader.nextLine();
            System.out.println();
            client.addp(name, desc);
            break;
          case "help": 
            System.out.println("Entries and what they do:" + "\n\n"
                      + "listrepo: List online servers" + "\n"
                      + "currrepo: Show current server" + "\n"
                      + "bind: To add new server" + "\n"
                      + "listp: Show the list of parts in the current server" + "\n"
                      + "showp: Show the current part" + "\n"
                      + "getp: Show the part by the id" + "\n"
                      + "clearlist: Clear the parts list" + "\n"
                      + "addsubpart: Add subparts to the current part" + "\n"
                      + "addp: Add a new part to the registry" + "\n"
                      + "help: Help" + "\n"
                      + "exit: To exit program"); 
            break;
          case "exit":
            System.exit(0);
            break;
          case "":
            break;
          default:
            System.err.println("Command not found.");
            break;
        }
      }
    } catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
