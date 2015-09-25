package app.pomis.reciper;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class SettingsActivity extends ActionBarActivity {

    MultiTypeAdapter mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new MultiTypeAdapter(this, R.layout.content_item_tall, Container.settings);
        mListView = ((ListView) findViewById(R.id.listSettings));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (Container.settings.get(i).getTypeOfFave()) {
                    case TOOL:
                        toggleToolSelection(i);
                        break;
                    case HEADER:
                        break;
                    case SETTING:
                        ((SettingsListElement)Container.settings.get(i)).mListener.onItemClick(adapterView, view, i, l);
                        break;
                }
            }
        });
        refresh();
    }

    void toggleToolSelection(int i) {
        if (Container.checkIfContained(Container.selectedTools, Container.settings.get(i).getName())) {
            Container.removeByName(Container.selectedTools, Container.settings.get(i).getName());
            DatabaseInstruments.saveSettings();
        } else {
            Container.selectedTools.add((Tool) Container.settings.get(i));
            DatabaseInstruments.saveSettings();
        }
        refresh();
    }

    void refresh() {
        Container.settings.clear();
        Container.settings.add(new ListHeader("Оборудование"));
        DatabaseInstruments.loadSettings();
        Container.settings.addAll(Container.allTools);
        Container.settings.add(new ListHeader("О программе"));
        Container.settings.add(new SettingsListElement(AppInfo.getName(), AppInfo.getDescr(), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getWindow().getContext(), AboutActivity.class));
            }
        }));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
