package app.pomis.reciper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanismagilov on 06.09.15.
 */
public class Tool implements IListItem {
    public String Name;

    public Tool (String name){
        Name = name;
    }
    @Override
    public Container.TypeOfFave getTypeOfFave() {
        return Container.TypeOfFave.TOOL;
    }

    @Override
    public String getName() {
        return Name;
    }

    public String getDescr() {
        switch (Name){
            case "Блендер" : return "Измельчитель, ну или миксер";
            case "Плита" : return "А с ней сковородка, кастрюля";
            case "Духовка" : return "Духовой шкаф";
            default: return "";
        }
    }

    static public ArrayList<Tool> toolListFrom(List stringList){
        ArrayList<Tool> toolList = new ArrayList<>();
        try {
            for (String str : (List<String>)stringList) {
                toolList.add(new Tool(str));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return toolList;
    }
}