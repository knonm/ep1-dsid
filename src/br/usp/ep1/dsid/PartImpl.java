package br.usp.ep1.dsid;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.Serializable;

public class PartImpl implements Part, Serializable {
  
  private int id;
  private PartRepository repo;
  private String name;
  private String desc;
  private Map<Integer, Part> subPart;
  private Map<Integer, Integer> quant;
  
  public PartImpl() {
    this.subPart = new HashMap<Integer, Part>();
    this.quant = new HashMap<Integer, Integer>();
    this.id = this.hashCode();
  }
  
  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
  }
  
  public PartRepository getRepo() {
    return this.repo;
  }
  public void setRepo(PartRepository repo) {
    this.repo = repo;
  }
  
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDesc() {
    return this.desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  
  public Map<Integer, Part> getSubPart() {
    return this.subPart;
  }
  public void setSubPart(Map<Integer, Part> subPart) {
    this.subPart = subPart;
  }
  
  public Map<Integer, Integer> getQuant() {
    return this.quant;
  }
  public void setQuant(Map<Integer, Integer> quant) {
    this.quant = quant;
  }
  
  public Part clone() throws CloneNotSupportedException {
    return (PartImpl) super.clone();
  }
}
