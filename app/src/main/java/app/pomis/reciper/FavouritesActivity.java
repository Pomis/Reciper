package app.pomis.reciper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class FavouritesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    //ArrayAdapter mContentAdapter;
    FaveAdapter mFaveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.FavsTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.favColorDark));
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
            Container.favourites.add(new ListHeader((Container.favouriteRecipes.size() > 0) ?
                    "Избранные рецепты" : "Избранных рецептов нет"));

            Container.favourites.addAll(Container.favouriteRecipes);
            Container.favourites.add(new ListHeader((Container.contentsToBeBought.size() > 0) ?
                    "Список покупок" : "Список покупок пуст"));

            for (String content : Container.contentsToBeBought)
                if (content.length()>0)
                    Container.favourites.add(new Content(content));

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
            refresh();
            return true;
        }
        if (id==R.id.action_clear_contents){
            Container.contentsToBeBought.clear();
            DatabaseInstruments.saveWishList();
            Toast.makeText(this, "Список очищен", Toast.LENGTH_SHORT).show();
            refresh();
            return true;
        }
        if (id==R.id.action_about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
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

    Toast toast;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (toast!=null) toast.cancel();
        switch (Container.favourites.get(i).getTypeOfFave()) {
            case CONTENT:
                final int index= i;
                final FavouritesActivity context = this;
                new MaterialDialog.Builder(this)
                        .title(((Content) Container.favourites.get(i)).content)
                        .content("Продукт будет удалён из списка покупок. Переместить его в \"Мои продукты\"?")
                        .positiveText("Да")
                        .neutralText("Нет")
                        .neutralColorAttr(Color.parseColor("#ffffff"))
                        .positiveColorRes(R.color.favColor)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onNeutral(MaterialDialog dialog) {
                                super.onNeutral(dialog);
                                toast = Toast.makeText(context, "Продукт " + ((Content) Container.favourites.get(index)).content + " убран из списка покупок", Toast.LENGTH_SHORT);
                                String removingContent = ((Content) Container.favourites.get(index)).content;
                                Container.contentsToBeBought.remove(removingContent);
                                Container.favourites.remove(index);
                                mFaveAdapter.notifyDataSetChanged();
                                toast.show();
                                DatabaseInstruments.saveWishList();
                                StoreActivity.instance.RefreshList();
                                //if (Container.contentsToBeBought.size() == 0)
                            }

                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                toast = Toast.makeText(context, "Продукт "
                                        + ((Content) Container.favourites.get(index)).content +
                                        " убран из списка покупок и добавлен в \"Мои продукты\"", Toast.LENGTH_SHORT);
                                String removingContent = ((Content) Container.favourites.get(index)).content;
                                if (!Container.selectedContents.contains(removingContent))
                                    Container.selectedContents.add(removingContent);
                                //Container.selectedContents = new ArrayList<>(new HashSet<>(Container.selectedContents));
                                Container.contentsToBeBought.remove(removingContent);
                                Container.favourites.remove(index);
                                mFaveAdapter.notifyDataSetChanged();
                                toast.show();
                                DatabaseInstruments.saveWishList();
                                StoreActivity.instance.RefreshList();
                            }
                        }).show();

                    break;

            case HEADER:
                break;
            case RECIPE:
                Intent intent = new Intent(this, FullRecipeInfoActivity.class);
                intent.putExtra("name", ((Recipe) Container.favourites.get(i)).Name);
                for (int c = 0; c < Container.RecipesList.size(); c++) {
                    if (Container.RecipesList.get(c).Name.equals(((Recipe) Container.favourites.get(i)).Name)) {
                        intent.putExtra("description", Container.RecipesList.get(c).Description);
                        intent.putExtra("short_description", Container.RecipesList.get(c).ShortDescription);
                    }
                }
                startActivity(intent);
                break;
        }

    }

    // Добавить продукты в список покупок
    public void addContent(View view) {
        new MaterialDialog.Builder(this)
                .title("Что ещё купить?")
                .inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                .input("Название продукта", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Container.contentsToBeBought.add(input.toString());
                        refresh();
                        DatabaseInstruments.saveWishList();
                    }
                })
                .neutralText("Выбрать из списка")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        Intent intent = new Intent(getWindow().getContext(), ContentSelector.class);
                        intent.putExtra("title", "Что ещё купить?");
                        startActivityForResult(intent, 666);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 666) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> myValue = data.getStringArrayListExtra("test");
                if (!Container.contentsToBeBought.contains(myValue)) Container.contentsToBeBought.addAll(myValue);
                refresh();
                DatabaseInstruments.saveWishList();
                Container.addingContents.clear();
            }
        }
    }
}
