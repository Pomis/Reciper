package app.pomis.reciper;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FullRecipeInfoActivity extends ActionBarActivity implements Animation.AnimationListener, ViewTreeObserver.OnScrollChangedListener {

    ScrollView mScrollView;
    boolean fabIsHiding = false;
    boolean nowAnimating = false;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe_info);
        //Фаб
        isFavorite = Container.checkIfContained(Container.favouriteRecipes, getIntent().getExtras().getString("name"));
        if (isFavorite) {
            (findViewById(R.id.fabAddToFavs)).setVisibility(View.INVISIBLE);
        }
        ((TextView) findViewById(R.id.full_info_title)).setText(getIntent().getExtras().getString("short_description"));
        ((TextView) findViewById(R.id.full_info_text)).setText(getIntent().getExtras().getString("description"));
        ArrayList<String> contentsList = Container.findContentsByTitle(getIntent().getExtras().getString("name"));
        final ContentAdapter aa = new ContentAdapter(ContentAdapter.Mode.RECIPE, this, R.layout.content_item, contentsList, null);
        ((ListView) findViewById(R.id.Contents)).setAdapter(aa);
        setListViewHeightBasedOnChildren((ListView) findViewById(R.id.Contents));
        setTitle(getIntent().getExtras().getString("name"));

        //Скролл
        mScrollView = ((ScrollView) findViewById(R.id.fullRecipeScroller));
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(this);


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
            Container.removeByName(Container.favouriteRecipes, getIntent().getExtras().getString("name"));
            DatabaseInstruments.singleton.updateFave(getIntent().getExtras().getString("name"), false);
            isFavorite = false;
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

    public void addToFavs(View view) {
        Container.favouriteRecipes.add(Container.findRecipeByTitle(getIntent().getExtras().getString("name")));
        DatabaseInstruments.singleton.updateFave(getIntent().getExtras().getString("name"), true);
        Toast.makeText(this, "Рецепт " + getIntent().getExtras().getString("name") + " добавлен в Избранное", Toast.LENGTH_LONG).show();
        hideFab();
        isFavorite = true;
    }

    void hideFab() {
        fabIsHiding = true;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_shrink_fade_out_from_bottom);
        (findViewById(R.id.fabAddToFavs)).startAnimation(animation);
        animation.setAnimationListener(this);
    }

    void showFab() {
        if (!isFavorite) {
            fabIsHiding = false;
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom);
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
        nowAnimating = false;
        if (fabIsHiding) {
            (findViewById(R.id.fabAddToFavs)).setVisibility(View.INVISIBLE);
        } else {
            (findViewById(R.id.fabAddToFavs)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    int oldScrollY = 0;

    @Override
    public void onScrollChanged() {
        int scrollY = mScrollView.getScrollY();
        // Если сейчас не идёт анимация
        if (!nowAnimating && !isFavorite) {
            if (scrollY > oldScrollY + 2 && !fabIsHiding) {
                hideFab();
            } else if (fabIsHiding && scrollY + 2 < oldScrollY) {
                showFab();
            }
        }

        oldScrollY = scrollY;
    }
}
