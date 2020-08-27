// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.util.ArrayList;

public class Recipe {
  private long id;
  private String name;
  private String description;
  private String difficulty;
  private String cookTime;
  private String prepTime;
  private String author;
  private String imgURL;
  private ArrayList steps;
  private ArrayList ingredients;

  public String getName() {
    return name;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public String getCookTime() {
    return cookTime;
  }

  public String getPrepTime() {
    return prepTime;
  }

  public String getAuthor() {
    return author;
  }

  public String getDescription() {
    return description;
  }

  public long getId() {
    return id;
  }

  public String getImage() {
    return imgURL;
  }

  public ArrayList<String> getSteps() {
    return steps;
  }

  public void setName(String givenName) {
    name = givenName;
  }

  public void setDescription(String givenDesc) {
    description = givenDesc;
  }

  public void setDifficulty(String givenDiff) {
    difficulty = givenDiff;
  }

  public void setPrepTime(String givenPTime) {
    prepTime = givenPTime;
  }

  public void setCookTime(String givenCTime) {
    cookTime = givenCTime;
  }

  public void setAuthor(String givenAuthor) {
    author = givenAuthor;
  }

  public void setId(long givenId) {
    id = givenId;
  }

  public void setImage(String givenImage) {
    imgURL = givenImage;
  }

  public void setSteps(ArrayList<String> givenSteps) {
    steps = givenSteps;
  }

  public void setIngredients(ArrayList<String> givenIngredients) {
    ingredients = givenIngredients;
  }

}
