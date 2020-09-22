<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
      <div class="content">
      <c:choose>
         <c:when test="${error == 1}">
              <h1>Sorry! </br>
              No recipe matching the input id</h1>
         </c:when>    
       <c:otherwise>
            <h1>${title}
                  <c:if test="${author_id == id}"> 
                      <a href="/recipe_post?id=${recipe_id}" style="float:right;" class="btn btn-secondary my-3 col-1" >edit</a>
                  </c:if>
            </h1>
            <a href="/user?id=${author_id}"><h3>by ${author}</h3></a>
            <div class="img-thumbnail rounded mb-3 recipe-img" style="background-image: url('${imgURL}');"></div>
            <ul class="list-group list-group-horizontal w-100 mb-3">
                <c:if test="${hour == 4}">
                    <li class="list-group-item">Prep Time: more than 3 hours</li>
                </c:if>
                <c:if test="${hour < 4}">
                    <li class="list-group-item">Prep Time: ${hour} h and ${min} minutes</li>
                </c:if>
                <li class="list-group-item">Difficulty: ${difficulty}</li>
            </ul>

            <div class="recipe-details">
                <div class="row">
                    <div class="col-4">
                        <ul class="list-group" >
                             <li class="list-group-item">Ingredients:</li>
                        <c:forEach items="${ingredients}" var="ingr">
                            <li class="list-group-item"><c:out value="${ingr}" /></li>
                        </c:forEach>  
                        </ul>
                    </div>
                    <div class="col-8">
                        <ul class="steps list-group list-group-flush" >
                            <c:set var="i" value="1"/>
                            <c:forEach items="${steps}" var="step">
                            <li class="list-group-item">
                                <p><b>STEP <c:out value="${i}"/>: </b><c:out value="${step}" /> </p>
                                <c:set var="i" value="${i + 1}"/>
                            </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
       </c:otherwise> 
      </c:choose>
      </div>
    </jsp:body>
</t:genericpage>