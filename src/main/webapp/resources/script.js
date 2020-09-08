// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function showIngredientsInput() {
  document.getElementById('ingredients-button').style.display = 'none';
  document.getElementById('ingredients-submit').style.display = 'block';
  document.getElementById('ingredients-input').style.display = 'block';
  document.getElementById('title-search').style.display = 'none';

  if (document.getElementById('searchbar'))
    document.getElementById('searchbar').classList.add('mb-3');

  if (document.getElementById('title-input'))
    document.getElementById('title-input').classList.add('mr-3');
}

function checkIngredientsList() {
  if (document.getElementById('ingredients-input').value) 
    showIngredientsInput();
}
