<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
   String uploadUrl = blobstoreService.createUploadUrl("/recipe_post"); %>

<c:set var = "upload"  value = "<%= uploadUrl %>" />

<t:genericpage>
    <jsp:body>
       <div class="content">
        <c:choose>
        <c:when test="${edit != 1}">
            <c:set var="title_p" value="Type your recipe title"/>
            <c:set var="descriprion_p" value="Add a short description for your recipe"/>
            <c:set var="img" value="required"/>
        </c:when>
        </c:choose>
        <c:choose>
        <c:when test="${isLoggedIn == 1}">
            <h2>Post your own recipe!</h2>
            <form action="${upload}" enctype="multipart/form-data" method="POST">
             <div class="form-group">
                <label for="image">Upload Main Recipe Image</label>
                <input type="file" class="form-control-file" name="image" ${img}>
             </div>
             <div class="form-group">
                <label for="title">Recipe Title</label>
                <input type="text" class="form-control" name="title" placeholder="${title_p}" value="${title}" maxlength="50" required>
             </div>
             <div class="form-group">
                <label for="description">Overview</label>
                <textarea class="form-control" name="description" rows="3" placeholder="${description_p}" maxlength="500" required>${description}</textarea>
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
             <div class="form-group">
                 <label>Select the prearation time for your recipe</label>
                 <div class="form-row">
                      <label class="form-inline" for="hour">
                           <select class="form-control m-3" id="hour" name="hour">
                             <option value="1">1</option>
                             <option value="2">2</option>
                             <option value="3">3</option>
                             <option value="4">more than 3</option>
                           </select>hour
                      </label>
                      <label class="form-inline" for="min">
                           <select class="form-control m-3" id="min" name="min">
                             <option value="15">15</option>
                             <option value="30">30</option>
                             <option value="45">45</option>
                           </select>minutes
                      </label>
                 </div>
             </div>
             <div class="form-group">
                <label>Ingredients</label>
                <div class="ingredients-wrapper">
                <div class="ingredient-form">
                 <div class="form-row">
                  <div class="col">
                      <input type="number" class="form-control" name="ingredients[][quantity]" placeholder="quantity" required>
                  </div>
                  <div class="col">
                      <input type="text" class="form-control" name="ingredients[][measure]" maxlength="50" placeholder="unit of measurment" >
                  </div>
                  <div class="col">
                      <input type="text" class="form-control" name="ingredients[][ingredient]" placeholder="ingredient" maxlength="50" required>
                  </div>
                 </div>
                 <a href="javascript:void(0);" class="ingredient-remove-button">REMOVE</a></div>
                </div>
                <a href="javascript:void(0);" class="ingredient-add-button" title="add-step">ADD INGREDIENT</a>
                </div>
             <div class="form-group">
                <label for="step">Steps</label>
                <div class="steps-wrapper">
                  <c:forEach items="${steps}" var="step">
                      <div class ="step-form">
                        <textarea class="form-control" name="step[]" rows="3" maxlength="500" required>${step}</textarea>
                        <a href="javascript:void(0);" class="step-remove-button">REMOVE</a>
                        </div>
                  </c:forEach>
                  <div class="step-form">
                   <textarea class="form-control" name="step[]" rows="3" maxlength="500" required></textarea>
                   <a href="javascript:void(0);" class="step-remove-button">REMOVE</a>
                  </div>
                  <div class="step-clone" style="display:none">
                   <textarea class="form-control" name="step[]" rows="3" maxlength="500" required></textarea>
                   <a href="javascript:void(0);" class="step-remove-button">REMOVE</a>
                  </div>
                </div>
                <a href="javascript:void(0);" class="step-add-button" title="add-step">ADD STEP</a>
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