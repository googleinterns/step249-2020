<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:genericpage>
    <jsp:body>
      <div class="content">
          <script type="text/javascript">
            checkIngredientsList();
          </script>
          <ul id="results" class="list-group list-group-flush">
                    <c:forEach items="${names}" var="recipe">
                        <li class="list-group-item">
                            <h1>${recipe}</h1>
                        </li>
                    </c:forEach>
                </ul>
        <c:choose>
            <c:when test="${recipesList.size() > 0}">
                <ul id="results" class="list-group list-group-flush">
                    <c:forEach items="${recipesList}" var="recipe">
                        <li class="list-group-item">
                            <img src="${recipe.getImage()}" class="img-thumbnail my-3 mr-3 float-left" width="200" height="200" alt="${recipe.getName()}">
                                <h3><a href="recipe?id=${recipe.getId()}"> <c:out value="${recipe.getName()}" /> </a></h3>
                                <p>
                                <c:out value="${recipe.getDescription()}" /> 
                                </p>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>    
            <c:otherwise>
                <h1 id="welcome-message">Right now we don't have any recipes matching your request. Stay close!</h1>
            </c:otherwise>
        </c:choose>
      </div>
    </jsp:body>
</t:genericpage>