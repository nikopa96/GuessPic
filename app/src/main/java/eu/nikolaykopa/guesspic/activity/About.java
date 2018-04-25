package eu.nikolaykopa.guesspic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eu.nikolaykopa.guesspic.R;
import eu.nikolaykopa.guesspic.adapter.AboutAdapter;
import eu.nikolaykopa.guesspic.model.AboutCard;

public class About extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        List<AboutCard> aboutCardList = new ArrayList<>();
        aboutCardList.add(new AboutCard(getResources().getString(R.string.about_GuessPic_1), getResources().getString(R.string.about_GuessPic_2), getResources().getString(R.string.about_GuessPic_3)));
        aboutCardList.add(new AboutCard(getResources().getString(R.string.about_Retrofit_1), getResources().getString(R.string.about_Retrofit_2), getResources().getString(R.string.about_Retrofit_3)));
        aboutCardList.add(new AboutCard(getResources().getString(R.string.about_Gson_1), getResources().getString(R.string.about_Gson_2), getResources().getString(R.string.about_Gson_3)));
        aboutCardList.add(new AboutCard(getResources().getString(R.string.about_FacebookSDK_1), getResources().getString(R.string.about_FacebookSDK_2), getResources().getString(R.string.about_FacebookSDK_3)));
        aboutCardList.add(new AboutCard(getResources().getString(R.string.about_VKAndroidSDK_1), getResources().getString(R.string.about_VKAndroidSDK_2), getResources().getString(R.string.about_VKAndroidSDK_3)));
        aboutCardList.add(new AboutCard(getResources().getString(R.string.about_Fresco_1), getResources().getString(R.string.about_Fresco_2), getResources().getString(R.string.about_Fresco_3)));
        aboutCardList.add(new AboutCard(getResources().getString(R.string.about_font_1), getResources().getString(R.string.about_font_2), getResources().getString(R.string.about_font_3)));

        recyclerView = (RecyclerView) findViewById(R.id.activity_about_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 1);

        adapter = new AboutAdapter(aboutCardList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
