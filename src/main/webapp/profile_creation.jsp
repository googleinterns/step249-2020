<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
       <div class="content">
        <form action="/profile_edit" method="POST">
           <div class="form-group">
              <label for="username">Display Name</label>
              <input type="text" class="form-control" name="username" placeholder="Type your username">
           </div>
           <div class="form-group">
              <label for="bio">Your Bio</label>
              <textarea class="form-control" name="bio" rows="3"></textarea>
           </div>
           <button type="submit" class="btn btn-secondary my-3">submit</button>
        </form>
       </div>
    </jsp:body>
</t:genericpage>