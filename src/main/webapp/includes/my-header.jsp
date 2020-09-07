<div class="header wrapper">
  <nav class="navbar mb-0">
    <a href="/" class="link nav-item mx-3">LOGO</a>
    <form action="search" class="form-line">
          <input type="text" class="form-control searchbar" id="title-input" placeholder="Type recipe title" name="searchterm" value="${titleSearched}"/>
          <button type="submit" class="btn btn-secondary mx-3" id="title-search" value="search">Search</button>
          <button type="button" class="btn btn-secondary mx-3" id="ingredients-button" onclick="showIngredientsInput()">Search by ingredients</button>
          <input type="text" class="form-control searchbar" id="ingredients-input" placeholder="Type ingredients separated by comma" name="ingredients" style="display: none;" value="${ingredientsSearched}"/>
          <button type="submit" class="btn btn-secondary mx-3" id="ingredients-submit" value="search" style="display: none;">Search</button>
    </form>
    <div class="links">
      <a class="link nav-item mx-3">post a recipe</a>
      <a class="link nav-item mx-3">log in/sign up</a>
    </div>
  </nav>
</div>
