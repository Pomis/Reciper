package app.pomis.reciper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanismagilov on 19.05.15.
 */
public class Container {

    public enum TypeOfFave {
        CONTENT, HEADER, RECIPE, TOOL, SETTING
    }

    static public ArrayList<Recipe> RecipesList = new ArrayList<>();
    static public ArrayList<Recipe> favouriteRecipes = new ArrayList<>();
    static public ArrayList<Content> allContents = new ArrayList<>();
    static public ArrayList<Content> selectedContents;
    static public ArrayList<Content> addingContents = new ArrayList<>();

    // Избранное
    static public ArrayList<Content> contentsToBeBought = new ArrayList<>();
    static public ArrayList<IListItem> favourites = new ArrayList<>();

    // Настройки
    static public ArrayList<Tool> allTools = new ArrayList<>();
    static public ArrayList<Tool> selectedTools = new ArrayList<>();
    static public ArrayList<IListItem> settings = new ArrayList<>();

    static public <T> void removeDoubles(ArrayList<T> list) {
        for (int i = list.size() - 1; i >= 0; i--)
            for (int j = list.size() - 1; j >= 0; j--) {
                if (list.get(i).equals(list.get(j)) && i != j)
                    list.remove(i);
            }
    }

    static public ArrayList<Content> findContentsByRecipeTitle(String title) {
        ArrayList<Content> list = new ArrayList<>();
        for (Recipe recipe : RecipesList) {
            if (recipe.Name.equals(title))
                list = recipe.Contents;
        }
        return list;
    }

    static public boolean checkIfContains(Content selectedContent) {
        for (Content c : selectedContents)
            if (c.content.equals(selectedContent.content))
                return true;
        return false;
    }

    static public Recipe findRecipeByTitle(String title) {
        for (Recipe recipe : RecipesList)
            if (recipe.Name.equals(title))
                return recipe;
        return null;

    }

    static public int getId(String title, ArrayList<Content> array) {
        for (int i = 0; i < array.size(); i++)
            if (array.get(i).content.equals(title))
                return i;
        return -1;
    }

    static public void calculateRelevancy(Recipe recipe, ArrayList<Content> list) {
        recipe.havingContentsCount = 0;
        recipe.Relevancy = 0;

        int majorContentsCount = 0;
        int minorContentsCount = 0;
        float majorPercentage = 0.7f;
        float minorPercentage = 0.3f;

        for (Content recipeContent : recipe.Contents) {
            if (recipeContent.isMajor)
                majorContentsCount++;
            else
                minorContentsCount++;
        }
        for (Content recipeContent : recipe.Contents) {
            for (Content usersContent : list) {
                if (usersContent.getName().equals(recipeContent.getName())) {
                    recipe.havingContentsCount++;
                    if (recipeContent.isMajor) {
                        recipe.Relevancy += majorPercentage / (float) majorContentsCount;
                    } else {
                        recipe.Relevancy += minorPercentage / (float) minorContentsCount;
                    }
                }
            }
        }
    }


    static public void sortByRelevancy(ArrayList<Recipe> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                //  Сравниваем элементы попарно,
                //  если они имеют неправильный порядок,
                //  то меняем местами
                if (list.get(j).Relevancy > list.get(j + 1).Relevancy) {
                    Recipe tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }
    }

    static public void sortByRating(ArrayList<Recipe> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                //  Сравниваем элементы попарно,
                //  если они имеют неправильный порядок,
                //  то меняем местами
                if (list.get(j).FavoriteCount > list.get(j + 1).FavoriteCount) {
                    Recipe tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }
    }

    // Нужны, если ссылки на объекты разные, а названия одинаковые
    static public boolean checkIfContained(List list, String objectName) {
        if (list != null && list.size() > 0 && list.get(0) instanceof IListItem)
            for (IListItem obj : (ArrayList<IListItem>) list) // Всё безопасно, студия гонит
                if (objectName.equals(obj.getName()))
                    return true;
        return false;
    }

//    static public boolean checkIfContained(String contentName, ArrayList<Content> list) {
//        for (Content c : list)
//            if (contentName.equals(c.content))
//                return true;
//        return false;
//    }

    static public void removeByName(List list, String objName) {
        IListItem obj = new Recipe();
        if (list != null && list.size() > 0 && list.get(0) instanceof IListItem) {
            for (IListItem r : (ArrayList<IListItem>) list)
                if (r.getName().equals(objName)) {
                    obj = r;
                }
            if (list.contains(obj))
                list.remove(obj);
        }
    }

    //static public void removeByName(ArrayList<>)
}

