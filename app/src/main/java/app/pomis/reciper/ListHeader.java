package app.pomis.reciper;

/**
 * Created by romanismagilov on 20.08.15.
 */
public class ListHeader implements IFavourite {

    @Override
    public Container.TypeOfFave getTypeOfFave() {
        return Container.TypeOfFave.HEADER;
    }
    public String content;
    public ListHeader(String c){
        content = c;
    }
}
