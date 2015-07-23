package app.pomis.reciper;

import java.util.ArrayList;

/**
 * Created by romanismagilov on 19.05.15.
 */
public class Container {
    public enum TypeOfDish{
        SOUP, DISH, SNACK,
    }

    static public ArrayList<Recipe> RecipesList = new ArrayList<>();
    static public ArrayList<Recipe> favouriteRecipes = new ArrayList<>();
    static public ArrayList<String> allContents = new ArrayList<>();
    static public ArrayList<String> selectedContents;
    static public ArrayList<String> addingContents = new ArrayList<>();


    static public void removeDoubles(ArrayList<Recipe> list){
        for (int i= list.size()-1; i>=0; i--)
            for (int j= list.size()-1; j>=0; j--){
                if (list.get(i).equals(list.get(j))&&i!=j)
                    list.remove(i);
            }
    }

    static public ArrayList<String> findContentsByTitle(String title){
        ArrayList<String> list = new ArrayList<>();
        for (Recipe recipe: RecipesList){
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
        for (Recipe recipe: RecipesList)
            if (recipe.Name.equals(title))
                return recipe;
        return null;

    }

    static public void calculateRelevancy(Recipe a, ArrayList<String> list){
        a.Relevancy=0;
        for (int i=0; i<a.Contents.size(); i++){
            for (int j=0; j<list.size(); j++){
                if (a.Contents.get(i).equals(list.get(j)))
                    a.Relevancy++;
            }
        }
    }



    static public void sortByRelevancy(ArrayList<Recipe> list){
        for(int i = list.size()-1 ; i > 0 ; i--){
            for(int j = 0 ; j < i ; j++){
                //  Сравниваем элементы попарно,
                //  если они имеют неправильный порядок,
                //  то меняем местами
                if( list.get(j).Relevancy > list.get(j+1).Relevancy ){
                    Recipe tmp = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,tmp);
                }
            }
        }
    }

    static public void sortByRating(ArrayList<Recipe> list){
        for(int i = list.size()-1 ; i > 0 ; i--){
            for(int j = 0 ; j < i ; j++){
                //  Сравниваем элементы попарно,
                //  если они имеют неправильный порядок,
                //  то меняем местами
                if( list.get(j).FavoriteCount > list.get(j+1).FavoriteCount ){
                    Recipe tmp = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,tmp);
                }
            }
        }
    }

    // Нужны, если ссылки на объекты разные, а названия одинаковые
    static public boolean checkIfContained(ArrayList<Recipe> list, String recipeName){
        for (Recipe rep: list)
            if (recipeName.equals(rep.Name))
                return true;
        return false;
    }

    static public void removeByName(ArrayList<Recipe> list, String recipeName){
        Recipe recipe = new Recipe();
        for (Recipe r: list)
            if (r.Name.equals(recipeName))
                recipe = r;
        if (list.contains(recipe))
            list.remove(recipe);
    }
}

