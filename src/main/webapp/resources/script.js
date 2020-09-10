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

$(document).ready(function (){
  var maxField = 50; //Input fields increment limitation
  var addButton = $('.add-button'); //Add button selector
  var x = 1; //Initial field counter is 1
  var wrapper = $('.step-wrapper'); //Input field wrapper
            
 //Once add button is clicked
  $(addButton).click(function(){
  var inputBox = $(".step-form" ).clone(); //clone the input box to be added
  var fieldHTML ='<div class="d-inline">'+inputBox.html()+'<a href="javascript:void(0);" class="remove-button">REMOVE</a></div>'; //New input field html 
  //Check maximum number of input fields
  if(x < maxField){ 
       x++; //Increment field counter
       $(wrapper).append(fieldHTML); //Add field html
  }
  });
    
 //Once remove button is clicked
$(wrapper).on('click', '.remove-button', function(e){
   e.preventDefault();
   $(this).parent('div').remove(); //Remove field html
   x--; //Decrement field counter
  });
});