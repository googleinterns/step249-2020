Welcome to Piece of Cake!
-------------------------
We are Beatrice and Paul, two Google STEP Interns (Summer 2020) in the Plx Alerts team.

In the second half of our internship we worked on a full stack web development project. 

We designed and implemented "Piece Of Cake" a platform where everyone, regardless of their tech abilities can post and share their favourite recipes. We believe that information about how to cook good and healthy food should be accessible to as wide of an audience as possible that is why we have decided to make searching for/accessing recipes available to all users of the website regardless of whether they have an account or not.

The website consists of the following pages: <br>
* Home/Search, for the users to search for recipes; <br>
* Results list,  for displaying the recipes matching the search parameters; <br>
* Recipe details, for displaying all the details about the selected recipe; <br>
* User profile creation/edit; <br>
* User profile display, for other users to check the profile of a certain user and the recipes that user published; <br>
* Recipe creation/edit, for the registered users to upload a recipe and to post it; <br>
* About/Terms/How search works pages (in the footer). <br>

How to run Piece of Cake
----------

Make sure to have MAVEN installed. To run the project on your local instance do:  **maven package appengine:run**

If you are running this on a Google Cloud Shell instance the project will work out of the box, otherwise it may need some tweaking (The APIs shoud in theory be accessible externally but we had some problem with Blobstore when running the project not on GCS).

The project can be depoyed from a GCS instance by doing: **maven package appengine:deploy**

It's a piece of cake :)


Searching for a recipe demo
--------------------

![alt-text](demo/demo_user.gif)

Posting a recipe demo
-----------------------

![alt-text](demo/demo_creator.gif)
