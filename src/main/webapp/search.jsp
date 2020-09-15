<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:genericpage>
    <jsp:body>
      <div class="content">
        <c:choose>
            <c:when test="${recipesList.size() > 0}">
                <ul id="results" class="list-unstyled">
                    <c:forEach items="${recipesList}" var="recipe">
                        <li class="my-4">
                            <a class="media search-result p-1" href="recipe?id=${recipe.getId()}">
                                <div class="img-thumbnail mr-3 recipe-thumbnail" style="background-image: url('${recipe.getImage()}');">
                                </div>
                                <div class="media-body">
                                    <h5 class="mt-0"><c:out value="${recipe.getName()}" /></h5>
                                    <p> 
                                        <b>Time</b>:
                                        <c:out value="${recipe.getPrepTime()}" />, 
                                        <b>Author</b>:
                                        <c:out value="${recipe.getAuthor()}" />, 
                                        <b>Difficulty</b>:
                                        <c:out value="${recipe.getDifficulty()}"/>  
                                    </p>
                                    <div class="search-description">
                                        <c:out value="${recipe.getDescription()}" /> 
                                    </div>
                                </div>
                            </a>
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