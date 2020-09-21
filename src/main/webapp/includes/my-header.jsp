<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<% UserService userService = UserServiceFactory.getUserService();
   String loginUrl = userService.createLoginURL("/login"); %>


<c:set var = "login"  value = "<%= loginUrl %>" />
<div class="header wrapper">
     <nav class="navbar">
        <a href="/" class="link nav-item logo">Piece of Cake</a>
        <form action="search" class="form-line">
            <div class="input-group">
          <input type="text" class="form-control searchbar" placeholder="Recipe title or ingredients" name="searchterm" value="${searchterm}"/>
                <div class="input-group-append">
                    <button type="submit" class="btn btn-secondary" value="search">Search</button>
                </div>
            </div>
        </form>
        <div class="links">
           <a href="/recipe_post.jsp" class="link nav-item mx-3">Post a Recipe</a>
            <c:choose>
             <c:when test="${isLoggedIn == 1}">
                  <a href="/user?id=${id}" class="link nav-item mx-3">My Profile</a>
                  <a href="${logoutURL}" class="link nav-item mx-3">Log Out</a>
             </c:when>
             <c:otherwise>
                  <a href="${login}" class="link nav-item mx-3">Log In/Sign Up</a>
             </c:otherwise>
            </c:choose>
     </div>
  </nav>
</div>
