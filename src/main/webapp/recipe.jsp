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
            <h1>${title}</h1>
            <a href="/user?id=${author_id}"><h3>by ${author}</h3></a>
            <img src=${imgURL} class="img-fluid my-3" alt="${title}">
            <ul class="list-group list-group-horizontal w-100 mb-3">
                <li class="list-group-item">Prep Time: ${prepTime}</li>
                <li class="list-group-item">Difficulty: ${difficulty}</li>
            </ul>

            <div class="container p-0">
                <div class="row">
                    <div class="col-4">
                        <ul class="list-group" >
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