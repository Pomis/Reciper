package app.pomis.reciper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by romanismagilov on 18.07.15.
 */
class RecipeAdapter extends ArrayAdapter {
    private final Activity activity;
    private final List<Recipe> list;

    public RecipeAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        list = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder view;

        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.recipe_item, null);

            view = new ViewHolder();
            view.textView = (TextView) rowView.findViewById(R.id.title);
            view.imageView = (ImageView) rowView.findViewById(R.id.imageView);
            view.subTitle = (TextView) rowView.findViewById(R.id.subText);
            view.faveStar = (ImageView) rowView.findViewById(R.id.faveStar);
            view.timeView = (TextView) rowView.findViewById(R.id.time);
            rowView.setTag(view);

        } else {
            view = (ViewHolder) rowView.getTag();
        }


        /** Set data to your Views. */
        Recipe item = list.get(position);//BackgroundLoader.loadedBooks.get(position);
        view.textView.setText(item.Name);

        view.subTitle.setText(item.getRelevancyString());

        //
        // Иконочки для блюд
        //
        view.imageView.setImageDrawable(getContext().getResources().getDrawable(IconHolder.getDishIconId(item.KindOfDish)));
        view.timeView.setText(item.time+" мин.");
        return rowView;

    }

    protected class ViewHolder {
        protected TextView textView;
        protected ImageView imageView;
        protected TextView subTitle;
        protected TextView timeView;
        protected ImageView faveStar;
    }
}
