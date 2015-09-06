package app.pomis.reciper;

import android.widget.AdapterView;

/**
 * Created by romanismagilov on 06.09.15.
 */
public class SettingsListElement implements IListItem {

    public AdapterView.OnItemClickListener mListener;
    String Name;
    private String descr;

    @Override
    public Container.TypeOfFave getTypeOfFave() {
        return Container.TypeOfFave.SETTING;
    }

    public SettingsListElement(String name, String descr, AdapterView.OnItemClickListener listener){
        this.Name = name;
        this.mListener = listener;
        this.descr = descr;
    }
    @Override
    public String getName() {
        return Name;
    }

    public String getDescr() {
        return descr;
    }
}
