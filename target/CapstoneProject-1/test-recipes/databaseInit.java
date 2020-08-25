import java.io.*; 
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import StringUtils.isBlank;

public class UploadingRecipes{

    public void upload(String  title, String imgURL, ArrayList<String> ingredients, ArrayList<String> stepList) throws Exception {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
        Entity recipeEntity = new Entity("Recipe");

        recipeEntity.setProperty("title", title);
        recipeEntity.setProperty("imgURL", imgURL);
        recipeEntity.setProperty("ingredients", ingredients);
        recipeEntity.setProperty("stepList", stepList);

        datastore.put(recipeEntity);
    }

    public static List<String> readFileInList(String fileName) { 
  
         List<String> lines = Collections.emptyList(); 
         lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
         return lines; 
    } 

    public void parseRecipe(String fileName) throws Exception {
       
       List<String> lines = readFileInList(fileName);

       String title = lines.get(2);
       String imgURL = lines.get(4);
       Int currentLineIndex = 7;
       List<String> ingredients = new ArrayList<String>();
       while (!lines.get(currentLineIndex).isBlank()){
            ingredients.add(lines.get(currentLineIndex));
            currentLineIndex += 1;
       }

       currentLineIndex += 2;
       List<String> steps = ArrayList<String>();
       while (!lines.get(currentLineIndex).isBlank()){
            steps.add(lines.get(currentLineIndex)); 
            currentLineIndex += 1;
       }
       upload(title, imgURL, ingredients, steps);
    }
}

