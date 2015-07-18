package app.pomis.reciper;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FullRecipeInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe_info);
        ((TextView)findViewById(R.id.full_info_title)).setText(getIntent().getExtras().getString("short_description"));
        ((TextView)findViewById(R.id.full_info_text)).setText(getIntent().getExtras().getString("description"));
        ArrayList<String> contentsList = Container.findContentsByTitle(getIntent().getExtras().getString("name"));
        final ContentAdapter aa = new ContentAdapter(ContentAdapter.Mode.RECIPE, this, R.layout.content_item, contentsList, null);
        ((ListView)findViewById(R.id.Contents)).setAdapter(aa);
        setListViewHeightBasedOnChildren((ListView)findViewById(R.id.Contents));
        setTitle(getIntent().getExtras().getString("name"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_recipe_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void addToFavs(View view) {
        Container.favouriteRecipes.add(Container.findRecipeByTitle(getIntent().getExtras().getString("name")));
        Toast.makeText(this, "Рецепт "+getIntent().getExtras().getString("name")+" добавлен в Избранное", Toast.LENGTH_LONG).show();
    }
}
