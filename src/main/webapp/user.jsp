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
       <c:otherwise>
            <div class="container">
                <div class="row">
                    <div class="col">
                        <img src="${user.getImage()}" class="img-circle"  width="200" height="200" alt="${user.getName()}">
                    </div>
                    <div class="col-8">
                        <h1>${user.getName()}</h1>
                        <h4>${user.getBio()}</h4>
                    </div>
                </div>
            </div>
            <h2>List of recipes posted by the user:</h2>
                <ul id="results" class="list-group list-group-flush">
                    <c:forEach items="${recipesList}" var="recipe">
                        <li class="list-group-item">
                            <img src="${recipe.getImage()}" class="img-thumbnail my-3 mr-3 float-left" width="200" height="200" alt="Recipe Image">
                                <h3><a href="recipe?id=${recipe.getId()}"> <c:out value="${recipe.getName()}" /> </a></h3>
                                <p>
                                <c:out value="${recipe.getDescription()}" /> 
                                </p>
                        </li>
                    </c:forEach>
                </ul>
            
       </c:otherwise> 
      </c:choose>
      </div>
    </jsp:body>
</t:genericpage>