<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:homepage>
    <jsp:body>
      <div id="home-page" class="content">
        <h1 id="welcome">Welcome to Piece of Cake!</h1>
        <form id="searchbar" method="GET" action="search">
             <input type="text" class="form-control" id="searchbar" placeholder="Type recipe title" name="searchterm">
             <div class="buttonline">
                 <button type="submit" class="btn btn-secondary my-3" value="search">search</button>
                 <a href="/recipe?id=0" class="btn btn-secondary my-3">random recipe</a>
             </div>
        </form>
      </div>
    </jsp:body>
</t:homepage>