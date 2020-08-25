//  
//
//  ++++++ READ ME +++++++
//
//  How to download the 60 init recipes to your database.
// 
//  STEP1: Create a new servlet named TestUploadServlet.java in your src/main/java/com/google/sps/servlets/ directory
//  STEP2: Go to line 77 and  change the string to your absolute path to where your recipe folder is (mine is /home/beatricemarch/capstone/step249-2020/src/main/webapp/test-recipes/recipe/ )
//  STEP3: Copy and paste the code below in the TestUploadServlet.java
//  STEP4: Run the script from the url "/test" (you can run it both on localhost and in the deployed version)
//  STEP5: Delete the servlet.
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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/test")
public class TestUploadServlet extends HttpServlet {

    public void upload(String  title, String imgURL, ArrayList<String> ingredients, ArrayList<String> stepList) throws Exception, IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
        Entity recipeEntity = new Entity("Recipe");

        recipeEntity.setProperty("title", title);
        recipeEntity.setProperty("imgURL", imgURL);
        recipeEntity.setProperty("ingredients", ingredients); 
        recipeEntity.setProperty("stepList", stepList);

        datastore.put(recipeEntity);
    }

    public static List<String> readFileInList(String fileName) throws Exception, IOException  { 
  
         List<String> lines = Collections.emptyList(); 
         lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
         return lines; 
    } 

    public void parseRecipe(String fileName) throws Exception, IOException  {

       List<String> lines = readFileInList("INSERT ABSOLUTE PATH HERE"+fileName);
       String title = lines.get(2);
       String imgURL = lines.get(4);
       int currentLineIndex = 7;

       ArrayList<String> ingredients = new ArrayList<String>();
       while (!lines.get(currentLineIndex).isBlank()){
            ingredients.add(lines.get(currentLineIndex));
            currentLineIndex += 1;
       }

       currentLineIndex += 2;
       ArrayList<String> steps = new ArrayList<String>();
       while ((currentLineIndex < lines.size()) && (!lines.get(currentLineIndex).isBlank())){
            steps.add(lines.get(currentLineIndex)); 
            currentLineIndex += 1;
       }
       upload(title, imgURL, ingredients, steps);
    
    }

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    try{

     for ( int i = 1; i <= 60; i++) {
       String nameFile = Integer.toString(i)+".txt";
       parseRecipe(nameFile);
     }
    } catch (Exception e) {
    e.printStackTrace();
    }
   
  }
}