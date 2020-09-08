<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:homepage>
    <jsp:body>
      <div class="home-page content">
        <h1 class="welcome">Welcome to Piece of Cake!</h1>
        <form class="searchbar" method="GET" action="search">
             <input type="text" class="form-control searchbar" placeholder="Type the recipe title or ingredients separated by comma" name="searchterm">
             <div class="btn-line">
                <button type="submit" class="btn btn-secondary my-3 " value="search">Search</button>
                <a href="/random" class="btn btn-secondary my-3">Random Recipe</a>
             </div>
        </form>
      </div>
    </jsp:body>
</t:homepage>