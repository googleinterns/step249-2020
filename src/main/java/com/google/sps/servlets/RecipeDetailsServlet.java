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
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
        String idRecipe = request.getParameter( "id");

// get the recipe entity
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        long id = Long.parseLong(idRecipe);
        Entity recipeEntity = datastore.Lookup(KeyFactory.createKey("Recipe", id));


// create a new recipe object

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setAuthor(author);
        recipe.setImage(recipeEntity["imgURL"]);
        recipe.setDifficulty(difficulty);
        recipe.setPrepTime(pTime);
        recipe.setCookTime(cTime);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

// send all the parameters to the request

        request.setAttribute("title", recipe.getName());
        request.setAttribute("author", recipe.getAuthor());
        request.setAttribute("imgURL", recipe.getId());
        request.setAttribute("difficulty", recipe.getDifficulty());
        request.setAttribute("prepTime", recipe.getPrepTime());
        request.setAttribute("cookTime", recipe.getcookTime());
        request.setAttribute("ingredient", recipe.getIngredients());
        request.setAttribute("steps", recipe.getSteps());

        request.getRequestDispatcher("/recipe.jsp").forward(request, response);
    }
}
