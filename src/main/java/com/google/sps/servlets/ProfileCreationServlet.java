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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
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
    if (((int)session.getAttribute("isLoggedIn"))== 1) {
        editProfile(request, response, session);
    } else {
        createProfile(request, response, session);
    }
  }

  public void createProfile(
    HttpServletRequest request,
    HttpServletResponse response,
    HttpSession session
   )  throws IOException {
    String username = request.getParameter("username");
    String bio = request.getParameter("bio");
    String email = (String) session.getAttribute("uregisteredUserEmail");
 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity userEntity = new Entity("User");
    setEnitityAttributes(userEntity, username, bio, email);
    datastore.put(userEntity);

    setSessionAttributes(session, userEntity, username, bio);

    response.sendRedirect("/user?id=" + userEntity.getKey().getId());
  }

   public void editProfile(
    HttpServletRequest request,
    HttpServletResponse response,
    HttpSession session
   )  throws IOException {
    String username = request.getParameter("username");
    String bio = request.getParameter("bio");
    String email = "";

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    long id = (long) session.getAttribute("id");
    Entity userEntity = null;
    try {
      userEntity = getUserById(datastore, id);
      email = (String) userEntity.getProperty("email");
    } catch (EntityNotFoundException e) {
      request.setAttribute("error", 1);
    }

    setEnitityAttributes(userEntity, username, bio, email);
    datastore.put(userEntity);

    session.setAttribute("name", username);
    session.setAttribute("bio", bio);

    response.sendRedirect("/user?id=" + id);
  }

  public void setEnitityAttributes(
    Entity userEntity,
    String username,
    String bio,
    String email
  ) {
    userEntity.setProperty("email", email);
    userEntity.setProperty("name", username);
    userEntity.setProperty("bio", bio);
    userEntity.setProperty("imageURL", "images/default.png");
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
}
