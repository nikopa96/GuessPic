package eu.nikolaykopa.guesspic.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import eu.nikolaykopa.guesspic.R;
import eu.nikolaykopa.guesspic.interf.Request;
import eu.nikolaykopa.guesspic.model.Variant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Loading extends AppCompatActivity {

    private static Request request;
    Class chosenGameClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getSupportActionBar().hide();

        chosenGameClass = null;

        Intent chosenGame = getIntent();
        final String category = chosenGame.getStringExtra("chosenGame");
        if (category.endsWith("L")) {
            chosenGameClass = LivesGameActivity.class;
        } else {
            chosenGameClass = TimeGameActivity.class;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nikolaykopa.eu/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        request = retrofit.create(Request.class);

        final String loadCategory = category.substring(0, category.length() - 2);

        final String resolution;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenDensity = metrics.densityDpi;
        if (screenDensity >= 480) {
            resolution = "1080p";
        } else {
            resolution = "720p";
        }

        Loading.getApi().getStarterData("ru", resolution, loadCategory).enqueue(new Callback<List<Variant>>() {
            @Override
            public void onResponse(@NonNull Call<List<Variant>> call, @NonNull Response<List<Variant>> response) {
                finish();
                Intent intent = new Intent(Loading.this, chosenGameClass);
                List<Variant> tempList = response.body();
                call.cancel();

                if (resolution.equals("1080p")) {
                    intent.putExtra("resolution", "1080p");
                } else {
                    intent.putExtra("resolution", "720p");
                }

                intent.putParcelableArrayListExtra("list", (ArrayList) tempList);
                intent.putExtra("category", loadCategory);
                intent.putExtra("category_with_type", category);
                startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<List<Variant>> call, @NonNull Throwable t) {
                Log.d("RETROFIT", "ERROR");
            }
        });

    }

    public static Request getApi() {
        return request;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
