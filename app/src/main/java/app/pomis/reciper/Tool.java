package app.pomis.reciper;

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
}