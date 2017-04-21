package br.usp.ep1.dsid;

import java.util.List;

import java.io.Serializable;

public class PartImpl implements Part, Serializable {
  
  private long id;
  private String name;
  private String desc;
  private List<Part> subPart;
  private int quant;
  
  public long getId() {
    return this.id;
  }
  public void setId(long id) {
    this.id = id;
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
  
  public List<Part> getSubPart() {
    return this.subPart;
  }
  public void setSubPart(List<Part> subPart) {
    this.subPart = subPart;
  }
  
  public int getQuant() {
    return this.quant;
  }
  public void setQuant(int quant) {
    this.quant = quant;
  }
}
