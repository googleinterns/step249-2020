<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
       <div class="content">
        <form action="/profile_edit?id=${id}" method="GET">
           <div class="form-group">
              <label for="username">Edit Display Name</label>
              <input type="text" class="form-control" name="username" placeholder="Type your username" required>
           </div>
           <div class="form-group">
              <label for="bio">Edit Your Bio</label>
              <textarea class="form-control" name="bio" rows="3" required></textarea>
           </div>
           <button type="submit" class="btn btn-secondary my-3">submit</button>
        </form>
       </div>
    </jsp:body>
</t:genericpage>