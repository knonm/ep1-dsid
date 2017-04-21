package br.usp.ep1.dsid;

import java.io.Serializable;

public class PartImpl implements Part, Serializable {
  
  private long id;
  private String name;
  private String desc;
  private Part[] subPart;
  private int[] quant;
  
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
  
  public Part[] getSubPart() {
    return this.subPart;
  }
  public void setSubPart(Part[] subPart) {
    this.subPart = subPart;
  }
  
  public int[] getQuant() {
    return this.quant;
  }
  public void setQuant(int[] quant) {
    this.quant = quant;
  }
}
