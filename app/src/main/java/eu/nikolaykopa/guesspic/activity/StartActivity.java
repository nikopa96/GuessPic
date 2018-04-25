package eu.nikolaykopa.guesspic.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import com.vk.sdk.VKScope;

import eu.nikolaykopa.guesspic.R;

public class StartActivity extends AppCompatActivity {

    Button singleGame, gameWithFriend;
    ConstraintLayout constraint;
    VideoView videoView;
    String videoPath = "android.resource://eu.nikolaykopa.guesspic/" + R.raw.startscreenhd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);

        constraint = (ConstraintLayout) findViewById(R.id.constraint);
        singleGame = (Button) findViewById(R.id.single_game);
        gameWithFriend = (Button) findViewById(R.id.game_with_friend);
        videoView = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        singleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    Intent goToChoiceActivity = new Intent(StartActivity.this, ChoiceActivity.class);
                    goToChoiceActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(goToChoiceActivity);
                } else {
                    Toast.makeText(StartActivity.this, getResources().getString(R.string.internet_access), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.setVideoURI(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.setVideoURI(null);
    }
}
