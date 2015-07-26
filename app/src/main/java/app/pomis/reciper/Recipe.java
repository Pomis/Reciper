package app.pomis.reciper;


import java.util.ArrayList;

/**
 * Created by romanismagilov on 19.05.15.
 */
public class Recipe {


    public String Name;
    public ArrayList<String> Contents = new ArrayList<>();
    public String Description;
    public String ShortDescription;
    public String Source;
    public int RID;
    public int Relevancy = 0;
    public int FavoriteCount = 0;
    public String KindOfDish;

    public String getRelevancyString() {
        return Relevancy + "/" + Contents.size() + " продуктов";
    }

    public Recipe(String name, String shortDescription, String description, ArrayList<String> contents, int rid, String kindOfDish,
                  String source) {
        Name = name;
        Contents = contents;
        Description = description;
        ShortDescription = shortDescription;
        KindOfDish = kindOfDish;
        RID = rid;
        Source = source;
    }

    public Recipe() {
    }

}
