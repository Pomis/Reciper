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
    public int Relevancy=0;

    public String getRelevancyString(){
        return Relevancy+"/"+Contents.size()+" продуктов";
    }
    public Recipe (String name, String shortDescription, String description, ArrayList<String> contents){
        Name = name;
        Contents = contents;
        Description = description;
        ShortDescription = shortDescription;
    }
    public Recipe(){}

}
