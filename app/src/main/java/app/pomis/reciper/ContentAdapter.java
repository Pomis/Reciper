package app.pomis.reciper;

import android.app.Activity;
import android.content.Context;
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

    public ContentAdapter(Context context, int resource, List objects, List selection) {
        super(context, resource, objects);
        activity = (Activity)context;
        list = objects;
        if (selection!=null) this.selection = selection;
        this.resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if(rowView == null)
        {
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
        if (this.selection==null) {
            if (Container.checkIfContains(list.get(position)))
                view.imageView.setVisibility(View.VISIBLE);
            else
                view.imageView.setVisibility(View.INVISIBLE);
        }
        else if (selection.contains(list.get(position))) {
            view.imageView.setVisibility(View.VISIBLE);
        }
            else
                view.imageView.setVisibility(View.INVISIBLE);

        return rowView;
    }

    protected static class ViewHolder{
        protected TextView textView;
        protected ImageView imageView;
    }
}