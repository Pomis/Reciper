package app.pomis.reciper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class ContentSelector extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayAdapter<String> mListAdapter;
    ListView mListView;
    TinyDB tinydb;
    static public ArrayList<String> allContents = new ArrayList<>();
    static ArrayList<String> notAddedContents = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_selector);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        mListAdapter = new ContentAdapter(ContentAdapter.Mode.SELECTOR,
                this, R.layout.content_item_tall, notAddedContents, Container.addingContents);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        notAddedContents.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!Container.addingContents.contains(notAddedContents.get(i))){
            Container.addingContents.add(notAddedContents.get(i));
        }
        else{
            Container.addingContents.remove(notAddedContents.get(i));
        }
        mListAdapter.notifyDataSetChanged();

        if (Container.addingContents.size()>0){
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
        resultData.putExtra("test", Container.addingContents);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

}
