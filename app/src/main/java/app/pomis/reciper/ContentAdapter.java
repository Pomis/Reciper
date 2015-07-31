package app.pomis.reciper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by romanismagilov on 29.06.15.
 */
public class ContentAdapter extends ArrayAdapter {
    private final Activity activity;
    private final List<String> list;
    private List<String> selection = null;
    private int resource;
    private Mode mode;

    public enum Mode {
        STORE, SELECTOR, RECIPE
    }

    public ContentAdapter(Mode mode, Context context, int resource, List objects, List selection) {
        super(context, resource, objects);
        activity = (Activity) context;
        list = objects;
        if (selection != null) this.selection = selection;
        this.resource = resource;
        this.mode = mode;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if (rowView == null) {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(resource, null);

            // Hold the view objects in an object, that way the don't need to be "re-  finded"
            view = new ViewHolder();
            view.textView = (TextView) rowView.findViewById(R.id.rowContent);
            view.imageView = (ImageView) rowView.findViewById(R.id.rowImage);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }

        /** Set data to your Views. */
        String item = list.get(position);
        view.textView.setText(item);

        switch (mode) {
            case RECIPE:
                if (Container.checkIfContains(list.get(position)))
                    view.imageView.setVisibility(View.VISIBLE);
                else
                    view.imageView.setVisibility(View.INVISIBLE);
                break;

            case SELECTOR:
                if (selection.contains(list.get(position))) {
                    view.imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_done));
                    view.imageView.setVisibility(View.VISIBLE);

                }
                else{
                    view.imageView.setVisibility(View.VISIBLE);
                    view.imageView.setImageDrawable(getIcon(item));
                }
                break;

            case STORE:
                view.imageView.setVisibility(View.VISIBLE);
                view.imageView.setImageDrawable(getIcon(item));
                break;
        }

        return rowView;
    }
    private Drawable getIcon(String icoName){
        Drawable drawable;
        switch (icoName){
            case "Кабачок": drawable=getContext().getResources().getDrawable(R.drawable.kabak);break;
            case "Лавровый лист": drawable=getContext().getResources().getDrawable(R.drawable.lavr);break;
            case "Лук": drawable=getContext().getResources().getDrawable(R.drawable.look);break;
            case "Масло": drawable=getContext().getResources().getDrawable(R.drawable.maslow);break;
            case "Яйца": drawable=getContext().getResources().getDrawable(R.drawable.egg);break;
            case "Соль": drawable=getContext().getResources().getDrawable(R.drawable.salt);break;
            case "Сода": drawable=getContext().getResources().getDrawable(R.drawable.soda);break;
            case "Мука": drawable=getContext().getResources().getDrawable(R.drawable.mooka);break;
            case "Огурец": drawable=getContext().getResources().getDrawable(R.drawable.ogurez);break;
            case "Помидор": drawable=getContext().getResources().getDrawable(R.drawable.pomi);break;
            case "Сметана": drawable=getContext().getResources().getDrawable(R.drawable.smetana);break;
            case "Майонез": drawable=getContext().getResources().getDrawable(R.drawable.mayonez);break;

            case "Апельсин": drawable=getContext().getResources().getDrawable(R.drawable.apelsin);break;
            case "Банан": drawable=getContext().getResources().getDrawable(R.drawable.banan);break;
            case "Картофель": drawable=getContext().getResources().getDrawable(R.drawable.kartoshka);break;
            case "Морковь": drawable=getContext().getResources().getDrawable(R.drawable.morqov);break;
            case "Перец болгарский": drawable=getContext().getResources().getDrawable(R.drawable.perecbolgar);break;
            case "Перец чёрный": drawable=getContext().getResources().getDrawable(R.drawable.perecblack);break;
            case "Укроп": drawable=getContext().getResources().getDrawable(R.drawable.ooqrop);break;
            case "Мясо": drawable=getContext().getResources().getDrawable(R.drawable.meat);break;
            case "Сыр": drawable=getContext().getResources().getDrawable(R.drawable.cheese);break;
            case "Капуста": drawable=getContext().getResources().getDrawable(R.drawable.kapoosta);break;
            case "Колбаса": drawable=getContext().getResources().getDrawable(R.drawable.kolbasa);break;
            case "Крабовые палочки": drawable=getContext().getResources().getDrawable(R.drawable.krab);break;
            case "Кубик-бульон": drawable=getContext().getResources().getDrawable(R.drawable.kubiq);break;
            case "Макароны": drawable=getContext().getResources().getDrawable(R.drawable.makarons);break;
            case "Молоко": drawable=getContext().getResources().getDrawable(R.drawable.moloko);break;
            case "Чеснок": drawable=getContext().getResources().getDrawable(R.drawable.chesnoq);break;
            default: drawable=getContext().getResources().getDrawable(R.drawable.product);break;
        }
        return drawable;

    }
    protected static class ViewHolder {
        protected TextView textView;
        protected ImageView imageView;
    }
}