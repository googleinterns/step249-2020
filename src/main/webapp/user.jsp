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
                    <c:choose>
                        <c:when test="${user.getImage() != null}">
                             <div class="img-thumbnail rounded-circle mr-3 profile-thumbnail" style="background-image: url('${user.getImage()}');"></div>
                        </c:when>    
                        <c:otherwise>
                             <div class="img-thumbnail rounded-circle mr-3 profile-thumbnail" style="background-image: url('/images/default.png');"></div>
                        </c:otherwise> 
                    </c:choose>              
                    </div>
                    <div class="col-8">
                        <div class="row">
                            <h1 class="col-11">${user.getName()}</h1>
                            <a href="/profile_creation.jsp" style="display:${edit}" class="btn btn-secondary my-3 col-1" >edit</a>
                        </div>
                        <h6>${user.getBio()}</h6>
                    </div>
                </div>
            </div>
            <hr />
            <h3>List of recipes posted by the user:</h3>
            <ul id="results" class="list-unstyled">
                <c:forEach items="${recipesList}" var="recipe">
                    <li class="my-4">
                        <a class="media search-result p-1 rounded" href="recipe?id=${recipe.getId()}">
                            <div class="img-thumbnail rounded mr-3 recipe-thumbnail" style="background-image: url('${recipe.getImage()}');">
                            </div>
                            <div class="media-body">
                                <h5 class="mt-0"><c:out value="${recipe.getName()}" /></h5>
                                <p>
                                    <b>Time</b>:
                                    <c:out value="${recipe.getPrepTime()}" /> min, 
                                    <b>Difficulty</b>:
                                    <c:out value="${recipe.getDifficulty()}"/>  
                                </p>
                                <c:choose>
                                    <c:when test="${recipe.getMatchingIngredients().size() > 0}">
                                        <ul id="results" class="list-group list-group-flush">
                                            <c:forEach items="${recipe.getMatchingIngredients()}" var="ingredient">
                                                <li class="list-group-item">
                                                    ${ingredient}
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${recipe.getDescription()}" /> 
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </a>
                    </li>
                </c:forEach>
            </ul>
      </div>
    </jsp:body>
</t:genericpage>
