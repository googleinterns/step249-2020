<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
       <div class="content">
        <c:choose>
        <c:when test="${isLoggedIn == 1}">
            <h3>Edit Your Profile</h3>
            <h5 class="mb-3">To edit your user profile please fill the form below</h5>
            <c:set var="user_name" value="${name}"/>
            <c:set var="user_bio" value="${bio}"/>
        </c:when>
        <c:otherwise>
            <h3>User Registration</h3>
            <h5 class="mb-3">To complete your user registration please fill the fom below and click submit</h5>
            <c:set var="user_name" value="type your username"/>
            <c:set var="user_bio" value=""/>
        </c:otherwise>
        </c:choose> 
        <form action="/profile_creation" method="POST">
           <div class="form-group">
              <label for="username">Display Name</label>
              <input type="text" class="form-control" name="username" placeholder="${user_name}" required>
           </div>
           <div class="form-group">
              <label for="bio">Your Bio</label>
              <textarea class="form-control" name="bio" rows="3" required>${user_bio}</textarea>
           </div>
           <button type="submit" class="btn btn-secondary my-3">submit</button>
        </form>
       </div>
    </jsp:body>
</t:genericpage>