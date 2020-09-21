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
//
//  ++++++ READ ME +++++++
//
//  How to download the 60 init recipes to your database.
//
//  STEP1: Copy this file to your src/main/java/com/google/sps/servlets/ directory
//  STEP2: Set the constant "RECIPES_DIRECTORY" to where your recipe folder is (e.g is /home/beatricemarch/capstone/step249-2020/src/main/webapp/one-off/recipe/)
//  STEP3: Run the script from the url "/test" (you can run it both on localhost and in the deployed version)
//  STEP4: Delete the servlet.
//
//  Trobleshooting
//
//  If you have errors relating to 'isBlank()' try the following:
//  + add this to the imports "import org.apache.commons.lang3.StringUtils.isBlank"
//  + add this dependency to the pom.xml:
//  <dependency>
//       <groupId>org.apache.commons</groupId>
//        <artifactId>commons-lang3</artifactId>
//        <version>3.11</version>
//  </dependency>
//  + sustitute isBlank by StringUtils.isBlank in the code
//
//  +++++++++++++++++++++++

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.gson.Gson;
import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

@WebServlet("/test")
public class TestUploadServlet extends HttpServlet {
  private static final String RECIPES_DIRECTORY = "ABSOLUTE_PATH";
  private static final String RECIPE_FILE_EXTENSION = ".txt";
  // Name of the index used.
  private static final String INDEX_NAME = "recipes_index";
  /**
   * Receives a recipe's properties and creates an entity, for the database and a document for the index.
   */
  private void upload(
    String title,
    String imgURL,
    ArrayList<String> ingredients,
    String ingredientsString,
    ArrayList<String> stepList
  )
    throws Exception, InterruptedException, IOException {
    Index index = getIndex(INDEX_NAME);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // We use an index to store the documents; every document contains a recipe's title and its ID.
    //
    // The search makes use of the index to match partially on the recipe's title,
    // and returns the matching documents.
    //
    // Each document has a correspondent entity in the datastore and can be fetched by the ID.
    //
    // We are allocating the Recipe's ID before inserting the recipe entity in the datastore
    // because we need the same id to match the documents in the index to the entities in the datastore.

    KeyRange keyRange = datastore.allocateIds("Recipe", 1L);

    Entity recipeEntity = buildRecipeEntity(
      keyRange,
      title,
      imgURL,
      ingredients,
      stepList,
      30
    );
    Document recipeDocument = buildRecipeDocument(
      recipeEntity,
      title.toLowerCase(),
      ingredientsString.toLowerCase(),
      30
    );

    datastore.put(recipeEntity);
    index.put(recipeDocument);
  }

  /**
   * Returns the index that stores the recipes documents
   */
  private Index getIndex(String indexName) {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    return index;
  }

  /**
   * Reads the file given as parameter as a list of strings.
   */
  private static List<String> readFileInList(String fileName)
    throws Exception, IOException {
    return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
  }

  private void parseRecipe(String fileName) throws Exception, IOException {
    List<String> lines = readFileInList(RECIPES_DIRECTORY + fileName);
    String title = lines.get(2);
    String imgURL = lines.get(4);

    // When the scraper for the recipes was created the file structure was the following:
    // Line 0 - link to the recipe's page from the website it was scraped,
    // Line 1, 3, 5 - Blank line,
    // Line 2 - Recipe's title,
    // Line 4 - link to the recipe's image,
    // Line 6 - "Ingredients:",
    // The ingredients list is starting from the line 7.
    int currentLineIndex = 7;

    // Reads all the ingredients until the first blank line.
    // StringBuilder is used to create a string, it is not efficient to do string += another_string multiple times
    // because a string builder is created everytime.
    ArrayList<String> ingredientsList = new ArrayList<String>();
    StringBuilder ingredientsString = new StringBuilder();

    while (!StringUtils.isBlank(lines.get(currentLineIndex))) {
      ingredientsList.add(lines.get(currentLineIndex));
      ingredientsString.append(lines.get(currentLineIndex));
      currentLineIndex += 1;
    }

    //Removes the blank line and the paragraph: "Steps:".
    //Reads all the steps until the first blank line.
    currentLineIndex += 2;
    ArrayList<String> steps = new ArrayList<String>();
    while (
      (currentLineIndex < lines.size()) &&
      (!StringUtils.isBlank(lines.get(currentLineIndex)))
    ) {
      steps.add(lines.get(currentLineIndex));
      currentLineIndex += 1;
    }
    upload(title, imgURL, ingredientsList, ingredientsString.toString(), steps);
  }

  /**
   * Build a Recipe Entity by the given key.
   */
  private Entity buildRecipeEntity(
    KeyRange keyRange,
    String title,
    String imgURL,
    ArrayList<String> ingredients,
    ArrayList<String> stepList,
    int prep_time
  ) {
    Entity recipeEntity = new Entity(keyRange.getStart());
    String description =
      "Lorem quam dolor dapibus ante, sit amet pellentesque turpis lacus eu ipsum. Duis quis mi ut tortor interdum efficitur quis at mi. Pellentesque quis mauris vel ligula commodo scelerisque. In vulputate quam nisl, vel sagittis ipsum molestie quis. Suspendisse quis ipsum a sem aliquam euismod mattis sed metus.";
    Random rd = new Random();
    Double number = rd.nextDouble();
    recipeEntity.setProperty("title", title);
    recipeEntity.setProperty("imgURL", imgURL);
    recipeEntity.setProperty("ingredients", ingredients);
    recipeEntity.setProperty("stepList", stepList);
    recipeEntity.setProperty("index_title", title.toLowerCase());
    recipeEntity.setProperty("author", "Piece of Cake");
    recipeEntity.setProperty("description", description);
    recipeEntity.setProperty("difficulty", "easy");
    recipeEntity.setProperty("prep_time", prep_time);
    recipeEntity.setProperty("cook_time", "N/A");
    recipeEntity.setProperty("author_id", 1);
    recipeEntity.setProperty("random_number", number);

    return recipeEntity;
  }

  /**
   * Build a Recipe Document by the given Recipe Entity.
   */
  private Document buildRecipeDocument(
    Entity recipeEntity,
    String titleValue,
    String ingredientsValue,
    int prep_time
  ) {
    Document recipeDocument = Document
      .newBuilder()
      .setId(String.valueOf(recipeEntity.getKey().getId()))
      .addField(Field.newBuilder().setName("title").setText(titleValue))
      .addField(
        Field.newBuilder().setName("ingredients").setText(ingredientsValue)
      )
      .addField(
        Field
          .newBuilder()
          .setName("prep_time")
          .setNumber(prep_time)
      )
      .addField(
        Field
          .newBuilder()
          .setName("difficulty")
          .setText((String) recipeEntity.getProperty("difficulty"))
      )
      .build();

    return recipeDocument;
  }

  /**
   * Upload an user entity to the datastore.
   */
  private void uploadUser() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    KeyRange keyRange = datastore.allocateIds("User", 1);
    Entity userEntity = createUserEntity(keyRange);
    datastore.put(userEntity);
  }

  /**
   * Create an user entity.
   */
  private Entity createUserEntity(KeyRange keyRange) {
    Entity userEntity = new Entity(keyRange.getStart());
    userEntity.setProperty("name", "Piece of cake");
    userEntity.setProperty("email", "pieceofcake@google.com");
    userEntity.setProperty(
      "imgURL",
      "https://www.pngitem.com/pimgs/m/158-1589500_slice-of-cake-clip-art-black-and-white.png"
    );
    userEntity.setProperty(
      "bio",
      "Lorem quam dolor dapibus ante, sit amet pellentesque turpis lacus eu ipsum. Duis quis mi ut tortor interdum efficitur quis at mi. Pellentesque quis mauris vel ligula commodo scelerisque. In vulputate quam nisl, vel sagittis ipsum molestie quis. Suspendisse quis ipsum a sem aliquam euismod mattis sed metus."
    );

    return userEntity;
  }

  /**
   * doGet loops trough all the 60 text files in the recipe folder an calls 'parseRecipe' on them.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    try {
      uploadUser();
      for (int i = 1; i <= 60; i++) {
        String nameFile = Integer.toString(i) + RECIPE_FILE_EXTENSION;
        parseRecipe(nameFile);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
