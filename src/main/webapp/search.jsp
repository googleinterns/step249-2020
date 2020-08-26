<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
      <div class="content">
        <ul class="list-group list-group-flush">
            <c:forEach items="${recipesList}" var="recipe">
                <li class="list-group-item">
                    <img src="images/default.png" class="img-thumbnail my-3 mr-3 float-left" style="width: 200px; height: 200px" alt="Recipe Image">
                        <h3><a href="recipe?id=${recipe.getId()}"> <c:out value="${recipe.getName()}" /> </a></h3>
                        <p>
                         <c:out value="${recipe.getDescription()}" /> 
                        </p>
                </li>
            </c:forEach>
        </ul>
      </div>
    </jsp:body>
</t:genericpage>