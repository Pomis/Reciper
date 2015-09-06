package app.pomis.reciper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by romanismagilov on 20.08.15.
 */
public class MultiTypeAdapter extends ArrayAdapter {
    ArrayList<IListItem> faveList;
    Activity context;

    public MultiTypeAdapter(Context context, int resource, ArrayList<IListItem> faveList) {
        super(context, resource, faveList);
        this.faveList = faveList;
        this.context = (Activity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        switch (faveList.get(position).getTypeOfFave()) {
            case CONTENT:
                ContentViewHolder contentViewHolder = new ContentViewHolder();
                rowView = inflater.inflate(R.layout.content_item_tall, null);

                contentViewHolder.textView = (TextView) rowView.findViewById(R.id.rowContent);
                contentViewHolder.imageView = (ImageView) rowView.findViewById(R.id.rowImage);

                contentViewHolder.imageView.setVisibility(View.VISIBLE);
                contentViewHolder.imageView.setImageDrawable(getContext().getResources()
                        .getDrawable(IconHolder.getContentIconId(((Content) faveList.get(position)).content)));
                contentViewHolder.textView.setText(((Content) faveList.get(position)).content);

                rowView.setTag(contentViewHolder);
                break;

            case HEADER:
                HeaderViewHolder headerViewHolder = new HeaderViewHolder();
                rowView = inflater.inflate(R.layout.header, null);

                headerViewHolder.textView = (TextView) rowView.findViewById(R.id.headerText);
                headerViewHolder.textView.setText(((ListHeader) faveList.get(position)).content);
                rowView.setTag(headerViewHolder);
                break;

            case RECIPE:
                RecipeViewHolder recipeViewHolder = new RecipeViewHolder();
                rowView = inflater.inflate(R.layout.recipe_item, null);

                recipeViewHolder.textView = (TextView) rowView.findViewById(R.id.title);
                recipeViewHolder.imageView = (ImageView) rowView.findViewById(R.id.imageView);
                recipeViewHolder.subTitle = (TextView) rowView.findViewById(R.id.subText);

                rowView.setTag(recipeViewHolder);

                Recipe item = (Recipe) faveList.get(position);
                recipeViewHolder.textView.setText(item.Name);
                recipeViewHolder.subTitle.setText(item.getRelevancyString());

                //
                // Иконочки для блюд
                //
                recipeViewHolder.imageView.setImageDrawable(getContext().getResources()
                        .getDrawable(IconHolder.getDishIconId(item.KindOfDish)));
                break;

            case TOOL:
                ContentViewHolder toolViewHolder = new ContentViewHolder();
                rowView = inflater.inflate(R.layout.content_item_tall, null);

                toolViewHolder.textView = (TextView) rowView.findViewById(R.id.rowContent);
                toolViewHolder.imageView = (ImageView) rowView.findViewById(R.id.rowImage);

                toolViewHolder.imageView.setVisibility(View.VISIBLE);
                toolViewHolder.textView.setText(((Tool) faveList.get(position)).getName());

                rowView.setTag(toolViewHolder);
                Tool tool = (Tool) faveList.get(position);

                ((TextView)rowView.findViewById(R.id.subText)).setText(tool.getDescr());
                if (Container.checkIfContained(Container.selectedTools, tool.Name)) {
                    toolViewHolder.imageView.setImageDrawable(getContext().getResources()
                            .getDrawable(R.drawable.ic_done));
                }
                else {
                    toolViewHolder.imageView.setImageDrawable(getContext().getResources()
                            .getDrawable(IconHolder.getToolIconId(tool.getName())));
                }
                break;

            case SETTING:
                ContentViewHolder settingViewHolder = new ContentViewHolder();
                rowView = inflater.inflate(R.layout.content_item_tall, null);

                settingViewHolder.textView = (TextView) rowView.findViewById(R.id.rowContent);
                settingViewHolder.imageView = (ImageView) rowView.findViewById(R.id.rowImage);

                settingViewHolder.imageView.setVisibility(View.VISIBLE);
                settingViewHolder.textView.setText(((SettingsListElement) faveList.get(position)).getName());

                rowView.setTag(settingViewHolder);
                SettingsListElement elem = (SettingsListElement) faveList.get(position);

                ((TextView)rowView.findViewById(R.id.subText)).setText(elem.getDescr());
                    settingViewHolder.imageView.setImageDrawable(getContext().getResources()
                            .getDrawable(IconHolder.getSettingIconId(elem.getName())));
                break;
        }

        return rowView;
    }

    protected static class ContentViewHolder {
        protected TextView textView;
        protected ImageView imageView;
    }

    protected static class HeaderViewHolder {
        protected TextView textView;
    }

    protected static class RecipeViewHolder {
        protected TextView textView;
        protected ImageView imageView;
        protected TextView subTitle;
    }

}
