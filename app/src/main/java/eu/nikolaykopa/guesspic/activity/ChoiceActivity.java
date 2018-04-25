package eu.nikolaykopa.guesspic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import eu.nikolaykopa.guesspic.adapter.MainAdapter;
import eu.nikolaykopa.guesspic.model.Category;
import eu.nikolaykopa.guesspic.R;

public class ChoiceActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    SharedPreferences sharedP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        setTitle(R.string.title_bar_name);

        sharedP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("animalsmusic", R.drawable.animals_icon, R.string.animals, sharedP.getInt("animals_L", 0), sharedP.getInt("animals_T", 0)));
        categoryList.add(new Category("dogs", R.drawable.dogs_icon, R.string.dogs, sharedP.getInt("dogs_L", 0), sharedP.getInt("dogs_T", 0)));
        categoryList.add(new Category("attractions", R.drawable.attractions_icon, R.string.attractions, sharedP.getInt("attractions_L", 0), sharedP.getInt("attractions_T", 0)));
        categoryList.add(new Category("flags", R.drawable.flags_icon, R.string.flags, sharedP.getInt("flags_L", 0), sharedP.getInt("flags_T", 0)));
        categoryList.add(new Category("weapons", R.drawable.weapons_icon, R.string.weapons, sharedP.getInt("weapons_L", 0), sharedP.getInt("weapons_T", 0)));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new MainAdapter(categoryList, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChoiceActivity.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(ChoiceActivity.this, About.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
