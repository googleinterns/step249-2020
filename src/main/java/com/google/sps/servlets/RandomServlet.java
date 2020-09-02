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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns a random recipe*/
@WebServlet("/random")
public class RandomServlet extends HttpServlet {

  /**
   * This function creates an url from a random recipe's id.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    String url = "/recipe?id=" + returnRandomId(datastore);

    request.getRequestDispatcher(url).forward(request, response);
  }

  /**
   * This function generates a random double and returns the id of the first Entity with the closest value of "random_number".
   */
  private long returnRandomId(DatastoreService datastore) {
    Random randomGenerator = new Random();
    Double numberGenerated = randomGenerator.nextDouble();
    try {
      return closestSmallerRecipeRandomId(numberGenerated, datastore);
    } catch (IndexOutOfBoundsException e) {
      return closestGreaterRecipeRandomId(numberGenerated, datastore);
    }
  }

  /**
   * Return the recipe's id with the minimum difference between numberGenerated and recipe's random_number.
   */
  private long closestSmallerRecipeRandomId(
    Double numberGenerated,
    DatastoreService datastore
  ) {
    Filter propertyFilter = new FilterPredicate(
      "random_number",
      FilterOperator.LESS_THAN_OR_EQUAL,
      numberGenerated
    );
    Query query = new Query("Recipe")
      .setFilter(propertyFilter)
      .addSort("random_number", SortDirection.DESCENDING);

    PreparedQuery preparedQuery = datastore.prepare(query);
    List<Entity> recipeEntity = preparedQuery.asList(
      FetchOptions.Builder.withLimit(1)
    );
    return recipeEntity.get(0).getKey().getId();
  }

  /**
   * Return the recipe's id with the minimum difference between recipe's random_number and numberGenerated.
   */
  private long closestGreaterRecipeRandomId(
    Double numberGenerated,
    DatastoreService datastore
  ) {
    Filter propertyFilter = new FilterPredicate(
      "random_number",
      FilterOperator.GREATER_THAN_OR_EQUAL,
      numberGenerated
    );
    Query query = new Query("Recipe")
      .setFilter(propertyFilter)
      .addSort("random_number", SortDirection.ASCENDING);

    PreparedQuery preparedQuery = datastore.prepare(query);
    List<Entity> recipeEntity = preparedQuery.asList(
      FetchOptions.Builder.withLimit(1)
    );
    return recipeEntity.get(0).getKey().getId();
  }
}
