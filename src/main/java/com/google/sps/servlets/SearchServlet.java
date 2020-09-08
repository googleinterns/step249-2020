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
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.search.*;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  /**
   * Search and returns a list of first 10 recipes with the title matching the given parameter(searchterm).
   * The index returns a list of documents in the ascending order by title.
   * Special characters inside the search terms are replaced with space.
   * We compute and return the intersection of the following lists:
   * - recipes that are matching by title;
   * - recipes that are matching by the ingredients.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String stringToMatch = request
      .getParameter("searchterm")
      .replaceAll("[^a-zA-Z0-9]+", " ")
      .trim();
    request.setAttribute("dataSearched", stringToMatch);

    List<Recipe> recipesListToReturn = recipesMatching(
      request,
      response,
      stringToMatch
    );

    request.setAttribute("recipesList", recipesListToReturn);
    request.getRequestDispatcher("/search.jsp").forward(request, response);
  }

  /**
   * Returns a list of recipes matching the given title and ingredients.
   */
  private List<Recipe> recipesMatching(
    HttpServletRequest request,
    HttpServletResponse response,
    String stringToMatch
  )
    throws ServletException, IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<Recipe> recipesMatchingList = new ArrayList<>();
    Index index = getIndex("recipes_index");

    Query query = buildQuery(stringToMatch);
    Results<ScoredDocument> results = index.search(query);
    for (ScoredDocument entity : results) {
      try {
        Entity recipeEntity = getRecipeEntityFromDocumentEntity(
          datastore,
          entity
        );

        Recipe recipe = buildRecipe(recipeEntity);
        recipesMatchingList.add(recipe);
      } catch (EntityNotFoundException e) {
        response.setStatus(505);
        request.getRequestDispatcher("/search.jsp").forward(request, response);
        return recipesMatchingList;
      }
    }
    return recipesMatchingList;
  }

  /**
   * Returns the index that stores the recipes documents
   */
  private Index getIndex(String indexName) {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    return index;
  }

  private Query buildQuery(String stringToMatch) {
    Cursor responseCursor = Cursor.newBuilder().build();
    QueryOptions options = QueryOptions
      .newBuilder()
      .setLimit(60)
      .setSortOptions(
        SortOptions.newBuilder().setMatchScorer(MatchScorer.newBuilder())
      )
      .setCursor(responseCursor)
      .build();

    Query query = Query
      .newBuilder()
      .setOptions(options)
      .build(stringToMatch.toLowerCase());

    return query;
  }

  /**
   * Build a Recipe Object with the given Entity.
   */
  private Recipe buildRecipe(Entity recipeEntity) {
    Long id = recipeEntity.getKey().getId();
    String name = (String) recipeEntity.getProperty("title");
    String imgURL = (String) recipeEntity.getProperty("imgURL");
    String description = (String) recipeEntity.getProperty("description");

    Recipe recipe = new Recipe();
    recipe.setId(id);
    recipe.setName(name);
    recipe.setImage(imgURL);
    recipe.setDescription(description);

    return recipe;
  }

  /**
   * Build a Recipe Entity with the given Document.
   */
  private Entity getRecipeEntityFromDocumentEntity(
    DatastoreService datastore,
    ScoredDocument entity
  )
    throws EntityNotFoundException {
    String id = entity.getId();
    Entity recipeEntity = datastore.get(
      KeyFactory.createKey("Recipe", Long.parseLong(id))
    );

    return recipeEntity;
  }
}
