package app.pomis.reciper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class RecipiesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> mostRelevantRecipies = new ArrayList<>();
    ListView mListView;
    boolean loaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);
        if (!loaded) loadRelevantRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loaded) loadRelevantRecipes();
    }

    @Override
    public boolean onNavigateUpFromChild(Activity child) {
        loadRelevantRecipes();
        return super.onNavigateUpFromChild(child);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    // Нажатие на рецепт
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, FullRecipeInfoActivity.class);
        intent.putExtra("name", mostRelevantRecipies.get(i));
        for (int c=0; c<Container.RecipiesList.size(); c++){
            if (Container.RecipiesList.get(c).Name==mostRelevantRecipies.get(i)){
                intent.putExtra("description", Container.RecipiesList.get(c).Description);
                intent.putExtra("short_description", Container.RecipiesList.get(c).ShortDescription);
            }
        }
        startActivity(intent);
    }

    public void loadRelevantRecipes(){
        mostRelevantRecipies.clear();
        for (int i=0; i<Container.RecipiesList.size(); i++){
            calculateRelevancy(Container.RecipiesList.get(i));
        }
        sortByRelevancy();
        Container.removeDoubles();
        for (int i=Container.RecipiesList.size()-1; i>Container.RecipiesList.size()-10&&i>=0; i--){
            if (Container.RecipiesList.get(i).Relevancy>0)
            mostRelevantRecipies.add(Container.RecipiesList.get(i).Name);
        }

        // Массив
        mListView = (ListView)findViewById(R.id.RecipiesLV);
        // Адаптер
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.list_item, mostRelevantRecipies);
        mListView.setAdapter(aa);
        mListView.setOnItemClickListener(this);
        loaded = true;
    }

    public void calculateRelevancy(Recipe a){
        a.Relevancy=0;
        for (int i=0; i<a.Contents.size(); i++){
            for (int j=0; j<Container.selectedContents.size(); j++){
                if (a.Contents.get(i).equals(Container.selectedContents.get(j)))
                    a.Relevancy++;
            }
        }
    }



    void sortByRelevancy(){
        for(int i = Container.RecipiesList.size()-1 ; i > 0 ; i--){
            for(int j = 0 ; j < i ; j++){
            //  Сравниваем элементы попарно,
            //  если они имеют неправильный порядок,
            //  то меняем местами
            if( Container.RecipiesList.get(j).Relevancy > Container.RecipiesList.get(j+1).Relevancy ){
                Recipe tmp = Container.RecipiesList.get(j);
                Container.RecipiesList.set(j,Container.RecipiesList.get(j+1));
                Container.RecipiesList.set(j+1,tmp);
            }
        }
    }
    }
}
