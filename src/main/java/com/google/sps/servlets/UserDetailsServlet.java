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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user")
public class UserDetailsServlet extends HttpServlet {

  /**
   * doGet receives the request and returns the user's data with the given id
   **/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = null;
    Long id = Long.parseLong(request.getParameter("id"));
    try {
      userEntity = getUserById(datastore, id);
      User userRequested = createUserObject(userEntity);
      ArrayList<Recipe> recipesList = userRecipesList(id, datastore);

      request.setAttribute("user", userRequested);
      request.setAttribute("recipesList", recipesList);
    } catch (EntityNotFoundException e) {
      request.setAttribute("error", 1);
    }
    request.getRequestDispatcher("/user.jsp").forward(request, response);
  }

  private Entity getUserById(DatastoreService datastore, Long id)
    throws IOException, EntityNotFoundException, NullPointerException {
    Entity userEntity = datastore.get(KeyFactory.createKey("User", id));
    return userEntity;
  }

  private User createUserObject(Entity userEntity) throws IOException {
    User user = new User();
    user.setId(userEntity.getKey().getId());
    user.setName((String) userEntity.getProperty("name"));
    user.setBio((String) userEntity.getProperty("bio"));
    user.setImage((String) userEntity.getProperty("imageURL"));
    user.setEmail((String) userEntity.getProperty("email"));

    return user;
  }

  private ArrayList<Recipe> userRecipesList(
    Long userId,
    DatastoreService datastore
  ) {
    ArrayList<Recipe> recipesList = new ArrayList<>();
    Filter propertyFilter = new FilterPredicate(
      "author_id",
      FilterOperator.EQUAL,
      userId
    );
    Query q = new Query("Recipe").setFilter(propertyFilter);
    PreparedQuery pq = datastore.prepare(q);

    List<Entity> recipesEntityList = pq.asList(
      FetchOptions.Builder.withDefaults()
    );
    for (Entity entity : recipesEntityList) {
      recipesList.add(buildRecipe(entity));
    }

    return recipesList;
  }

  private Recipe buildRecipe(Entity recipeEntity) {
    Long id = recipeEntity.getKey().getId();
    String name = (String) recipeEntity.getProperty("title");
    String imageUrl = (String) recipeEntity.getProperty("imgURL");
    String description = (String) recipeEntity.getProperty("description");
    Recipe recipe = new Recipe();
    recipe.setId(id);
    recipe.setName(name);
    recipe.setImage(imageUrl);
    recipe.setDescription(description);

    return recipe;
  }
}
