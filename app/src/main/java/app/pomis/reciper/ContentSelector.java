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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class ContentSelector extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView mListView;
    TinyDB tinydb;
    static public ArrayList<String> allContents = new ArrayList<>();
    static public ArrayList<String> addingContents = new ArrayList<>();
    static ArrayList<String> notAddedContents = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_selector);
        allContents.clear();
        for (int i=0; i<Container.RecipesList.size(); i++)
            for (int j=0; j<Container.RecipesList.get(i).Contents.size(); j++){
                allContents.add(Container.RecipesList.get(i).Contents.get(j));
            }
        allContents = DelDubl(allContents);
        Container.allContents=allContents;
        // Массив
        mListView = (ListView)findViewById(R.id.StoreSelectorLV);
        //if (!sharedPrefsAreLoaded) loadSharedPrefs();

        for (String str : allContents)
            if (!Container.selectedContents.contains(str))
                notAddedContents.add(str);
        final ArrayAdapter<String> aa = new ContentAdapter(this, R.layout.content_item_tall, notAddedContents, addingContents);
        mListView.setAdapter(aa);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        addingContents.clear();
        notAddedContents.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!addingContents.contains(notAddedContents.get(i))){
            addingContents.add(notAddedContents.get(i));
        }
        else{
            addingContents.remove(notAddedContents.get(i));
        }
        final ArrayAdapter<String> aa = new ContentAdapter(this, R.layout.content_item_tall, notAddedContents, addingContents);
        mListView.setAdapter(aa);
        mListView.setOnItemClickListener(this);

        if (addingContents.size()>0){
            findViewById(R.id.fab_add_selected).setVisibility(View.VISIBLE);
        }
        else
            findViewById(R.id.fab_add_selected).setVisibility(View.INVISIBLE);
    }

    public ArrayList<String> DelDubl(ArrayList<String> array){
        ArrayList<String> result = new ArrayList<String>(new HashSet<String>(array));
        Collections.sort(result);
        //System.out.println(result);
        return result;
    }

    public void addSelected(View view) {
        Intent resultData = new Intent();
        resultData.putExtra("test", addingContents);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}
