package app.pomis.reciper;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavouritesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayAdapter mContentAdapter;
    FaveAdapter mFaveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.FavsTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.favColorDark));
            getWindow().setStatusBarColor(getResources().getColor(R.color.favColorDark));

            //setTitleColor(getResources().getColor(R.color.favColorDark));
        }
        setContentView(R.layout.activity_favourites);
        setTitle("Избранное");
        refresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    void refresh() {
        if (Container.favouriteRecipes != null) {
            //mContentAdapter = new RecipeAdapter(this, R.layout.recipe_item, Container.favouriteRecipes);
            Container.favourites.clear();
            Container.favourites.add(new ListHeader("Избранные продукты"));
            Container.favourites.addAll(Container.favouriteRecipes);
            Container.favourites.add(new ListHeader("Продукты, которые надо купить"));
            for (String content : Container.contentsToBeBought) {
                Container.favourites.add(new Content(content));
            }
            mFaveAdapter = new FaveAdapter(this, R.layout.recipe_item, Container.favourites);
            ListView listView = ((ListView) findViewById(R.id.listFavs));
            listView.setAdapter(mFaveAdapter);
            listView.setOnItemClickListener(this);
            mFaveAdapter.notifyDataSetChanged();

            for (int i = 0; i < Container.favouriteRecipes.size(); i++) {
                Container.calculateRelevancy(Container.favouriteRecipes.get(i), Container.selectedContents);
            }
            Container.sortByRelevancy(Container.favouriteRecipes);
            Container.removeDoubles(Container.favouriteRecipes);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_favs) {
            ServerInstruments.getSingleton().decreaseFaveCount(Container.favouriteRecipes);
            Container.favouriteRecipes.clear();
            DatabaseInstruments.singleton.clearFaveList();
            Toast.makeText(this, "Список очищен", Toast.LENGTH_SHORT).show();
            return true;
        }
//
//        // Сортировка по рейтингу (количество добавлений в избранное)
//        if (id == R.id.action_sort_rating) {
//            if (Container.favouriteRecipes != null) {
//                Container.sortByRating(Container.favouriteRecipes);
//                mContentAdapter.notifyDataSetChanged();
//            }
//        }
//
//        // Сортировка по количеству доступных продуктов
//        if (id == R.id.action_sort_contents) {
//            if (Container.favouriteRecipes != null) {
//                Container.sortByRelevancy(Container.favouriteRecipes);
//                mContentAdapter.notifyDataSetChanged();
//            }
//
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, FullRecipeInfoActivity.class);
        intent.putExtra("name", Container.favouriteRecipes.get(i).Name);
        for (int c = 0; c < Container.RecipesList.size(); c++) {
            if (Container.RecipesList.get(c).Name.equals(Container.favouriteRecipes.get(i).Name)) {
                intent.putExtra("description", Container.RecipesList.get(c).Description);
                intent.putExtra("short_description", Container.RecipesList.get(c).ShortDescription);
            }
        }
        startActivity(intent);

        //
            switch (Container.favourites.get(i).getTypeOfFave()){
                case CONTENT:
                    break;
                case HEADER:
                    break;
                case RECIPE:
                    break;
            }

    }
}
