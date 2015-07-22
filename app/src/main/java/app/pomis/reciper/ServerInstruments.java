package app.pomis.reciper;

import java.util.ArrayList;

/**
 * Created by romanismagilov on 22.07.15.
 */
public class ServerInstruments {
    static ServerInstruments singleton;
    static public ServerInstruments getSingleton(){
        if (singleton==null){
            singleton = new ServerInstruments();
        }
        return singleton;
    }
    private ServerInstruments(){}

    // Обращаемся к лодеру рецептов, отправляем айдишник последнего рецепта
    public void loadRecipesFromServer(){

    }

    // Увеличиваем рейтинг
    public void increaseFaveCount(int rid){

    }

    // Уменьшаем рейтинг
    public void decreaseFaveCount(int rid){

    }

    // Уменьшаем рейтинг
    public void decreaseFaveCount(ArrayList<Recipe> recipeList){

    }

}
