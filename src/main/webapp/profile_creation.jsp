<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
   String uploadUrl = blobstoreService.createUploadUrl("/profile_creation"); %>

<c:set var = "upload"  value = "<%= uploadUrl %>" />
<t:genericpage>
    <jsp:body>
       <div class="content">
        <h3> User Registration </h3>
        <h5> To complete your user registration please fill the fom below and click submit</h5>
        <br>
        <form action="${upload}" enctype="multipart/form-data" method="POST">
            <div class="form-group">
              <label for="image">Upload Profile image</label>
              <input type="file" class="form-control-file" name="image" required>
           </div>
           <div class="form-group">
              <label for="username">Display Name</label>
              <input type="text" class="form-control" name="username" placeholder="Type your username" required>
           </div>
           <div class="form-group">
              <label for="bio">Your Bio</label>
              <textarea class="form-control" name="bio" rows="3" required></textarea>
           </div>
           <button type="submit" class="btn btn-secondary my-3">submit</button>
        </form>
       </div>
    </jsp:body>
</t:genericpage>