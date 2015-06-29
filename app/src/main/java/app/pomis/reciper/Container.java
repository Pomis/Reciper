package app.pomis.reciper;

import java.util.ArrayList;

/**
 * Created by romanismagilov on 19.05.15.
 */
public class Container {
    static public ArrayList<Recipe> RecipiesList = new ArrayList<>();
    static public ArrayList<Recipe> favouriteRecipies = new ArrayList<>();
    static public ArrayList<String> allContents = new ArrayList<>();
    static public ArrayList<String> selectedContents;


    static public void removeDoubles(){
        for (int i=RecipiesList.size()-1; i>=0; i--)
            for (int j=RecipiesList.size()-1; j>=0; j--){
                if (RecipiesList.get(i).equals(RecipiesList.get(j))&&i!=j)
                    RecipiesList.remove(i);
            }
    }

    static public ArrayList<String> findContentsByTitle(String title){
        ArrayList<String> list = new ArrayList<>();
        for (Recipe recipe: RecipiesList){
            if (recipe.Name.equals(title))
                list = recipe.Contents;
        }
        return list;
    }

    static public boolean checkIfContains(String content){
        for (String selectedContent: selectedContents)
            if (selectedContent.equals(content))
                return true;
        return false;
    }

    static public Recipe findRecipeByTitle(String title){
        for (Recipe recipe: RecipiesList)
            if (recipe.Name.equals(title))
                return recipe;
        return null;

    }
}

