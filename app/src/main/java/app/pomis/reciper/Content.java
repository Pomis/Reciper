package app.pomis.reciper;

/**
 * Created by romanismagilov on 20.08.15.
 */
public class Content implements IFavourite {

    @Override
    public Container.TypeOfFave getTypeOfFave() {
        return Container.TypeOfFave.CONTENT;
    }
    public String content;
    public Content(String c){
        content = c;
    }

}
