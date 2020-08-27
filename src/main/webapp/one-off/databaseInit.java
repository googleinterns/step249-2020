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
//  + add this dependncy to the pom.xl: 
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
import com.google.appengine.api.datastore.KeyRange;

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
  /*
  * Receives a recipe's properties and creates an entity, for the database and a document for the index
  */
  public void upload(String title, String imgURL, ArrayList<String> ingredients, ArrayList<String> stepList)
    throws Exception, InterruptedException, IOException {
    
    IndexSpec indexSpec = IndexSpec.newBuilder().setName("recipesIndex").build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    KeyRange keyRange = datastore.allocateIds("Recipe", 1L);

    Entity recipeEntity = new Entity(keyRange.getStart());

    recipeEntity.setProperty("title", title);
    recipeEntity.setProperty("index_title", title.toLowerCase());
    recipeEntity.setProperty("imgURL", imgURL);
    recipeEntity.setProperty("ingredients", ingredients);
    recipeEntity.setProperty("stepList", stepList);
    recipeEntity.setProperty("author", "Piece of Cake");

   Document recipeDocument =
        Document.newBuilder()
            .setId(String.valueOf(recipeEntity.getKey().getId()))
            .addField(Field.newBuilder().setName("title").setText(title))
            .addField(Field.newBuilder().setName("imgURL").setText(imgURL))
            .build();

    datastore.put(recipeEntity);
    index.put(recipeDocument);
  }

  public static List<String> readFileInList(String fileName)
    throws Exception, IOException {
    List<String> lines = Collections.emptyList();
    lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
    return lines;
  }

  public void parseRecipe(String fileName) throws Exception, IOException {
    List<String> lines = readFileInList(
      "/home/donpaul/step/capstone/step249-2020/src/main/webapp/one-off/recipes/" +
      fileName
    );
    String title = lines.get(2);
    String imgURL = lines.get(4);
    int currentLineIndex = 7;

    ArrayList<String> ingredients = new ArrayList<String>();
    while (!StringUtils.isBlank(lines.get(currentLineIndex))) {
      ingredients.add(lines.get(currentLineIndex));
      currentLineIndex += 1;
    }

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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    try {
      for (int i = 1; i <= 60; i++) {
        String nameFile = Integer.toString(i) + ".txt";
        parseRecipe(nameFile);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}