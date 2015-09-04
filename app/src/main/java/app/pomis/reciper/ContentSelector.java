package app.pomis.reciper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class ContentSelector extends Activity implements AdapterView.OnItemClickListener {

    public ArrayAdapter<Content> mListAdapter;
    public ListView mListView;
    TinyDB tinydb;
    Toolbar toolbar;
    static public ContentSelector instance;
    static public ArrayList<Content> allContents = new ArrayList<>();
    static public ArrayList<Content> selectorList = new ArrayList<>();
    static public ArrayList<Integer> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_selector);
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
        allContents.clear();
        for (int i = 0; i < Container.RecipesList.size(); i++)
            for (int j = 0; j < Container.RecipesList.get(i).Contents.size(); j++) {
                allContents.add(Container.RecipesList.get(i).Contents.get(j));
            }
        allContents = DelDubl(allContents);
        Container.allContents = allContents;
        // Массив
        mListView = (ListView) findViewById(R.id.StoreSelectorLV);
        mListView.setFastScrollEnabled(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            mListView.setFastScrollAlwaysVisible(true);
        }
        mListView.setOnScrollListener(new CustomScrollListener(this));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("title") != null) {

            toolbar.setTitle(getIntent().getExtras().getString("title"));
            for (Content str : allContents)
                if (!Container.contentsToBeBought.contains(str))
                    selectorList.add(str);
            mListAdapter = new ContentAdapter(ContentAdapter.Mode.SELECTOR,
                    this, R.layout.content_item_tall, selectorList, Container.addingContents);
        }
        else {
            toolbar.setTitle("Добавить продукты");
            for (Content str : allContents)
                if (!Container.checkIfContained(str.content,Container.selectedContents))
                    selectorList.add(str);
            mListAdapter = new ContentAdapter(ContentAdapter.Mode.SELECTOR,
                    this, R.layout.content_item_tall, selectorList, Container.addingContents);
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.searchColor));
        //if (!sharedPrefsAreLoaded) loadSharedPrefs();


        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        selectorList.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!Container.addingContents.contains(selectorList.get(i))) {
            Container.addingContents.add(selectorList.get(i));
        } else {
            Container.addingContents.remove(selectorList.get(i));
        }
        mListAdapter.notifyDataSetChanged();

        if (Container.addingContents.size() > 0) {
            findViewById(R.id.fab_add_selected).setVisibility(View.VISIBLE);
        } else
            findViewById(R.id.fab_add_selected).setVisibility(View.INVISIBLE);
    }

    public ArrayList<Content> DelDubl(ArrayList<Content> array) {
        ArrayList<String> stringArrayList = new ArrayList<String>(new HashSet<String>(Content.stringListFrom(array)));
        ArrayList<Content> result = Content.contentListFrom(stringArrayList);
        Collections.sort(result, new ContentComparator());
        //System.out.println(result);
        return result;
    }

    public void addSelected(View view) {
        Intent resultData = new Intent();
        resultData.putExtra("test", Content.stringListFrom(Container.addingContents));
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    public void scrollTo(int i) {
        mListView.smoothScrollToPosition(i);
    }

    public void openSearch(View view) {
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
        searchView.startAnimation(animation);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //fvgbhjnkml,
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResults.clear();
                for (Content c : selectorList)
                    if (c.content.toLowerCase().contains(newText.toLowerCase()))
                        searchResults.add(Container.getId(c.content, selectorList));
                if (searchResults.size() > 0)
                    ContentSelector.instance.scrollTo(searchResults.get(0));
                return false;
            }
        });
        findViewById(R.id.searchButton).setVisibility(View.GONE);
        searchView.setIconified(false);
        toolbar.setBackgroundColor(getResources().getColor(R.color.searchColor));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.searchColorDark));
        }
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ((SearchView) findViewById(R.id.searchView)).setVisibility(View.GONE);
                findViewById(R.id.searchButton).setVisibility(View.VISIBLE);
                toolbar.setBackgroundColor(getResources().getColor(R.color.mainColor));
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.mainColorDark));
                }
                return false;
            }
        });
    }

    private class CustomScrollListener implements AbsListView.OnScrollListener, Animation.AnimationListener {

        private ListView list;
        private int mState = -1;
        private Field stateField = null;
        private Object mFastScroller;
        private int STATE_DRAGGING;
        private ContentSelector context;
        private Animation animation;
        boolean isScrolling = false;

        public CustomScrollListener(ContentSelector context) {
            super();

            String fastScrollFieldName = "mFastScroller";
            // this has changed on Lollipop
            if (Build.VERSION.SDK_INT >= 21) {
                fastScrollFieldName = "mFastScroll";
            }

            try {
                this.list = context.mListView;
                this.context = context;
                Field fastScrollerField = AbsListView.class.getDeclaredField(fastScrollFieldName);
                fastScrollerField.setAccessible(true);
                mFastScroller = fastScrollerField.get(list);

                Field stateDraggingField = mFastScroller.getClass().getDeclaredField("STATE_DRAGGING");
                stateDraggingField.setAccessible(true);
                STATE_DRAGGING = stateDraggingField.getInt(mFastScroller);

                stateField = mFastScroller.getClass().getDeclaredField("mState");
                stateField.setAccessible(true);
                mState = stateField.getInt(mFastScroller);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            // update fast scroll state
            try {
                if (stateField != null) {
                    mState = stateField.getInt(mFastScroller);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            if (mState == STATE_DRAGGING) {
                // the user is fast scrolling through the list
                isScrolling = !isScrolling;

                if (isScrolling) {
                    context.findViewById(R.id.alphaIndexerLayout).setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(this.context, R.anim.abc_grow_fade_in_from_bottom);
                    (findViewById(R.id.alphaIndexerLayout)).startAnimation(animation);
                    //animation.setAnimationListener(this);
                } else {
                    //context.findViewById(R.id.alphaIndexerLayout).setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(this.context, R.anim.abc_shrink_fade_out_from_bottom);
                    (findViewById(R.id.alphaIndexerLayout)).startAnimation(animation);
                    animation.setAnimationListener(this);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            // update fast scroll state
            try {
                if (stateField != null) {
                    mState = stateField.getInt(mFastScroller);
                    //Log.d("scroll", "fast: " + firstVisibleItem);
                    if (ContentSelector.selectorList != null) {
                        ((TextView) context.findViewById(R.id.alphaIndexer))
                                .setText(ContentSelector.selectorList.get(firstVisibleItem).content.substring(0, 1));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            (findViewById(R.id.alphaIndexerLayout)).setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
