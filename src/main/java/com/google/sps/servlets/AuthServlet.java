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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {

  /**
  * doGet checks if the user is currently logged in and returns the correct header
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    String url = "/login";
    UserService userService = UserServiceFactory.getUserService();
    HttpSession session = request.getSession();

    if (userService.isUserLoggedIn()) {
       setLogInAttributes(session, url, userService);
    } else {
       setLogOutAttributes(session, url, userService);
    }

    response.sendRedirect("/");
  }

  public void setLogInAttributes(HttpSession session, String url, UserService userService){
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = url;
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      session.setAttribute("isLogIn", 1);
      session.setAttribute("userEmail", userEmail);
      session.setAttribute("logoutURL", logoutUrl);
  }

  public void setLogOutAttributes(HttpSession session, String url, UserService userService){
      String urlToRedirectToAfterUserLogsIn = url;
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      session.setAttribute("isLogIn", 0);
      session.setAttribute("userEmail", null);
      session.setAttribute("loginURL", loginUrl);
  }

}
