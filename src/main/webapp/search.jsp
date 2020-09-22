<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:genericpage>
    <jsp:body>
      <div class="content">
        <div class="row">
            <div class="col-md-2">
                <form action="search">
                    <input type="hidden" placeholder="Type recipe title" name="searchterm" value="${searchterm}"/>
                    <select class="custom-select custom-select-sm mb-3" name="difficulty">
                        <option ${empty difficulty ? 'selected' : ''} disabled hidden value=" ">Difficulty</option>
                        <option ${difficulty == "easy" ? 'selected' : ''} value="easy">Easy</option>
                        <option ${difficulty == "medium" ? 'selected' : ''} value="medium">Medium</option>
                        <option ${difficulty == "hard" ? 'selected' : ''} value="hard">Hard</option>
                    </select>
                    <select class="custom-select custom-select-sm mb-3" name="time">
                        <option ${empty prepTime ? 'selected' : ''} disabled hidden value=" ">Time</option>
                        <option ${prepTime == "30" ? 'selected' : ''} value="30">Less than 30 mins</option>
                        <option ${prepTime == "60" ? 'selected' : ''} value="60">Less than 1h</option>
                        <option ${prepTime == "120" ? 'selected' : ''} value="120">Less than 2h</option>
                    </select>
                    <button type="submit" class="btn btn-secondary mx-3" value="search">Filter the results</button>
                </form>
            </div>
            <div class="col-md-10">
                <c:choose>
                    <c:when test="${recipesList.size() > 0}">
                        <ul class="list-unstyled search-results">
                            <c:forEach items="${recipesList}" var="recipe">
                                <li class="my-4">
                                    <a class="media rounded search-result p-1" href="recipe?id=${recipe.getId()}">
                                        <div class="rounded img-thumbnail mr-3 recipe-thumbnail" style="background-image: url('${recipe.getImage()}');">
                                        </div>
                                        <div class="media-body">
                                            <h5 class="mt-0"><c:out value="${recipe.getName()}" /></h5>
                                            <p>
                                                <b>Time</b>:
                                                <c:out value="${recipe.getPrepTime()}" /> min, 
                                                <b>Author</b>:
                                                <c:out value="${recipe.getAuthor()}" />, 
                                                <b>Difficulty</b>:
                                                <c:out value="${recipe.getDifficulty()}"/>  
                                            </p>
                                            <c:choose>
                                                <c:when test="${recipe.getMatchingIngredients().size() > 0}">
                                                    <ul class="list-group list-group-flush matching-ingredients-results">
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
                    </c:when>    
                    <c:otherwise>
                        <h1 id="welcome-message">Right now we don't have any recipes matching your request. Stay close!</h1>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
      </div>
    </jsp:body>
</t:genericpage>