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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recipe")
public class RecipeDetailsServlet extends HttpServlet {

  /**
   * doGet receives the request and returns the message sent as parameter
   **/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity recipeEntity = null;
    try {
      recipeEntity = getRecipeById(datastore, request.getParameter("id"));
    } catch (EntityNotFoundException e) {
      request.setAttribute("error", 1);
    }
    setRecipePropertiesInRequest(request, recipeEntity);
    request.getRequestDispatcher("/recipe.jsp").forward(request, response);
  }

  public Entity getRecipeById(DatastoreService datastore, String idRecipe)
    throws IOException, EntityNotFoundException {
    long id = Long.parseLong(idRecipe);
    Entity recipeEntity = datastore.get(KeyFactory.createKey("Recipe", id));
    return recipeEntity;
  }

  public void setRecipePropertiesInRequest(
    HttpServletRequest request,
    Entity recipeEntity
  )
    throws IOException {
    request.setAttribute("title", recipeEntity.getProperty("title"));
    request.setAttribute("author", recipeEntity.getProperty("author"));
    request.setAttribute("imgURL", recipeEntity.getProperty("imgURL"));
    request.setAttribute("difficulty", recipeEntity.getProperty("difficulty"));
    request.setAttribute("prepTime", recipeEntity.getProperty("prep_time"));
    request.setAttribute("cookTime", recipeEntity.getProperty("cook_time"));
    request.setAttribute(
      "ingredients",
      recipeEntity.getProperty("ingredients")
    );
    request.setAttribute("steps", recipeEntity.getProperty("stepList"));
  }
}
