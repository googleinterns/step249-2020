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

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  /*
   * Search and returns a list of first 10 recipes with the title matching the given parameter(searchterm).
   * The index returns a list of documents in the ascending order by title
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    int error = 0;
    List<Recipe> recipesList = new ArrayList<>();
    String titleToMatch = request.getParameter("searchterm");
    Index index = getIndex();
    Cursor responseCursor = Cursor.newBuilder().build();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    QueryOptions options = QueryOptions
      .newBuilder()
      .setLimit(10)
      .setFieldsToSnippet("title")
      .setCursor(responseCursor)
      .setSortOptions(
        SortOptions
          .newBuilder()
          .addSortExpression(
            SortExpression
              .newBuilder()
              .setExpression("title")
              .setDirection(SortExpression.SortDirection.ASCENDING)
              .setDefaultValue("")
          )
      )
      .build();

    Query query = Query
      .newBuilder()
      .setOptions(options)
      .build(titleToMatch.toLowerCase());

    Results<ScoredDocument> results = index.search(query);

    for (ScoredDocument entity : results) {
      try {
        String id = entity.getId();
        Entity recipeEntity = datastore.get(
          KeyFactory.createKey("Recipe", Long.parseLong(id))
        );

        String name = (String) recipeEntity.getProperty("title");

        Recipe recipe = new Recipe();
        String description =
          "Lorem quam dolor dapibus ante, sit amet pellentesque turpis lacus eu ipsum. Duis quis mi ut tortor interdum efficitur quis at mi. Pellentesque quis mauris vel ligula commodo scelerisque. In vulputate quam nisl, vel sagittis ipsum molestie quis. Suspendisse quis ipsum a sem aliquam euismod mattis sed metus.";

        recipe.setId(Long.parseLong(id));
        recipe.setName(name);
        recipe.setImage((String) recipeEntity.getProperty("imgURL"));
        recipe.setDescription(description);

        recipesList.add(recipe);
      } catch (EntityNotFoundException e) {
        error = 1;
      }
    }

    request.setAttribute("recipesList", recipesList);
    request.setAttribute("error", error);

    request.getRequestDispatcher("/search.jsp").forward(request, response);
  }

  /*
   * Returns the index that stores the recipes documents
   */
  private Index getIndex() {
    IndexSpec indexSpec = IndexSpec
      .newBuilder()
      .setName("recipesIndex")
      .build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    return index;
  }
}
