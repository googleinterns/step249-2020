<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
       <div class="content">
        <c:choose>
        <c:when test="${isLoggedIn == 1}">
        <form action="/profile_edit" method="POST">
           <div class="form-group">
              <label for="username">Edit Display Name</label>
              <input type="text" class="form-control" name="username" placeholder="Type your username" value="${name}" required>
           </div>
           <div class="form-group">
              <label for="bio">Edit Your Bio</label>
              <textarea class="form-control" name="bio" rows="3" required>${bio}</textarea>
           </div>
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