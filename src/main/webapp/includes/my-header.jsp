<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<div class="header wrapper">
  <nav class="navbar mb-0">
    <a href="/" class="link nav-item mx-3">LOGO</a>
    <form action="search" class="form-line">
          <input type="text" class="form-control searchbar" placeholder="Type the recipe title or ingredients separated by comma" name="searchterm" value="${dataSearched}"/>
          <input type="hidden" name="difficulty" value=""/>
          <input type="hidden" name="time" value=""/>
          <button type="submit" class="btn btn-secondary mx-3" value="search">Search</button>
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
