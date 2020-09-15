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

$(document).ready(function () {
  var step = 1;
  var ingredient = 1;
  addRemove(
    step,
    ".step-add-button",
    ".step-wrapper",
    ".step-form",
    ".step-remove-button"
  );
  addRemove(
    ingredient,
    ".ingredient-add-button",
    ".ingredient-wrapper",
    ".ingredient-form",
    ".ingredient-remove-button"
  );
});

function addRemove(x, addButton, wrapper, form, removeButton) {
  const MAX_NUMBER_OF_FIELD = 50;
  //Once add button is clicked
  $(addButton).click(function () {
    var inputBox = $(form).clone();
    var fieldHTML = "<div>" + inputBox.html() + "</div>"; //New input field html
    //Check maximum number of input fields
    if (x < MAX_NUMBER_OF_FIELD) {
      x++; //Increment field counter
      $(wrapper).append(fieldHTML); //Add field html
    }
  });

  //Once remove button is clicked
  $(wrapper).on("click", removeButton, function (e) {
    e.preventDefault();
    $(this).parent("div").remove(); //Remove field html
    x--; //Decrement field counter
  });
}
