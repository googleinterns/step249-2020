<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:genericpage>
    <jsp:body>
      <div class="content">
          <script type="text/javascript">
            checkIngredientsList();
          </script>
        <div class="row">
            <div class="col-md-2">
                <form action="search">
                    <input type="hidden" placeholder="Type recipe title" name="searchterm" value="${dataSearched}"/>
                    <select class="custom-select custom-select-sm mb-3" name="difficulty">
                        <c:choose>
                            <c:when test="${not empty difficultySearchedKey}">    
                                <option value="<c:out value="${difficultySearchedKey}"/>" selected hidden><c:out value="${difficultySearchedValue}"/></option>
                            </c:when>
                            <c:otherwise>
                                <option selected disabled hidden value=" ">Difficulty</option>
                            </c:otherwise>  
                        </c:choose>
                        <c:forEach items="${difficultyList}" var="difficulty">
                            <option value="${difficulty.key}">${difficulty.value}</option>
                        </c:forEach>
                    </select>
                    <select class="custom-select custom-select-sm mb-3" name="time">
                        <c:choose>
                            <c:when test="${not empty timeSearchedKey}">    
                                <option value="<c:out value="${timeSearchedKey}"/>" selected hidden><c:out value="${timeSearchedValue}"/></option>
                            </c:when>
                            <c:otherwise>
                                <option selected disabled hidden value=" ">Cooking Time</option>
                            </c:otherwise>  
                        </c:choose>
                        <c:forEach items="${cookingTimeList}" var="time">
                            <option value="${time.key}">${time.value}</option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-secondary mx-3" value="search">Filter the results</button>
                </form>
            </div>
            <div class="col-md-10">
                <c:choose>
                    <c:when test="${recipesList.size() > 0}">
                        <ul id="results" class="list-group list-group-flush">
                            <c:forEach items="${recipesList}" var="recipe">
                                <li class="list-group-item">
                                    <img src="${recipe.getImage()}" class="img-thumbnail my-3 mr-3 float-left" width="200" height="200" alt="${recipe.getName()}">
                                        <h3><a href="recipe?id=${recipe.getId()}"> <c:out value="${recipe.getName()}" /> </a></h3>
                                        <p>
                                        <c:choose>
                                            <c:when test="${recipe.getIngredientsMatched().size() > 0}">
                                                <ul id="results" class="list-group list-group-flush">
                                                    <c:forEach items="${recipe.getIngredientsMatched()}" var="ingredient">
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
        </div>
      </div>
    </jsp:body>
</t:genericpage>