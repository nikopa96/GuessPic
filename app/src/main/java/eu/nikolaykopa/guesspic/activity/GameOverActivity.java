package eu.nikolaykopa.guesspic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import java.util.Arrays;

import eu.nikolaykopa.guesspic.R;

public class GameOverActivity extends AppCompatActivity implements Animation.AnimationListener {

    TextView text1, text2, scoreText, scoreInt;
    ImageView stars, vk, fb;
    Animation slide, slideUp, slideToRight, slideToLeft, gameOverSlide;
    ConstraintLayout mainGameOverMenu;
    SimpleDraweeView img;
    Button restart, goToCat;
    PointF focusPoint;
    SharedPreferences sharedPref;
    private String[] scope = new String[]{VKScope.WALL};
    CallbackManager callbackManager;
    LoginManager loginManager;
    private ShareDialog shareDialog;
    String link;
    String name;
    String author;
    String license;
    MediaPlayer mediaPlayer;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        getSupportActionBar().hide();

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Fresco.initialize(this);

        final Intent intent = getIntent();
        String[] arr = intent.getStringArrayExtra("arr");

        if (arr[0].equals(getResources().getString(R.string.no_more_answers_4))) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.success);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gameover);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        link = arr[6];
        author = arr[3];
        license = arr[4];
        name = arr[5];
        img = (SimpleDraweeView) findViewById(R.id.img);
        Uri uri = Uri.parse(arr[2]);
        img.setImageURI(uri);

        String category = intent.getStringExtra("category_info");

        if (category.contains("dogs") || category.contains("animals")) {
            focusPoint = new PointF(0f, 0.3f);
        } else if (category.contains("flags") || category.contains("weapons")
                || category.contains("attractions")) {
            focusPoint = new PointF(0f, 0.37f);
        }
        img.getHierarchy().setActualImageFocusPoint(focusPoint);

        mainGameOverMenu = (ConstraintLayout) findViewById(R.id.main_gameover_menu);
        stars = (ImageView) findViewById(R.id.stars);

        vk = (ImageView) findViewById(R.id.vk);

        FacebookSdk.sdkInitialize(getApplicationContext());
        loginManager = LoginManager.getInstance();
        fb = (ImageView) findViewById(R.id.fb);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        facebookAccount();

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        restart = (Button) findViewById(R.id.go_restart);
        goToCat = (Button) findViewById(R.id.go_gotocat);

        slide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide);
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideToRight = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_to_right);
        slideToLeft = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_to_left);
        gameOverSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.game_over_slide);

        mainGameOverMenu.setVisibility(View.INVISIBLE);

        slide.setAnimationListener(this);
        slideUp.setAnimationListener(this);
        slideToRight.setAnimationListener(this);
        slideToLeft.setAnimationListener(this);
        gameOverSlide.setAnimationListener(this);
        text2.setVisibility(View.INVISIBLE);
        stars.startAnimation(slide);
        text1.startAnimation(slideToRight);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        scoreText = (TextView) findViewById(R.id.score_text);
        scoreInt = (TextView) findViewById(R.id.score_int);

        scoreInt.setText(arr[0]);

        if (compareResult(arr[0], category)) {
            saveResult(arr[0], category);
            if (arr[1].equals(getResources().getString(R.string.no_more_answers_4))) {
                scoreText.setText(getResources().getString(R.string.no_more_answers_4));
                scoreText.setTextColor(Color.MAGENTA);
            } else {
                scoreText.setText(getResources().getString(R.string.new_record));
                scoreText.setTextColor(Color.RED);
            }
        } else {
            scoreText.setText(getResources().getString(R.string.score));
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.go_restart:
                        if (isNetworkConnected()) {
                            finish();
                            System.gc();
                            Intent intent1 = new Intent(GameOverActivity.this, Loading.class);
                            intent1.putExtra("chosenGame", intent.getStringExtra("category_info"));
                            Log.d("CATEGORY", intent.getStringExtra("category_info"));
                            startActivity(intent1);
                        } else {
                            Toast.makeText(GameOverActivity.this, getResources().getString(R.string.internet_access), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.go_gotocat:
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                        break;
                    case R.id.vk:
                        VKSdk.login(GameOverActivity.this, scope);
                        break;
                    case R.id.fb:
                        loginManager.logInWithReadPermissions(GameOverActivity.this, Arrays.asList("public_profile"));
                        break;
                }
            }
        };

        restart.setOnClickListener(onClickListener);
        goToCat.setOnClickListener(onClickListener);
        vk.setOnClickListener(onClickListener);
        fb.setOnClickListener(onClickListener);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
                System.gc();
                Intent intent2 = new Intent(GameOverActivity.this, ChoiceActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
            }
        });
    }

    private boolean compareResult(String sc, String category) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPref.getInt(category, 0) < Integer.parseInt(sc);
    }

    private void saveResult(String sc, String category) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(category, Integer.parseInt(sc));
        editor.apply();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == slide) {
            stars.setVisibility(View.INVISIBLE);
        } else if (animation == slideUp) {
            text1.setVisibility(View.INVISIBLE);
            text2.setVisibility(View.INVISIBLE);
            mainGameOverMenu.startAnimation(gameOverSlide);
        } else if (animation == slideToRight) {
            text2.setVisibility(View.VISIBLE);
            text2.startAnimation(slideToLeft);
        } else if (animation == slideToLeft) {
            text1.startAnimation(slideUp);
            text2.startAnimation(slideUp);
        } else if (animation == gameOverSlide) {
            mainGameOverMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                makePost(null, "Hello");
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(GameOverActivity.this, getResources().getString(R.string.vk_error), Toast.LENGTH_SHORT).show();
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void makePost(VKAttachments attachments, String message) {
        new VKShareDialogBuilder()
                .setText(getResources().getString(R.string.did_not_know) + ": " +  name)
                .setAttachmentLink(name + " / " + author + " / " + license, link).setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
            @Override
            public void onVkShareComplete(int postId) {
                Toast.makeText(GameOverActivity.this, getResources().getString(R.string.vk_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVkShareCancel() {
            }

            @Override
            public void onVkShareError(VKError error) {
            }
        })
                .show(getFragmentManager(), "VK_SHARE_DIALOG");
    }

    private void facebookAccount() {
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(link))
                            .build();
                    shareDialog.show(linkContent);
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(GameOverActivity.this, getResources().getString(R.string.fb_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    @Override
    protected void onPause() {
        slide.setAnimationListener(null);
        slideUp.setAnimationListener(null);
        slideToRight.setAnimationListener(null);
        slideToLeft.setAnimationListener(null);
        gameOverSlide.setAnimationListener(null);
        mediaPlayer.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        slide.setAnimationListener(this);
        slideUp.setAnimationListener(this);
        slideToRight.setAnimationListener(this);
        slideToLeft.setAnimationListener(this);
        gameOverSlide.setAnimationListener(this);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        mInterstitialAd.setAdListener(null);
        slide.setAnimationListener(null);
        slideUp.setAnimationListener(null);
        slideToRight.setAnimationListener(null);
        slideToLeft.setAnimationListener(null);
        gameOverSlide.setAnimationListener(null);
        restart.setOnClickListener(null);
        goToCat.setOnClickListener(null);
        vk.setOnClickListener(null);
        fb.setOnClickListener(null);
        mediaPlayer.stop();
        super.onDestroy();
    }
}
