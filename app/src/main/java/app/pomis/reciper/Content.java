package app.pomis.reciper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanismagilov on 20.08.15.
 */
public class Content implements IListItem {

    @Override
    public Container.TypeOfFave getTypeOfFave() {
        return Container.TypeOfFave.CONTENT;
    }
    public String content;
    public boolean isMajor=false;
    public Content(String c){
        content = c;
    }
    public Content(String c, boolean major) { content = c; isMajor = major; }

    public String getName(){
        return content;
    }

    static public ArrayList<Content> contentListFrom(List<String> stringList){
        ArrayList<Content> contentList = new ArrayList<>();
        for (String str : stringList) {
            if (str.substring(0,1).equals("!")){
                contentList.add(new Content(str.substring(1,str.length()), true));
            }
            else
                contentList.add(new Content(str));
        }
        return contentList;
    }

    static public ArrayList<String> stringListFrom(ArrayList<Content> contentsList){
        ArrayList<String> stringList = new ArrayList<>();
        for (Content c : contentsList) {
            stringList.add(c.content);
        }
        return stringList;
    }
}

