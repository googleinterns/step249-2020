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
    List<Recipe> recipesListToReturn = new ArrayList<>();
    Integer timeValue;
    String stringToMatch = getParameter("searchterm", request);
    String difficulty = getParameter("difficulty", request);
    String time = getParameter("time", request);
    HashMap<String, String> difficultiesMap = createMapOfDifficulties();
    HashMap<Integer, String> timeIntervalsMap = createMapOfTimeIntervals();

    request.setAttribute("dataSearched", stringToMatch);
    request.setAttribute("difficultySearchedKey", difficulty);
    request.setAttribute("timeSearchedKey", time);
    request.setAttribute(
      "difficultySearchedValue",
      difficultiesMap.get(difficulty)
    );
    request.setAttribute("difficultyList", difficultiesMap);
    request.setAttribute("cookingTimeList", timeIntervalsMap);

    stringToMatch = trimAndRemoveCommas(stringToMatch);
    difficulty = trimAndRemoveCommas(difficulty);
    try {
      timeValue = Integer.valueOf(trimAndRemoveCommas(time));
    } catch (NumberFormatException e) {
      timeValue = 0;
    }
    request.setAttribute("timeSearchedValue", timeIntervalsMap.get(timeValue));

    try {
      recipesListToReturn =
        recipesMatching(
          request,
          response,
          stringToMatch,
          difficulty,
          timeValue
        );
    } catch (SearchQueryException e) {
      request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
    request.setAttribute("recipesList", recipesListToReturn);
    request.getRequestDispatcher("/search.jsp").forward(request, response);
  }

  /**
   * Creates a map of values for the dropdown select for difficulties.
   */
  private HashMap<String, String> createMapOfDifficulties() {
    HashMap<String, String> options = new HashMap<String, String>();
    options.put("hard", "Hard");
    options.put("medium", "Medium");
    options.put("easy", "Easy");
    return options;
  }

  /**
   * Creates a map of values for the dropdown select for difficulties.
   */
  private HashMap<Integer, String> createMapOfTimeIntervals() {
    HashMap<Integer, String> options = new HashMap<Integer, String>();
    options.put(120, "Less than 2h");
    options.put(60, "Less than 1h");
    options.put(30, "Less than 30 minutes");
    return options;
  }

  /**
   * Replace and return the string with no commas and no multiple consecutive or trailing spaces.
   */
  private String trimAndRemoveCommas(String stringToRemove) {
    if (!StringUtils.isBlank(stringToRemove)) {
      stringToRemove = stringToRemove.replaceAll("[,]+", " ");
      stringToRemove = stringToRemove.replaceAll("  +", " ");
    }
    return stringToRemove;
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
    List<Recipe> recipesMatchingList = new ArrayList<>();
    Index index = getIndex("recipes_index");

    Query query = buildQuery(stringToMatch, difficulty, time);
    Results<ScoredDocument> results = index.search(query);
    for (ScoredDocument document : results) {
      try {
        Entity recipeEntity = getRecipeEntityFromDocument(datastore, document);
        ArrayList<String> ingredientsMatched = returnMatchedIngredients(
          document
        );

        Recipe recipe = buildRecipe(recipeEntity, ingredientsMatched);
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
   * Returns a list of matched ingredients snippet by the search query.
   */
  private ArrayList<String> returnMatchedIngredients(ScoredDocument document) {
    ArrayList<String> ingredientsMatched = new ArrayList<>();

    List<Field> listFields = document.getExpressions();
    for (Field field : listFields) {
      if (!StringUtils.isBlank(field.getHTML())) ingredientsMatched.add(
        field.getHTML()
      );
    }

    return ingredientsMatched;
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
      .setLimit(10)
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
        ")";
    }
    if (!StringUtils.isBlank(stringToMatch)) {
      searchString = searchString + " OR ";
    }

    if (!StringUtils.isBlank(stringToMatch)) {
      searchString =
        searchString +
        "(ingredients=" +
        stringToMatch.replaceAll(" ", " AND ") +
        "))";
    }
    if (
      !StringUtils.isBlank(stringToMatch) && !StringUtils.isBlank(difficulty)
    ) {
      searchString = searchString + " AND ";
    }

    if (!StringUtils.isBlank(difficulty)) {
      searchString = searchString + "difficulty=\"" + difficulty + "\"";
    }

    if (
      !StringUtils.isBlank(difficulty) &&
      time != 0 ||
      !StringUtils.isBlank(stringToMatch) &&
      time != 0
    ) {
      searchString = searchString + " AND ";
    }
    if (time != 0) {
      searchString = searchString + "prep_time <=" + String.valueOf(time);
    }

    return searchString;
  }

  /**
   * Build a Recipe Object with the given Entity.
   */
  private Recipe buildRecipe(
    Entity recipeEntity,
    ArrayList<String> ingredientsMatched
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
    recipe.setMatchedIngredient(ingredientsMatched);

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
