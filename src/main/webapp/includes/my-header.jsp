<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<div class="header wrapper">
  <nav class="navbar mb-0">
    <a href="/" class="link nav-item mx-3">LOGO</a>
    <form action="search" class="form-line">
          <input type="text" class="form-control searchbar" id="title-input" placeholder="Type recipe title" name="searchterm" value="${titleSearched}"/>
          <button type="submit" class="btn btn-secondary mx-3" id="title-search" value="search">Search</button>
          <button type="button" class="btn btn-secondary mx-3" id="ingredients-button" onclick="showIngredientsInput()">Search by ingredients</button>
          <input type="text" class="form-control searchbar" id="ingredients-input" placeholder="Type the ingredients separated by comma" name="ingredients" style="display: none;" value="${ingredientsSearched}"/>
          <button type="submit" class="btn btn-secondary mx-3" id="ingredients-submit" value="search" style="display: none;">Search</button>
    </form>
    <div class="links">
           <a class="link nav-item mx-3">post a recipe</a>
            <c:choose>
             <c:when test="${isLoggedIn == 1}">
                  <a class="link nav-item mx-3">welcome ${username}</a>
                  <a href="${logoutURL}" class="link nav-item mx-3">log out</a>
             </c:when>
             <c:otherwise>
                  <a href="_ah/login?continue=%2Flogin" class="link nav-item mx-3">log in/sign up</a>
             </c:otherwise>
            </c:choose>
     </div>
  </nav>
</div>
