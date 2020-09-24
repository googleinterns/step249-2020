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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import java.util.ArrayList;

/**
 * Represents a Recipe.
 */
public class Recipe {
  private long id;
  private long author_id;
  private String name;
  private String description;
  private String difficulty;
  private Integer prepTime;
  private String author;
  private String imgURL;
  private ArrayList steps;
  private ArrayList ingredients;
  private ArrayList<String> ingredientsMatching;

  public Recipe() {}

  // This constructor takes the values and constructs the recipe.
  public Recipe(
    long givenId,
    String givenName,
    String givenDescription,
    String givenDifficulty,
    Integer givenPrepTime,
    String givenAuthor,
    String givenImg,
    ArrayList<String> givenSteps,
    ArrayList<String> givenIngredients,
    long givenAuthorId
  ) {
    id = givenId;
    name = givenName;
    description = givenDescription;
    difficulty = givenDifficulty;
    prepTime = givenPrepTime;
    author = givenAuthor;
    imgURL = givenImg;
    steps = givenSteps;
    ingredients = givenIngredients;
    author_id = givenAuthorId;
  }

  // This constructor takes the recipe entity, ingredientsMatched & authorName and constructs the recipe.
  public Recipe(
    Entity recipeEntity,
    ArrayList<String> givenIngredientsMatched,
    String authorName
  ) {
    id = recipeEntity.getKey().getId();
    author = authorName;
    name = (String) recipeEntity.getProperty("title");
    imgURL = (String) recipeEntity.getProperty("imgURL");
    description = (String) recipeEntity.getProperty("description");
    difficulty = (String) recipeEntity.getProperty("difficulty");
    Long longPrepTime = ((Long) recipeEntity.getProperty("prep_time"));
    prepTime = longPrepTime.intValue();
    ingredientsMatching = givenIngredientsMatched;
  }

  // This constructor takes the recipe entity and constructs the recipe.
  public Recipe(Entity recipeEntity) {
    id = recipeEntity.getKey().getId();
    name = (String) recipeEntity.getProperty("title");
    imgURL = (String) recipeEntity.getProperty("imgURL");
    description = (String) recipeEntity.getProperty("description");
    difficulty = (String) recipeEntity.getProperty("difficulty");
    Long longPrepTime = ((Long) recipeEntity.getProperty("prep_time"));
    prepTime = longPrepTime.intValue();
  }

  // Creates and returns the corresponding recipe entity.
  public Entity getEntity() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    KeyRange keyRange = datastore.allocateIds("Recipe", 1L);
    Entity recipeEntity = new Entity(keyRange.getStart());

    recipeEntity.setProperty("title", name);
    recipeEntity.setProperty("index_title", name.toLowerCase());
    if (imgURL != null && !imgURL.isEmpty()) recipeEntity.setProperty(
      "imgURL",
      imgURL
    );
    recipeEntity.setProperty("ingredients", ingredients);
    recipeEntity.setProperty("stepList", steps);
    recipeEntity.setProperty("author", author);
    recipeEntity.setProperty("description", description);
    recipeEntity.setProperty("difficulty", difficulty);
    recipeEntity.setProperty("prep_time", prepTime);
    recipeEntity.setProperty("author_id", author_id);
    return recipeEntity;
  }

  public String getName() {
    return name;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public Integer getPrepTime() {
    return prepTime;
  }

  public String getAuthor() {
    return author;
  }

  /**
   * Get the list of ingredients for the recipe.
   */
  public ArrayList getIngredients() {
    return ingredients;
  }

  /**
   * Get the list of matched ingredients with a search for the recipe, from the snippet fields.
   */
  public ArrayList<String> getMatchingIngredients() {
    return ingredientsMatching;
  }

  public String getDescription() {
    return description;
  }

  public long getId() {
    return id;
  }

  /**
   * Returns a string with the url of the image.
   */
  public String getImage() {
    return imgURL;
  }

  /**
   * Get the list of steps/instruction for the recipe.
   */
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

  /**
   * Set the prep time as a integer, to contain the number of minutes for a recipe to be prepared.
   */
  public void setPrepTime(Integer givenPTime) {
    prepTime = givenPTime;
  }

  public void setAuthor(String givenAuthor) {
    author = givenAuthor;
  }

  public void setId(long givenId) {
    id = givenId;
  }

  /**
   * Set a string for the recipe's image URL.
   */
  public void setImage(String givenImage) {
    imgURL = givenImage;
  }

  /**
   * Set a list of strings as instruction/steps for the recipe.
   */
  public void setSteps(ArrayList<String> givenSteps) {
    steps = givenSteps;
  }

  /**
   * Set a list of strings as the ingredients for the recipe.
   */
  public void setIngredients(ArrayList<String> givenIngredients) {
    ingredients = givenIngredients;
  }

  /**
   * Set a list of strings as the matching ingredients for the recipe.
   */
  public void setMatchingIngredients(ArrayList<String> givenIngredients) {
    ingredientsMatching = givenIngredients;
  }
}
