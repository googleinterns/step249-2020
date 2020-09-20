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
    "step-form",
    ".step-add-button",
    ".steps-wrapper",
    ".step-clone",
    ".step-remove-button"
  );
  addRemove(
    ingredient,
    "ingredient-form",
    ".ingredient-add-button",
    ".ingredients-wrapper",
    ".ingredient-clone",
    ".ingredient-remove-button"
  );
});

function addRemove(numberOfForm, formName, addButton, wrapper, form, removeButton) {
  const MAX_NUMBER_OF_FIELDS = 50;
  //Once add button is clicked
  $(addButton).click(function () {
    var inputBox = $(form).clone();
    var fieldHTML = "<div class="+formName+">" + inputBox.html() + "</div>"; //New input field html
    //Check maximum number of input fields
    if (numberOfForm < MAX_NUMBER_OF_FIELDS) {
      numberOfForm++; //Increment field counter
      $(wrapper).append(fieldHTML); //Add field html
    }
  });

  //Once remove button is clicked
  $(wrapper).on("click", removeButton, function (e) {
    e.preventDefault();
    $(this).parent("div").remove(); //Remove field html
    numberOfForm--; //Decrement field counter
  });
}