//
//  ++++++ READ ME +++++++
//
//  How to download the 60 init recipes to your database.
//
//  STEP1: Create a new servlet named TestUploadServlet.java in your src/main/java/com/google/sps/servlets/ directory
//  STEP2: Go to line 119 and  change the string to your absolute path to where your recipe folder is (mine is /home/beatricemarch/capstone/step249-2020/src/main/webapp/one-off/recipe/ )
//  STEP3: Copy and paste the code below in the TestUploadServlet.java
//  STEP4: Run the script from the url "/test" (you can run it both on localhost and in the deployed version)
//  STEP5: Delete the servlet.
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
//
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

  /**
   * Receives a recipe's properties and creates an entity, for the database and a document for the index.
   */
  private void upload(
    String title,
    String imgURL,
    ArrayList<String> ingredients,
    ArrayList<String> stepList
  )
    throws Exception, InterruptedException, IOException {
    IndexSpec indexSpec = IndexSpec
      .newBuilder()
      .setName("recipesIndex")
      .build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

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
    // This id will be used to compute the random recipe, because the allocated id's are consecutive
    // and they start from the value 1.

    KeyRange keyRange = datastore.allocateIds("Recipe", 1L);

    Entity recipeEntity = buildRecipeEntity(
      keyRange,
      title,
      imgURL,
      ingredients,
      stepList
    );
    Document recipeDocument = buildRecipeDocumentForIndexing(recipeEntity);

    datastore.put(recipeEntity);
    index.put(recipeDocument);
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
    int currentLineIndex = 7;

    /**
     * Reads all the ingredients until the first blank line.
     */
    ArrayList<String> ingredients = new ArrayList<String>();
    while (!StringUtils.isBlank(lines.get(currentLineIndex))) {
      ingredients.add(lines.get(currentLineIndex));
      currentLineIndex += 1;
    }

    /**
     * Removes the blank line and the paragraph: "Steps:".
     * Reads all the steps until the first blank line.
     */
    currentLineIndex += 2;
    ArrayList<String> steps = new ArrayList<String>();
    while (
      (currentLineIndex < lines.size()) &&
      (!StringUtils.isBlank(lines.get(currentLineIndex)))
    ) {
      steps.add(lines.get(currentLineIndex));
      currentLineIndex += 1;
    }
    upload(title, imgURL, ingredients, steps);
  }

  /**
   * Build a Recipe Entity by the given key.
   */
  private Entity buildRecipeEntity(
    KeyRange keyRange,
    String title,
    String imgURL,
    ArrayList<String> ingredients,
    ArrayList<String> stepList
  ) {
    Entity recipeEntity = new Entity(keyRange.getStart());
    Random rd = new Random();
    Double number = rd.nextDouble();
    recipeEntity.setProperty("title", title);
    recipeEntity.setProperty("index_title", title.toLowerCase());
    recipeEntity.setProperty("imgURL", imgURL);
    recipeEntity.setProperty("ingredients", ingredients);
    recipeEntity.setProperty("stepList", stepList);
    recipeEntity.setProperty("author", "Piece of Cake");
    recipeEntity.setProperty("difficulty", "N/A");
    recipeEntity.setProperty("prep_time", "N/A");
    recipeEntity.setProperty("cook_time", "N/A");
    recipeEntity.setProperty("author_id", 1);
    recipeEntity.setProperty("random_number", number);

    return recipeEntity;
  }

  /**
   * Build a Recipe Document by the given Recipe Entity
   */
  private Document buildRecipeDocumentForIndexing(Entity recipeEntity) {
    Document recipeDocument = Document
      .newBuilder()
      .setId(String.valueOf(recipeEntity.getKey().getId()))
      .addField(
        Field
          .newBuilder()
          .setName("title")
          .setText((String) recipeEntity.getProperty("index_title"))
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
