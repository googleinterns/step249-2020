package com.google.sps.servlets;

public class Recipe {
  private int id;
  private String name;
  private String description;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

  public void setName(String givenName) {
    name = givenName;
  }

  public void setDescription(String givenDesc) {
    description = givenDesc;
  }

  public void setId(int givenId) {
    id = givenId;
  }
}
