package app.pomis.reciper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;


public class FullRecipeInfoActivity extends ActionBarActivity
        implements Animation.AnimationListener,
        ViewTreeObserver.OnScrollChangedListener,
        AdapterView.OnItemClickListener {

    Toast mToast;
    ScrollView mScrollView;
    ListView mListView;
    ArrayList<String> contentsList;
    boolean fabIsHiding = false;
    boolean nowAnimating = false;
    boolean isFavorite = false;
    //boolean scrollStarted = false;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe_info);
        //Фаб
        isFavorite = Container.checkIfContained(Container.favouriteRecipes, getIntent().getExtras().getString("name"));
        if (isFavorite) {
            (findViewById(R.id.fabAddToFavs)).setVisibility(View.INVISIBLE);
        }
        (findViewById(R.id.fabAddToFavs)).bringToFront();
        ((TextView) findViewById(R.id.full_info_text)).setText(getIntent().getExtras().getString("description"));

        if (getIntent().getExtras().getString("short_description") == null || getIntent().getExtras().getString("short_description").length() == 0) {
            findViewById(R.id.full_info_title).setVisibility(View.GONE);
            findViewById(R.id.full_info_title_layout).setVisibility(View.GONE);
        }else{
            ((TextView) findViewById(R.id.full_info_title)).setText(getIntent().getExtras().getString("short_description"));
        }

        contentsList = Container.findContentsByTitle(getIntent().getExtras().getString("name"));


        setTitle(getIntent().getExtras().getString("name"));
        //Скролл
        mScrollView = ((ScrollView) findViewById(R.id.fullRecipeScroller));
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        drawCustomContentList();
    }

    //
    // Нажатие на продукт
    // дабы
    // добавить в список покупок
    @Override               //
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Container.contentsToBeBought.add(contentsList.get(i));
        Container.removeDoubles(Container.contentsToBeBought);
        DatabaseInstruments.saveWishList();
        Toast.makeText(this, "Продукт добавлен в список покупок", Toast.LENGTH_SHORT).show();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isFavorite) {
            MenuItem item = menu.findItem(R.id.action_remove_from_favs);
            item.setEnabled(true);
        } else {
            MenuItem item = menu.findItem(R.id.action_remove_from_favs);
            item.setEnabled(false);
        }
        //menu.removeItem(R.id.action_remove_from_favs);


        return true;
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
        if (id == R.id.action_remove_from_favs) {
            ServerInstruments.getSingleton().decreaseFaveCount(Container.findRecipeByTitle(getIntent().getExtras().getString("name")).RID);
            Container.removeByName(Container.favouriteRecipes, getIntent().getExtras().getString("name"));
            DatabaseInstruments.singleton.updateFave(getIntent().getExtras().getString("name"), false);
            isFavorite = false;
            return true;
        }

        if (id == R.id.action_favs) {
            Intent intent = new Intent(this, FavouritesActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (isFavorite) {
            MenuItem item = menu.findItem(R.id.action_remove_from_favs);
            item.setEnabled(true);
        } else {
            MenuItem item = menu.findItem(R.id.action_remove_from_favs);
            item.setEnabled(false);
        }
        return super.onMenuOpened(featureId, menu);
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

    static public ArrayList<View> viewList;

    void drawCustomContentList() {
        viewList = new ArrayList<>();
        ((LinearLayout) findViewById(R.id.preKitkatList)).setVisibility(View.VISIBLE);
        LayoutInflater inflater = this.getLayoutInflater();
        ContentAdapter adapter = new ContentAdapter(ContentAdapter.Mode.RECIPE, this, R.layout.content_item, contentsList, null);
        for (int i = 0; i < contentsList.size(); i++) {
            final int index = i;
            final FullRecipeInfoActivity context = this;
            View view = adapter.getView(i, null, ((LinearLayout) findViewById(R.id.preKitkatList)));
            ((LinearLayout) findViewById(R.id.preKitkatList)).addView(view);
            viewList.add(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mToast != null) mToast.cancel();
                    if (Container.selectedContents.contains(contentsList.get(index))) {

                    } else if (!Container.contentsToBeBought.contains(contentsList.get(index))) {
                        ((ImageView) FullRecipeInfoActivity.viewList.get(index).findViewById(R.id.rowImageCart))
                                .setVisibility(View.VISIBLE);
                        mToast = Toast.makeText(context, contentsList.get(index) + " добавлен" + WordEndings.getFor(contentsList.get(index)) + " в список покупок", Toast.LENGTH_SHORT);
                        mToast.show();
                        Container.contentsToBeBought.add(contentsList.get(index));
                        Container.contentsToBeBought = new ArrayList<String>(new HashSet<String>(Container.contentsToBeBought));
                        DatabaseInstruments.saveWishList();
                    } else {
                        ((ImageView) FullRecipeInfoActivity.viewList.get(index).findViewById(R.id.rowImageCart))
                                .setVisibility(View.INVISIBLE);
                        mToast = Toast.makeText(context, contentsList.get(index) + " убран" + WordEndings.getFor(contentsList.get(index)) + " из списка покупок", Toast.LENGTH_SHORT);
                        mToast.show();
                        Container.contentsToBeBought.remove(contentsList.get(index));
                        Container.contentsToBeBought = new ArrayList<String>(new HashSet<String>(Container.contentsToBeBought));
                        DatabaseInstruments.saveWishList();
                    }

                }
            });
        }
    }

    public void addToFavs(View view) {
        Container.favouriteRecipes.add(Container.findRecipeByTitle(getIntent().getExtras().getString("name")));
        DatabaseInstruments.singleton.updateFave(getIntent().getExtras().getString("name"), true);
        Toast.makeText(this, "Рецепт «" + getIntent().getExtras().getString("name") + "» добавлен в Избранное", Toast.LENGTH_LONG).show();
        hideFab();
        isFavorite = true;
    }

    void hideFab() {
        fabIsHiding = true;
        animation = AnimationUtils.loadAnimation(this, R.anim.abc_shrink_fade_out_from_bottom);
        (findViewById(R.id.fabAddToFavs)).startAnimation(animation);
        animation.setAnimationListener(this);
    }


    void showFab() {
        if (!isFavorite) {
            fabIsHiding = false;
            animation = AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom);
            (findViewById(R.id.fabAddToFavs)).startAnimation(animation);
            animation.setAnimationListener(this);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        nowAnimating = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //if (scrollStarted) {
        nowAnimating = false;
        if (fabIsHiding) {
            (findViewById(R.id.fabAddToFavs)).setVisibility(View.INVISIBLE);
        } else {
            (findViewById(R.id.fabAddToFavs)).setVisibility(View.VISIBLE);
        }
        //}
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    int oldScrollY = 0;
    int scrollCounter = 0; // Событие срабатывает дважды при создании листвью. Чтобы предотватить срабатывание

    // считаем итерации обработчика
    @Override
    public void onScrollChanged() {
        if (++scrollCounter > 2) {
            int scrollY = mScrollView.getScrollY();
            // Если сейчас не идёт анимация
            if (!nowAnimating && !isFavorite) {
                if (scrollY > oldScrollY + 2 && !fabIsHiding) {
                    hideFab();
                } else if (fabIsHiding && scrollY + 2 < oldScrollY) {
                    showFab();
                }
            }
            Log.d("scrolls: ", "y: " + scrollY + "; oldY: " + oldScrollY);
//        if (mScrollView.getMaxScrollAmount()<=scrollY && !fabIsHiding){
//            Log.d("dsf", mScrollView.getVer+"; "+scrollY);
//            hideFab();
//        }
            oldScrollY = scrollY;
        }
    }

    public void openInBrowser(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                Container.findRecipeByTitle(getIntent().getExtras().getString("name")).Source));
        startActivity(Intent.createChooser(intent, "Выберите браузер"));
    }


}
