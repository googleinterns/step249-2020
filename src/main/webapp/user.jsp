<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
      <div class="content">
      <c:choose>
         <c:when test="${error == 1}">
              <h1>Sorry! </br>
              No user matching the input id.</h1>
         </c:when>   
         <c:when test="${user.getId() == id}">
              <c:set var="edit" value=""/>
         </c:when>    
         <c:otherwise>
               <c:set var="edit" value="none"/>
         </c:otherwise> 
       </c:choose>
            <div class="container">
                <div class="row">
                    <div class="col">
                        <img src="${user.getImage()}" class="img-circle"  width="200" height="200" alt="${user.getName()}">
                    </div>
                    <div class="col-8">
                        <div class="row">
                            <h1>${user.getName()}</h1>
                            <a href="/profile_creation.jsp" style="display:${edit}" class="btn btn-secondary my-3" >edit</a>
                        </div>
                        <h4>${user.getBio()}</h4>
                    </div>
                </div>
            </div>
            <h2>List of recipes posted by the user:</h2>
                <ul id="results" class="list-group list-group-flush">
                    <c:forEach items="${recipesList}" var="recipe">
                        <li class="list-group-item">
                            <img src="${recipe.getImage()}" class="img-thumbnail my-3 mr-3 float-left" width="200" height="200" alt="${recipe.getName()}">
                                <div class="row">
                                  <h3><a href="recipe?id=${recipe.getId()}"> <c:out value="${recipe.getName()}" /> </a></h3>
                                  <a href="/recipe_post?id=${recipe.getId()}" style="display:${edit}" class="btn btn-secondary my-3"  display="${edit}">edit</a>
                                </div>
                                <p>
                                <c:out value="${recipe.getDescription()}" /> 
                                </p>
                        </li>
                    </c:forEach>
                </ul>
      </div>
    </jsp:body>
</t:genericpage>