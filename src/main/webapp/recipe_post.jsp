<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
       <div class="content">
        <c:choose>
        <c:when test="${isLoggedIn == 1}">
            <h2>Post your own recipe!</h2>
            <form action="/recipe_post" method="POST">
             <div class="form-group">
                <label for="title">Recipe Title</label>
                <input type="text" class="form-control" name="title" placeholder="Type your recipe title" maxlength="50" required>
             </div>
             <div class="form-group">
                <label for="description">Add a short description for your recipe</label>
                <textarea class="form-control" name="description" rows="3" required></textarea>
             </div>
             <fieldset class="form-group">
                <label>Select the difficulty</label>
                         <div class="form-check">
                             <label class="radio-inline" for="easy">
                             <input class="form-check-input" type="radio" name="difficulty" id="easy" value="easy" required>
                             Easy</label>
                         </div>
                         <div class="form-check">
                             <label class="radio-inline" for="medium">
                             <input class="form-check-input" type="radio" name="difficulty" id="medium" value="medium" required>
                             Medium</label>
                         </div>
                         <div class="form-check">
                             <label class="radio-inline" for="hard">
                             <input class="form-check-input" type="radio" name="difficulty" id="hard" value="hard" required>
                             Hard</label>
                         </div>
             </fieldset>
             <button type="submit" class="btn btn-secondary my-3">submit</button>
            </form>
        </c:when>
        <c:otherwise>
            <h2>You have to be logged in to acess this page!</h2>
        </c:otherwise>
      </c:choose>
       </div>
    </jsp:body>
</t:genericpage>