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

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.gson.Gson;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

@WebServlet("/recipe_post")
public class RecipePostServlet extends HttpServlet {

  /**
   * doPost creates a new recipe entity with the attributes inputted in the post
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String title = request.getParameter("title");
    String imgURL = getUploadedFileUrl(request, "image");
    String description = request.getParameter("description");
    int prepTime = getPrepTime(request);
    String difficulty = request.getParameter("difficulty");
    ArrayList<String> ingredients = getIngredient(request);
    ArrayList<String> stepList = getSteps(request);

    IndexSpec indexSpec = IndexSpec
      .newBuilder()
      .setName("recipesIndex")
      .build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    KeyRange keyRange = datastore.allocateIds("Recipe", 1L);

    Entity recipeEntity = buildRecipeEntity(
      keyRange,
      title,
      imgURL,
      description,
      prepTime,
      difficulty,
      ingredients,
      stepList,
      request
    );
    Document recipeDocument = buildRecipeDocumentForIndexing(recipeEntity);

    datastore.put(recipeEntity);
    index.put(recipeDocument);

    response.sendRedirect("/recipe?id=" + recipeEntity.getKey().getId());
  }

  private Entity buildRecipeEntity(
    KeyRange keyRange,
    String title,
    String imgURL,
    String description,
    int prepTime,
    String difficulty,
    ArrayList<String> ingredients,
    ArrayList<String> stepList,
    HttpServletRequest request
  ) {
    Entity recipeEntity = new Entity(keyRange.getStart());

    recipeEntity.setProperty("title", title);
    recipeEntity.setProperty("index_title", title.toLowerCase());
    recipeEntity.setProperty("imgURL", imgURL);
    recipeEntity.setProperty("ingredients", ingredients);
    recipeEntity.setProperty("stepList", stepList);
    recipeEntity.setProperty(
      "author",
      request.getSession().getAttribute("name")
    );
    recipeEntity.setProperty("description", description);
    recipeEntity.setProperty("difficulty", difficulty);
    recipeEntity.setProperty("prep_time", prepTime);
    recipeEntity.setProperty(
      "author_id",
      request.getSession().getAttribute("id")
    );

    return recipeEntity;
  }

  private Document buildRecipeDocumentForIndexing(Entity recipeEntity) {
    Document recipeDocument = Document
      .newBuilder()
      .setId(String.valueOf(recipeEntity.getKey().getId()))
      .addField(
        Field
          .newBuilder()
          .setName("title")
          .setText((String) recipeEntity.getProperty("index_title"))
      )
      .addField(
        Field.newBuilder().setName("ingredients").setText( String.join(" ",(ArrayList<String>) recipeEntity.getProperty("ingredients")))
      )
      .addField(
        Field
          .newBuilder()
          .setName("prep_time")
          .setNumber((int) recipeEntity.getProperty("prep_time"))
      )
      .addField(
        Field
          .newBuilder()
          .setName("difficulty")
          .setText((String) recipeEntity.getProperty("difficulty"))
      )
      .build();

    return recipeDocument;
  }

  private int getPrepTime(HttpServletRequest request) {
    int min =
      Integer.parseInt(request.getParameter("hour")) *
      60 +
      Integer.parseInt(request.getParameter("min"));
    return min;
  }

  private ArrayList<String> getSteps(HttpServletRequest request) {
    String[] param = request.getParameterValues("step[]");
    ArrayList<String> steps = new ArrayList<String>();
    for (String i : param) steps.add(i);
    return steps;
  }

  private ArrayList<String> getIngredient(HttpServletRequest request) {
    String[] quantity = request.getParameterValues("ingredients[][quantity]");
    String[] measure = request.getParameterValues("ingredients[][measure]");
    String[] ingr = request.getParameterValues("ingredients[][ingredient]");
    ArrayList ingredientsList = new ArrayList();
    for (int i = 0; i < quantity.length; i++) {
      String ingredient =
        "(" + quantity[i] + ", " + measure[i] + ", " + ingr[i] + ")";
      ingredientsList.add(ingredient);
    }
    return ingredientsList;
  }

  private String getUploadedFileUrl(
    HttpServletRequest request,
    String formInputElementName
  ) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    // User submitted form without selecting a file, so we can't get a URL. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    // We could check the validity of the file here, e.g. to make sure it's an image file
    // https://stackoverflow.com/q/10779564/873165

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

    // To support running in Google Cloud Shell with AppEngine's devserver, we must use the relative
    // path to the image, rather than the path returned by imagesService which contains a host.
    try {
      URL url = new URL(imagesService.getServingUrl(options));
      return url.getPath();
    } catch (MalformedURLException e) {
      return imagesService.getServingUrl(options);
    }
  }
}
