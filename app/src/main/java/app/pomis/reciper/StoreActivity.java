package app.pomis.reciper;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;


public class StoreActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    boolean sharedPrefsAreLoaded = false;

    ListView mListView;
    TinyDB tinydb;
    public static ArrayList<String> selectedContents = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        tinydb = new TinyDB(this);

        // Массив
        mListView = (ListView)findViewById(R.id.StoreLV);
        if (!sharedPrefsAreLoaded) loadSharedPrefs();
        RefreshList();
        mListView.setOnItemClickListener(this);
        saveSharedPrefs();

        loadRecipies();
        Container.selectedContents = this.selectedContents;

        refreshTip();

        setTitle("Мои продукты");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            startActivity(new Intent(this, RecipiesActivity.class));
            return true;
        }
        else if (id == R.id.action_clear){
            clearList();
        }
        else if (id == R.id.action_favs){
            startActivity(new Intent(this, FavouritesActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "Продукт "+selectedContents.get(i)+" убран из списка", Toast.LENGTH_SHORT).show();
        selectedContents.remove(i);
        RefreshList();
        saveSharedPrefs();
        refreshTip();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharedPrefsAreLoaded) loadSharedPrefs();
        refreshTip();
    }

    void refreshTip(){
        int maxLength=7;
        int minLength=3;
        if (Container.selectedContents.size()<=minLength){
            ((TextView)findViewById(R.id.tip)).setText("Добавьте продукты!");
        }
        else {
            ((TextView) findViewById(R.id.tip)).setText("");
        }
        ((TextView)findViewById(R.id.tipNext)).setText("");

        if (Container.selectedContents.size()>minLength){
            ((ImageButton)findViewById(R.id.fabNext)).setVisibility(View.VISIBLE);
            if (Container.selectedContents.size()<maxLength){
                ((TextView)findViewById(R.id.tipNext)).setText("Подобрать рецепты");
                ((TextView)findViewById(R.id.tip)).setText("Добавить продукты");
            }
        }
        else{
            ((ImageButton)findViewById(R.id.fabNext)).setVisibility(View.INVISIBLE);
        }

    }

    public void openRecipes(View v){
        startActivity(new Intent(this, RecipiesActivity.class));
    }

    void saveSharedPrefs(){
        tinydb.putListString("selectedContents", selectedContents);
    }

    void loadSharedPrefs(){
        selectedContents=tinydb.getListString("selectedContents");
        sharedPrefsAreLoaded = true;
    }


    int REQUEST_ID=1;
    public void addContent(View v){
        startActivityForResult(new Intent(this, ContentSelector.class),REQUEST_ID);
    }
    String myValue;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ID) {
            if (resultCode == RESULT_OK) {
                myValue = data.getStringExtra("test");
                if (!selectedContents.contains(myValue)) selectedContents.add(myValue);
                saveSharedPrefs();
                RefreshList();
                refreshTip();
            }
        }
    }

    void clearList(){
        selectedContents.clear();
        saveSharedPrefs();
        RefreshList();
        refreshTip();
    }

    void RefreshList(){
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.list_item, selectedContents);
        mListView.setAdapter(aa);
    }

    void loadRecipies(){
        int[] recipes = {R.array.recipe_1, R.array.recipe_2, R.array.recipe_3, R.array.recipe_4,
                R.array.recipe_5, R.array.recipe_6, R.array.recipe_7, R.array.recipe_8,
                R.array.recipe_9};
        for (int i=0; i<recipes.length; i++){
            String[] stringList = getResources().getStringArray(recipes[i]);
            ArrayList<String> contents = new ArrayList<String>(Arrays.asList(stringList[3].split(",")));
            Container.RecipiesList.add(new Recipe(stringList[0], stringList[1], stringList[2], contents));
        }

    }
}
