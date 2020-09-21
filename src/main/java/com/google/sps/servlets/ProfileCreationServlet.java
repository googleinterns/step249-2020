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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/profile_creation")
public class ProfileCreationServlet extends HttpServlet {

  /**
   * doPost creates a new user entity and assigns it the property inputed in the user creation form
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    HttpSession session = request.getSession();
    Integer isLoggedIn = (Integer) session.getAttribute("isLoggedIn");
    if ((isLoggedIn != null) && (isLoggedIn == 1)) {
      editProfile(request, response, session);
    } else {
      createProfile(request, response, session);
    }
  }

  public void createProfile(
    HttpServletRequest request,
    HttpServletResponse response,
    HttpSession session
  )
    throws IOException {
    String username = request.getParameter("username");
    String bio = request.getParameter("bio");
    String email = (String) session.getAttribute("unregisteredUserEmail");
    String imageUrl = getUploadedFileUrl(request, "image");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity userEntity = new Entity("User");
    setEnitityAttributes(userEntity, imageUrl, username, bio, email);
    datastore.put(userEntity);

    setSessionAttributes(session, userEntity, username, bio);

    response.sendRedirect("/user?id=" + userEntity.getKey().getId());
  }

  public void editProfile(
    HttpServletRequest request,
    HttpServletResponse response,
    HttpSession session
  )
    throws IOException {
    String username = request.getParameter("username");
    String bio = request.getParameter("bio");
    String email = (String) session.getAttribute("userEmail");
    String imageUrl = getUploadedFileUrl(request, "image");
    long userId = (long) session.getAttribute("id");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = null;
    try {
      userEntity = getUserById(datastore, userId);
      email = (String) userEntity.getProperty("email");
    } catch (EntityNotFoundException e) {
      request.setAttribute("error", 1);
    }

    setEnitityAttributes(userEntity, imageUrl, username, bio, email);
    datastore.put(userEntity);

    session.setAttribute("name", username);
    session.setAttribute("bio", bio);

    response.sendRedirect("/user?id=" + userId);
  }

  public void setEnitityAttributes(
    Entity userEntity,
    String imageUrl,
    String username,
    String bio,
    String email
  ) {
    userEntity.setProperty("email", email);
    userEntity.setProperty("name", username);
    userEntity.setProperty("bio", bio);
    if (imageUrl != null && !imageUrl.isEmpty()) userEntity.setProperty(
      "imageURL",
      imageUrl
    );
  }

  public void setSessionAttributes(
    HttpSession session,
    Entity userEntity,
    String username,
    String bio
  ) {
    session.setAttribute("name", username);
    session.setAttribute("bio", bio);
    session.setAttribute("isLoggedIn", 1);
    session.setAttribute("id", userEntity.getKey().getId());
  }

  public Entity getUserById(DatastoreService datastore, long id)
    throws IOException, EntityNotFoundException {
    Entity userEntity = datastore.get(KeyFactory.createKey("User", id));
    return userEntity;
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
