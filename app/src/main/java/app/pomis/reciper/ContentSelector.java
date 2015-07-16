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
        ArrayList<String> notAddedContents = new ArrayList<>();
        for (String str : allContents)
            if (!Container.selectedContents.contains(str))
                notAddedContents.add(str);
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.list_item, notAddedContents);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent resultData = new Intent();
        resultData.putExtra("test", ((TextView)view).getText());
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    public ArrayList<String> DelDubl(ArrayList<String> array){
        ArrayList<String> result = new ArrayList<String>(new HashSet<String>(array));
        Collections.sort(result);
        //System.out.println(result);
        return result;
    }
}
