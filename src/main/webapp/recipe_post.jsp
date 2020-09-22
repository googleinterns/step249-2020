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
        <c:when test="${edit}">
            <c:set var="welcome_message" value="Edit your recipe!"/>
        </c:when>
        <c:otherwise>
            <c:set var="welcome_message" value="Post your own recipe!"/>
            <c:set var="descriprion_placeholder" value="Add a short description for your recipe"/>
            <c:set var="img" value="required"/>
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${isLoggedIn == 1}">
            <h2>${welcome_message}</h2>
            <p>Thank you for your interest in contributing to our recipe database. Please fill in the form below to describe the ingredients and steps toward the tasteful end result.</p>

            <h4 class="mt-5">Recipe information</h4>
            <form action="${upload}" enctype="multipart/form-data" method="POST">
            <c:if test="${edit}">
                <input type="hidden" name="edited" value="true"/>
                <input type="hidden" name="recipeId" value="${recipeId}"/>
            </c:if>
             <div class="form-group">
                <label for="image">Pick the image that best shows the end result of your recipe.</label>
                <input type="file" class="form-control-file" name="image" ${img}>
             </div>
             <div class="form-group">
                <label for="title">Recipe Title</label>
                <input type="text" class="form-control" name="title" value="${title}" maxlength="50" required>
             </div>
             <div class="form-group">
                <label for="description">Overview</label>
                <textarea class="form-control" name="description" rows="3" placeholder="An overview of what the recipe is about" maxlength="500" required>${description}</textarea>
             </div>
             <fieldset class="form-group">
                <label>Select the difficulty</label>
                <div class="form-check">
                    <label class="radio-inline" for="easy">
                    <input class="form-check-input" type="radio" name="difficulty" id="easy" value="easy" ${easyChecked} required>
                    Easy</label>
                </div>
                <div class="form-check">
                    <label class="radio-inline" for="medium">
                    <input class="form-check-input" type="radio" name="difficulty" id="medium" value="medium" ${mediumChecked} required>
                    Medium</label>
                </div>
                <div class="form-check">
                    <label class="radio-inline" for="hard">
                    <input class="form-check-input" type="radio" name="difficulty" id="hard" value="hard" ${hardChecked} required>
                    Hard</label>
                </div>
             </fieldset>
             <div class="form-group">
                 <label>Preparation time (in minutes)</label>
                 <div class="form-row">
                    <label class="form-inline" for="time">
                        <input type="number" class="form-control mr-1" name="time" min="15" step="5" value="${time}" required> 
                        minutes
                    </label>
                 </div>
             </div>

             <div class="form-group my-5">
                <h4>Ingredients</h4>
                <p>Add the ingredients that are needed to make your delicious recipe.</p>
                <ul class="list-group">
                    <div class="ingredients-wrapper">
                        <c:choose>
                        <c:when test="${edit}">
                            <c:forEach items="${ingredients}" var="ingredient">
                                <li class="list-group-item">
                                    <div class ="ingredient-form row">
                                        <div class="col-6">
                                            <input type="text" class="form-control" name="ingredients[]" maxlength="50" value="${ingredient}" required>
                                        </div>
                                        <button type="button" class="close ingredient-remove-button" aria-label="Close">&times;</button>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <li class="list-group-item">
                                <div class ="ingredient-form row">
                                    <div class="col-6">
                                    <input type="text" class="form-control" name="ingredients[]" placeholder="Example: 4 cups of sugar, 2 onions, ... " maxlength="50" required>
                                    </div>
                                </div>
                            </li>
                        </c:otherwise>
                        </c:choose>
                    </div>
                    <li class="list-group-item">
                        <button type="button" class="btn btn-secondary btn-sm ingredient-add-button">+ Add another ingredient</button>
                    </li>
                </ul>
            </div>
             <div class="form-group my-5">
                <h4>Steps</h4>
                <p>Describe each step of the recipe.</p>
                <ul class="list-group">
                    <div class="steps-wrapper">
                        <c:choose>
                        <c:when test="${edit}">
                            <c:forEach items="${steps}" var="step">
                                <li class="list-group-item">
                                    <div class ="step-form">
                                        <textarea class="form-control" name="step[]" rows="3" maxlength="500" required>${step}</textarea>
                                        <button type="button" class="btn btn-sm step-remove-button" aria-label="Close">Remove step</button>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <li class="list-group-item">
                                <div class ="step-form">
                                    <textarea class="form-control" name="step[]" rows="3" maxlength="500" required></textarea>
                                </div>
                            </li>
                        </c:otherwise>
                        </c:choose>
                    </div>
                    <li class="list-group-item">
                        <button type="button" class="btn btn-secondary btn-sm step-add-button">+ Add another step</button>
                    </li>
                </ul>
             </div>

            <button type="submit" class="btn btn-lg btn-primary my-3">Post your recipe!</button>
            </form>

            <!-- Step template element. -->
            <div class="step-clone" style="display:none">
                <li class="list-group-item">
                    <div class ="step-form">
                        <textarea class="form-control" name="step[]" rows="3" maxlength="500" ></textarea>
                        <button type="button" class="btn btn-sm step-remove-button" aria-label="Close">Remove step</button>
                    </div>
                </li>
            </div>

            <!-- Ingredient template element. -->
            <div class="ingredient-clone" style="display:none">
                <li class="list-group-item">
                    <div class ="ingredient-form row">
                        <div class="col-6">
                        <input type="text" class="form-control" name="ingredients[]" placeholder="Example: 4 cups of sugar, 2 onions, ... " maxlength="50" required>
                        </div>
                        <button type="button" class="close ingredient-remove-button" aria-label="Close">&times;</button>
                    </div>
                </li>
            </div>

        </c:when>
        <c:otherwise>
            <h2>You have to be logged in to acess this page!</h2>
        </c:otherwise>
      </c:choose>
       </div>
    </jsp:body>
</t:genericpage>