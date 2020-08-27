<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:homepage>
    <jsp:body>
      <div class="home-page content">
        <h1 class="welcome">Welcome to Piece of Cake!</h1>
        <form class="searchbar" method="GET" action="search">
             <input type="text" class="form-control searchbar" placeholder="Type recipe title" name="searchterm">
             <div class="btn-line">
                 <button type="submit" class="btn btn-secondary my-3" value="search">search</button>
                 <a href="/recipe?id=0" class="btn btn-secondary my-3">random recipe</a>
             </div>
        </form>
      </div>
    </jsp:body>
</t:homepage>