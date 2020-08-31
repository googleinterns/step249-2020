<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="header wrapper">
  <nav class="navbar justify-content-end">
    <div class="links">
      <a class="link nav-item mx-3">post a recipe</a>
      <c:choose>
        <c:when test="${isLogIn == 0}">
            <a href="${loginURL}" class="link nav-item mx-3">log in/sign up</a>
        </c:when>
       <c:otherwise>
            <a class="link nav-item mx-3">welcome ${userEmail}</a>
            <a href="${logoutURL}" class="link nav-item mx-3">log out</a>
       </c:otherwise>
      </c:choose>
    </div>
  </nav>
</div>
