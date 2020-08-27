<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
    <jsp:body>
      <div class="content">
        <h1>${title}</h1>
        <img src=${imgURL} class="img-fluid my-3" alt="Recipe Image">
        <ul class="list-group list-group-horizontal w-100">
             <li class="list-group-item">Prep Time:   </li>
             <li class="list-group-item">Cooking Time:   </li>
             <li class="list-group-item">Difficulty:     </li>
        </ul>
        <br>
        <ul class="list-group w-25" >
            <li class="list-group-item">${ingredient}</li>
            <li class="list-group-item"> Ingredint 2</li>
            <li class="list-group-item"> Ingredint 3</li>
        </ul>
        <ul class="steps list-group list-group-flush" >
            <li class="list-group-item">
                <p><b>STEP 1:</b> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
            </li>
            <li class="list-group-item">
                <p><b>STEP 2:</b> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
            </li>
            <li class="list-group-item">
                <p><b>STEP 3:</b> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
            </li>
        </ul>
      </div>
    </jsp:body>
</t:genericpage>