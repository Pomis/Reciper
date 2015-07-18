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


public class RecipiesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayList<Recipe> mostRelevantRecipies = new ArrayList<>();
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
        intent.putExtra("name", mostRelevantRecipies.get(i).Name);
        for (int c=0; c<Container.RecipesList.size(); c++){
            if (Container.RecipesList.get(c).Name==mostRelevantRecipies.get(i).Name){
                intent.putExtra("description", Container.RecipesList.get(c).Description);
                intent.putExtra("short_description", Container.RecipesList.get(c).ShortDescription);
            }
        }
        startActivity(intent);
    }

    public void loadRelevantRecipes(){
        mostRelevantRecipies.clear();
        for (int i=0; i<Container.RecipesList.size(); i++){
            calculateRelevancy(Container.RecipesList.get(i));
        }
        sortByRelevancy();
        Container.removeDoubles();
        for (int i=Container.RecipesList.size()-1; i>Container.RecipesList.size()-10&&i>=0; i--){
            if (Container.RecipesList.get(i).Relevancy>0)
            mostRelevantRecipies.add(Container.RecipesList.get(i));
        }

        // Массив
        mListView = (ListView)findViewById(R.id.RecipiesLV);
        // Адаптер
        final ArrayAdapter<String> aa = new RecipeAdapter(this, R.layout.list_item, mostRelevantRecipies);
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
        for(int i = Container.RecipesList.size()-1 ; i > 0 ; i--){
            for(int j = 0 ; j < i ; j++){
            //  Сравниваем элементы попарно,
            //  если они имеют неправильный порядок,
            //  то меняем местами
            if( Container.RecipesList.get(j).Relevancy > Container.RecipesList.get(j+1).Relevancy ){
                Recipe tmp = Container.RecipesList.get(j);
                Container.RecipesList.set(j,Container.RecipesList.get(j+1));
                Container.RecipesList.set(j+1,tmp);
            }
        }
    }
    }
}
