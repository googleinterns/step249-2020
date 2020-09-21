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
import com.google.appengine.api.datastore.EntityNotFoundException;
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

  // Name of the index used.
  private static final String INDEX_NAME = "recipes_index";

  /**
   * doGet retrieves the attributes of the recipe to be edited and sets them as reqest attributes
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity recipeEntity = null;
    try {
      recipeEntity = getRecipeById(datastore, request.getParameter("id"));
    } catch (EntityNotFoundException e) {
      request.setAttribute("error", 1);
    }
    if (recipeEntity != null) {
        request.setAttribute("edit", 1);
        request.setAttribute("recipeId", request.getParameter("id"));
        setEditRecipePropertiesInRequest(request, recipeEntity);
    }
    request.getRequestDispatcher("/recipe_post.jsp").forward(request, response);
  }         

  /**
   * doPost checks if the recipe is being edited or if it is a new recipe being submitted 
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (request.getParameter("edited").toString().equals("yes")){
        editRecipe(request, response);
    } else { 
        createRecipe(request, response);
    }
  }
 
  public void createRecipe(HttpServletRequest request, HttpServletResponse response) throws IOException{
    String title = request.getParameter("title");
    String imgURL = getUploadedFileUrl(request, "image");
    String description = request.getParameter("description");
    int prepTime = getPrepTime(request);
    String difficulty = request.getParameter("difficulty");
    ArrayList<String> ingredients = getIngredients(request);
    ArrayList<String> stepList = getSteps(request);

    IndexSpec indexSpec = IndexSpec
      .newBuilder()
      .setName(INDEX_NAME)
      .build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    KeyRange keyRange = datastore.allocateIds("Recipe", 1L);
    Entity recipeEntity = new Entity(keyRange.getStart());

    recipeEntity = setRecipeEntityProperties(
      recipeEntity,
      title,
      imgURL,
      description,
      prepTime,
      difficulty,
      ingredients,
      stepList,
      request
    );
    Document recipeDocument = buildRecipeDocumentForIndexing(recipeEntity, ingredients);

    datastore.put(recipeEntity);
    index.put(recipeDocument);
   
    response.sendRedirect("/recipe?id=" + Long.toString(recipeEntity.getKey().getId()));
  }

  public void editRecipe(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String title = request.getParameter("title");
      String imgURL = getUploadedFileUrl(request, "image");
      String description = request.getParameter("description");
      int prepTime = getPrepTime(request);
      String difficulty = request.getParameter("difficulty");
      ArrayList<String> ingredients = getIngredients(request);
      ArrayList<String> stepList = getSteps(request);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Entity recipeEntity = null;
      try {
             recipeEntity = getRecipeById(datastore, request.getParameter("recipeId"));
      } catch (EntityNotFoundException e) {
             request.setAttribute("error", 1);
      }
      if (recipeEntity != null) {
        recipeEntity = setRecipeEntityProperties(
             recipeEntity,
             title,
             imgURL,
             description,
             prepTime,
             difficulty,
             ingredients,
             stepList,
             request
          );

        datastore.put(recipeEntity);
        response.sendRedirect("/recipe?id=" + Long.toString(recipeEntity.getKey().getId()));
      }else{ response.sendRedirect("/");}
      
  }

  private Entity setRecipeEntityProperties(
    Entity recipeEntity,
    String title,
    String imgURL,
    String description,
    int prepTime,
    String difficulty,
    ArrayList<String> ingredients,
    ArrayList<String> stepList,
    HttpServletRequest request
  ) {
    recipeEntity.setProperty("title", title);
    recipeEntity.setProperty("index_title", title.toLowerCase());
    if (imgURL != null && !imgURL.isEmpty()) recipeEntity.setProperty("imgURL", imgURL);
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

  private Document buildRecipeDocumentForIndexing(Entity recipeEntity, ArrayList<String> ingredients) {

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
        Field.newBuilder().setName("ingredients").setText(String.join(" ", ingredients))
      )
      .addField(
        Field
          .newBuilder()
          .setName("prep_time")
          .setNumber((Integer) recipeEntity.getProperty("prep_time"))
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
    for (String i : param) {
        if (!i.isEmpty()) steps.add(i);
    }
    return steps;
  }

  private ArrayList<String> getIngredients(HttpServletRequest request) {
    String[] param = request.getParameterValues("ingredients[]");
    ArrayList<String> ingredients = new ArrayList<String>();
    for (String i : param) {
       if (!i.isEmpty()) ingredients.add(i);
    }
    return ingredients;
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

 public void setEditRecipePropertiesInRequest(
    HttpServletRequest request,
    Entity recipeEntity
  )
    throws IOException {
    String difficulty = (String)recipeEntity.getProperty("difficulty");
    long hour = (long) recipeEntity.getProperty("prep_time")/60;
    long min = (long) recipeEntity.getProperty("prep_time")%60;

    request.setAttribute("title", recipeEntity.getProperty("title"));
    request.setAttribute(
      "description",
      recipeEntity.getProperty("description")
    );
    request.setAttribute(difficulty+"Checked", "checked");
    request.setAttribute("selected"+hour, "selected");
    request.setAttribute("selected"+min, "selected");
    request.setAttribute(
      "ingredients",
      recipeEntity.getProperty("ingredients")
    );
    request.setAttribute("steps", recipeEntity.getProperty("stepList"));
 }

 public Entity getRecipeById(DatastoreService datastore, String idRecipe)
    throws IOException, EntityNotFoundException {
    long id = Long.parseLong(idRecipe);
    Entity recipeEntity = datastore.get(KeyFactory.createKey("Recipe", id));
    return recipeEntity;
 }

}
