<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:homepage>
    <jsp:body>
      <div class="home-page content">
        <h1 class="welcome">Welcome to Piece of Cake!</h1>
        <form class="searchbar" method="GET" action="search">
             <input type="text" class="form-control searchbar" id="searchbar" placeholder="Type recipe title" name="searchterm">
             <input type="text" class="form-control searchbar" id="ingredients-input" placeholder="Type ingredients separated by comma" name="ingredients" style="display: none;">
             <div class="btn-line">
                <button type="submit" class="btn btn-secondary my-3 " id="ingredients-submit" value="search" style="display: none;">Search</button>
                <button type="submit" class="btn btn-secondary my-3" id="title-search" value="search">Search by title</button>
                <button type="button" class="btn btn-secondary my-3" id="ingredients-button" onclick="showIngredientsInput()">Search by ingredients</button>
                <a href="/random" class="btn btn-secondary my-3">Random Recipe</a>
             </div>
        </form>
      </div>
    </jsp:body>
</t:homepage>