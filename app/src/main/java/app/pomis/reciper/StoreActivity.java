package app.pomis.reciper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class StoreActivity extends Activity implements AdapterView.OnItemClickListener{

    boolean sharedPrefsAreLoaded = false;

    ListView mListView;
    TinyDB tinydb;
    DatabaseInstruments dbi;
    Toolbar toolbar;
    Animation animation;
    Toast mToast;
    ArrayAdapter<String> mAdapter;

    //boolean fabIsHiding = false;
    static public StoreActivity instance;
    public static ArrayList<Content> selectedContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        dbi = new DatabaseInstruments(this);
        tinydb = new TinyDB(this);
        instance = this;
        // Массив
        mListView = (ListView) findViewById(R.id.StoreLV);
        if (!sharedPrefsAreLoaded) loadSharedPrefs();

        mListView.setOnItemClickListener(this);
        saveSharedPrefs();

        Container.favouriteRecipes = dbi.loadFaves();
        loadRecipes();
        Container.selectedContents = this.selectedContents;
        RefreshList();
        refreshTip();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Мои продукты");
        toolbar.setSubtitle(getComment());
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));

        Container.contentsToBeBought = DatabaseInstruments.loadWishList();


        initSwipeOnDismiss();
    }


    public String getComment() {
        String val = "";

        if (selectedContents.size() >= 4 && selectedContents.size() <= 6)
            val = "Хм, из этого можно что-то сделать";
        if (selectedContents.size() > 6)
            val = "Как много еды!";
        if (selectedContents.size() == 11)
            val = "Вы точно студент?";
        if (selectedContents.contains("Апельсин") && selectedContents.contains("Банан") && selectedContents.contains("Яблоко"))
            val = "Фруктовый магнат!";
        if (selectedContents.contains("Мясо") && selectedContents.contains("Тушёнка") && selectedContents.contains("Курица"))
            val = "Откуда столько мяса?";
        if (selectedContents.contains("Армянский лаваш"))
            val = "Шаурмастер!";
        if (selectedContents.size() == 0)
            val = "Что-то совсем пусто...";
        if (selectedContents.size() > 0 && selectedContents.size() < 4)
            val = "Мало продуктов, надо бы больше";
        return val;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    boolean longClicked = false;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (longClicked) {
        } else {
            mToast = Toast.makeText(this, "Смахните продукт в сторону, чтобы удалить", Toast.LENGTH_SHORT);
            mToast.show();
        }
        longClicked = false;
    }


    public void initSwipeOnDismiss(){
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        mListView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                                if (mToast != null)
                                    mToast.cancel();

                                for (int position : reverseSortedPositions) {
                                    mToast = Toast.makeText(getWindow().getContext(), selectedContents.get(position).content + " убран"+WordEndings.getFor(selectedContents.get(position).content)+" из списка", Toast.LENGTH_SHORT);
                                    mAdapter.remove(mAdapter.getItem(position));
                                }
                                if (mToast != null)
                                    mToast.show();

                                mAdapter.notifyDataSetChanged();
                                saveSharedPrefs();
                                refreshTip();
                                toolbar.setSubtitle(getComment());

                            }
                        });
        mListView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        mListView.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
        if (!sharedPrefsAreLoaded) loadSharedPrefs();
        refreshTip();
        RefreshList();
        toolbar.setSubtitle(getComment());
    }

    // FAB анимация туть
    void refreshTip() {
        int maxLength = 7;
        int minLength = 3;
        if (Container.selectedContents.size() <= minLength) {
            ((TextView) findViewById(R.id.tip)).setText("Добавьте продукты!");
        } else {
            ((TextView) findViewById(R.id.tip)).setText("");
        }
        ((TextView) findViewById(R.id.tipNext)).setText("");

        if (Container.selectedContents.size() > minLength) {
            if ((findViewById(R.id.fabNext)).getVisibility() == View.INVISIBLE)
                showFab();//(findViewById(R.id.fabNext)).setVisibility(View.VISIBLE);
            if (Container.selectedContents.size() < maxLength) {
                ((TextView) findViewById(R.id.tipNext)).setText("Подобрать рецепты");
                ((TextView) findViewById(R.id.tip)).setText("Добавить продукты");
            }
        } else {
            if ((findViewById(R.id.fabNext)).getVisibility() == View.VISIBLE)
                hideFab();//(findViewById(R.id.fabNext)).setVisibility(View.INVISIBLE);
        }

    }

    void hideFab() {
        //fabIsHiding = true;
        animation = AnimationUtils.loadAnimation(this, R.anim.abc_shrink_fade_out_from_bottom);
        (findViewById(R.id.fabNext)).startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                (findViewById(R.id.fabNext)).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    void showFab() {
        //if (!isFavorite) {
        //    fabIsHiding = false;
        animation = AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom);
        (findViewById(R.id.fabNext)).startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                (findViewById(R.id.fabNext)).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //}
    }

    public void openRecipes(View v) {
        startActivity(new Intent(this, RecipiesActivity.class));
    }

    void saveSharedPrefs() {
        tinydb.putListString("selectedContents", Content.stringListFrom(selectedContents));
    }

    void loadSharedPrefs() {
        selectedContents = Content.contentListFrom(tinydb.getListString("selectedContents"));
        sharedPrefsAreLoaded = true;
    }


    int REQUEST_ID = 1;

    public void addContent(View v) {
        startActivityForResult(new Intent(this, ContentSelector.class), REQUEST_ID);
    }

    ArrayList<Content> myValue;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ID) {
            if (resultCode == RESULT_OK) {
                myValue = Content.contentListFrom(data.getStringArrayListExtra("test"));
                if (!selectedContents.contains(myValue)) selectedContents.addAll(myValue);
                saveSharedPrefs();
                RefreshList();
                refreshTip();
                Container.addingContents.clear();
                toolbar.setSubtitle(getComment());
            }
        }
    }

    void clearList() {
        selectedContents.clear();
        saveSharedPrefs();
        RefreshList();
        refreshTip();
        toolbar.setSubtitle(getComment());
    }

    void RefreshList() {
        mAdapter = new ContentAdapter(ContentAdapter.Mode.STORE,
                this, R.layout.content_item_tall, selectedContents, new ArrayList());
        mListView.setAdapter(mAdapter);
    }

    void loadRecipes() {
        Container.RecipesList = dbi.loadBasicRecipes();


    }

    public void openFavs(View view) {
        startActivity(new Intent(this, FavouritesActivity.class));
    }

    public void openAbout(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
