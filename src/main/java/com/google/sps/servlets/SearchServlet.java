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
import java.util.Arrays;
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
  // The number of recipes to be displayed on the search list.
  private static final int RECIPES_LIMIT = 10;

  /**
   * Search and returns a list of first 10 recipes with the title matching the given parameters(searchterm, difficulty & time).
   * The index returns a list of documents in the ascending order by title.
   * Special characters inside the search terms are replaced with space.
   * We compute and return the intersection of the following lists:
   * - recipes that are matching by title;
   * - recipes that are matching by the ingredients.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, NullPointerException {
    String searchTerm = getParameter("searchterm", request);
    String difficulty = getParameter("difficulty", request);
    String time = getParameter("time", request);

    request.setAttribute("searchTerm", searchTerm);
    request.setAttribute("difficulty", difficulty);
    request.setAttribute("prepTime", time);

    searchTerm = sanitizeString(searchTerm);
    difficulty = sanitizeString(difficulty);
    request.setAttribute(
      "difficulty",
      difficulty
    );


    Integer timeValue;
    try {
      timeValue = Integer.valueOf(sanitizeString(time));
    } catch (NumberFormatException e) {
      timeValue = 0;
    }

    try {
      List<Recipe> recipesListToReturn = recipesMatching(
        request,
        response,
        searchTerm,
        difficulty,
        timeValue
      );
      request.setAttribute("recipesList", recipesListToReturn);
    } catch (SearchQueryException e) {
      response.setStatus(505);
    }
    request.getRequestDispatcher("/search.jsp").forward(request, response);
  }

  /**
   * Replace and return the string with no commas and no multiple consecutive or trailing spaces.
   */
  private String sanitizeString(String stringToSanitize) {
    if (!StringUtils.isBlank(stringToSanitize)) {
      stringToSanitize = stringToSanitize.replaceAll("(,\\s?)+", " ");
    }
    return stringToSanitize;
  }

  /**
   * If the fields exists, returns the parameter from the request, otherwise it returns an empty string.
   */
  private String getParameter(String field, HttpServletRequest request) {
    String parameter = new String();
    try {
      parameter = request.getParameter(field);
    } catch (NullPointerException e) {
      parameter = "";
    }
    return parameter;
  }

  /**
   * Returns a list of recipes matching the given title and ingredients.
   */
  private List<Recipe> recipesMatching(
    HttpServletRequest request,
    HttpServletResponse response,
    String stringToMatch,
    String difficulty,
    Integer time
  )
    throws ServletException, IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Index index = getIndex("recipes_index");
    Query query = buildQuery(stringToMatch, difficulty, time);
    Results<ScoredDocument> results = index.search(query);

    List<Recipe> matchingRecipes = new ArrayList<>();
    for (ScoredDocument document : results) {
      try {
        Entity recipeEntity = getRecipeEntityFromDocument(datastore, document);
        ArrayList<String> matchingIngredients = returnMatchingIngredients(
          document
        );

        Recipe recipe = buildRecipe(recipeEntity, matchingIngredients);
        matchingRecipes.add(recipe);
      } catch (EntityNotFoundException e) {
        response.setStatus(505);
        request.setAttribute("recipesList", new ArrayList<>());
        request.getRequestDispatcher("/search.jsp").forward(request, response);
        return matchingRecipes;
      }
    }
    return matchingRecipes;
  }

  /**
   * Returns a list of matching ingredients snippet by the search query.
   */
  private ArrayList<String> returnMatchingIngredients(ScoredDocument document) {
    ArrayList<String> matchingIngredients = new ArrayList<>();

    List<Field> fields = document.getExpressions();
    for (Field field : fields) {
      if (!StringUtils.isBlank(field.getHTML())) {
        matchingIngredients.add(field.getHTML());
      }
    }

    return matchingIngredients;
  }

  /**
   * Returns the index that stores the recipes documents
   */
  private Index getIndex(String indexName) {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    return index;
  }

  private Query buildQuery(
    String stringToMatch,
    String difficulty,
    Integer time
  ) {
    Cursor responseCursor = Cursor.newBuilder().build();
    QueryOptions options = QueryOptions
      .newBuilder()
      .setLimit(RECIPES_LIMIT)
      .setFieldsToSnippet("ingredients")
      .setSortOptions(
        SortOptions.newBuilder().setMatchScorer(MatchScorer.newBuilder())
      )
      .setCursor(responseCursor)
      .build();

    String searchString = createSearchString(stringToMatch, difficulty, time);
    Query query = Query.newBuilder().setOptions(options).build(searchString);

    return query;
  }

  /**
   * This function creates and returns the string used inside the search query.
   */
  private String createSearchString(
    String stringToMatch,
    String difficulty,
    Integer time
  ) {
    String searchString = new String();

    if (!StringUtils.isBlank(stringToMatch)) {
      searchString =
        searchString +
        "((title=" +
        stringToMatch.replaceAll(" ", " AND ") +
        ") OR (ingredients=" +
        stringToMatch.replaceAll(" ", " AND ") +
        "))";
    }

    if (!StringUtils.isBlank(difficulty)) {
      if (!StringUtils.isBlank(searchString)) {
        searchString = searchString + " AND ";
      }
      searchString = searchString + "difficulty=\"" + difficulty + "\"";
    }

    if (time != 0) {
      if (!StringUtils.isBlank(searchString)) {
        searchString = searchString + " AND ";
      }
      searchString = searchString + "prep_time <=" + String.valueOf(time);
    }

    return searchString;
  }

  /**
   * Build a Recipe Object with the given Entity.
   */
  private Recipe buildRecipe(
    Entity recipeEntity,
    ArrayList<String> ingredientsMatching
  ) {
    Long id = recipeEntity.getKey().getId();
    String name = (String) recipeEntity.getProperty("title");
    String imgURL = (String) recipeEntity.getProperty("imgURL");
    String description = (String) recipeEntity.getProperty("description");

    Recipe recipe = new Recipe();
    recipe.setId(id);
    recipe.setName(name);
    recipe.setImage(imgURL);
    recipe.setDescription(description);
    recipe.setMatchingIngredient(ingredientsMatching);

    return recipe;
  }

  /**
   * Build a Recipe Entity with the given Document.
   */
  private Entity getRecipeEntityFromDocument(
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
