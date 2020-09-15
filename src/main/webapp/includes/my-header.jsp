<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

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
           <a class="link nav-item mx-3">Post a recipe</a>
            <c:choose>
             <c:when test="${isLoggedIn == 1}">
                  <a href="/user?id=${id}" class="link nav-item mx-3">my profile</a>
                  <a href="${logoutURL}" class="link nav-item mx-3">log out</a>
             </c:when>
             <c:otherwise>
                  <a href="_ah/login?continue=%2Flogin" class="link nav-item mx-3">Log in/Sign up</a>
             </c:otherwise>
            </c:choose>
     </div>
  </nav>
</div>
