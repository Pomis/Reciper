package app.pomis.reciper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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
    private final List<Content> list;
    private List<Content> selection = null;
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
        Content item = list.get(position);
        view.textView.setText(item.content);

        switch (mode) {
            case RECIPE:
                if (Container.checkIfContains(list.get(position)))
                    view.imageView.setVisibility(View.VISIBLE);
                else if (Container.checkIfContained(Container.contentsToBeBought, list.get(position).content)){
                    ((ImageView) rowView.findViewById(R.id.rowImageCart)).setVisibility(View.VISIBLE);
                    //view.imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_shopping_cart_black_24dp));
                }
                else
                    view.imageView.setVisibility(View.INVISIBLE);

                if (position == 0)
                    rowView.findViewById(R.id.border).setVisibility(View.INVISIBLE);
                if (list.get(position).isMajor)
                    view.textView.setTypeface(null, Typeface.BOLD);
                break;

            case SELECTOR:
                if (selection.contains(list.get(position))) {
                    view.imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_done));
                    view.imageView.setVisibility(View.VISIBLE);

                }
                else{
                    view.imageView.setVisibility(View.VISIBLE);
                    view.imageView.setImageDrawable(getContext().getResources().getDrawable(IconHolder.getContentIconId(item.content)));
                }
                break;

            case STORE:
                view.imageView.setVisibility(View.VISIBLE);
                view.imageView.setImageDrawable(getContext().getResources().getDrawable(IconHolder.getContentIconId(item.content)));
                break;
        }

        return rowView;
    }
    protected static class ViewHolder {
        protected TextView textView;
        protected ImageView imageView;
    }
}