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
       
//       List<String> lines = readFileInList("/test-recipes/recipe/"+fileName);

       List<String> lines = readFileInList("/home/beatricemarch/capstone/step249-2020/src/main/webapp/test-recipes/recipe/"+fileName);
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
       while ((!lines.get(currentLineIndex).isBlank()) && (currentLineIndex < lines.size())){
            steps.add(lines.get(currentLineIndex)); 
            currentLineIndex += 1;
       }
       upload(title, imgURL, ingredients, steps);
    
    }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    try{
     String nameFile = request.getParameter("file");
     parseRecipe(nameFile);
    } catch (Exception e) {
    e.printStackTrace();
    }
   
  }
}