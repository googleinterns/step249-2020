<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:homepage>
    <jsp:body>
      <div class="home-page content">
        <img src="/images/logo.png" class="main-logo mb-2" alt="Piece of Cake logo" />
        <h1 class="welcome mb-4">Welcome to Piece of Cake!</h1>
        <form class="searchbar" method="GET" action="search">
             <input type="text" class="form-control searchbar" placeholder="Recipe title or ingredients" name="searchterm">
             <input type="hidden" name="difficulty" value=""/>
             <input type="hidden" name="time" value=""/>
             <div class="btn-line">
                <button type="submit" class="btn btn-secondary my-3 " value="search">Search</button>
                <a href="/random" class="btn btn-secondary my-3">I'm Feeling Hungry</a>
             </div>
        </form>
      </div>
    </jsp:body>
</t:homepage>
