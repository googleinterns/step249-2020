<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
   String uploadUrl = blobstoreService.createUploadUrl("/profile_edit"); %>

<c:set var = "upload"  value = "<%= uploadUrl %>" />
<t:genericpage>
    <jsp:body>
       <div class="content">
        <c:choose>
        <c:when test="${isLoggedIn == 1}">
        <form action="${upload}" enctype="multipart/form-data" method="POST">
            <div class="form-group">
              <label for="image">Upload Profile image</label>
              <input type="file" class="form-control-file" name="image">
           </div>
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