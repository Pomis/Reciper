package app.pomis.reciper;


import java.util.ArrayList;

/**
 * Created by romanismagilov on 19.05.15.
 */
public class Recipe implements IListItem {


    public String Name;
    public ArrayList<Content> Contents = new ArrayList<>();
    public ArrayList<Tool> Tools = new ArrayList<>();
    public String Description;
    public String ShortDescription;
    public String Source;
    public int RID;
    public float Relevancy = 0;
    public int FavoriteCount = 0;
    public String KindOfDish;
    public int havingContentsCount = 0;
    public int time;

    public String getRelevancyString() {
        return havingContentsCount + "/" + Contents.size() + " продуктов";
    }

    public String getName(){
        return Name;
    }
    public Recipe(String name, String shortDescription, String description, ArrayList<Content> contents, int rid, String kindOfDish,
                  String source, int time, ArrayList<Tool> tools) {
        Name = name;
        Contents = contents;
        Description = description;
        ShortDescription = shortDescription;
        KindOfDish = kindOfDish;
        RID = rid;
        Source = source;
        this.time = time;
        // Если указаны какие-то инструменты. Иначе - можно готовить без инструментов
        if (!(tools.size()==1&&tools.get(0).equals(""))){
            this.Tools = tools;
        }
    }

    public Recipe() {
    }

    @Override
    public Container.TypeOfFave getTypeOfFave() {
        return Container.TypeOfFave.RECIPE;
    }
}
