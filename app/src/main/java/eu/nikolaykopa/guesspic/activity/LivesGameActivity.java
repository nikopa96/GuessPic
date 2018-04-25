package eu.nikolaykopa.guesspic.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eu.nikolaykopa.guesspic.R;
import eu.nikolaykopa.guesspic.interf.Request;
import eu.nikolaykopa.guesspic.logic.Logic;
import eu.nikolaykopa.guesspic.model.CountDownTimer;
import eu.nikolaykopa.guesspic.model.Variant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LivesGameActivity extends AppCompatActivity implements Animation.AnimationListener {

    Button button1, button2, button3, button4;
    SimpleDraweeView imageView1, imageView2;
    ImageView heart1, heart2, heart3, heart4, heart5;
    TextView timer, score, paragraph1, paragraph2;
    Logic logic;
    CountDownTimer countDownTimer;
    ToggleButton attributionBox;
    ConstraintLayout livesBox, scoreBox, timerBox, popUp, harderBox, pauseBox;
    FrameLayout backButtonPause;
    ImageView fading;
    ImageView gradient;
    TableLayout attrBox;
    private static Request request;
    private List<ImageView> heartsIcons = new ArrayList<>();
    private List<Button> buttonList = new ArrayList<>();
    LinearLayout buttonBox;
    private boolean backBtnPressed = false;
    private boolean openedAttribution = false;
    private boolean alertIsOpened = false;
    long seconds = 12000;
    private int translateAnimationState = 0;
    Animation translate, popUpAnimationOn, popUpAnimationOff, harder1, harder2;
    private Retrofit retrofit;
    SharedPreferences sharedPref;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_lives_game);
        getSupportActionBar().hide();

        logic = new Logic("easy", 0, 0, 5, 11, 0, 11, this, "L");
        Intent intent = getIntent();
        List<Variant> variantList = intent.getParcelableArrayListExtra("list");
        logic.putData("easy", variantList);
        logic.setData();

        translate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate);
        popUpAnimationOn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.pop_up_on);
        popUpAnimationOff = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.pop_up_off);
        harder1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.harder_1);
        harder2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.harder_2);

        translate.setAnimationListener(this);
        popUpAnimationOn.setAnimationListener(this);
        popUpAnimationOff.setAnimationListener(this);
        harder1.setAnimationListener(this);
        harder2.setAnimationListener(this);

        LayoutInflater pauseLayout = getLayoutInflater();
        View backPauseView = pauseLayout.inflate(R.layout.pause, null, false);
        backButtonPause = (FrameLayout) findViewById(R.id.back_pause);
        backButtonPause.addView(backPauseView);
        Button continueBackBtn = (Button) backPauseView.findViewById(R.id.back_continue);
        Button goToCatBackBtn = (Button) backPauseView.findViewById(R.id.back_go_to_cat);
        Button mainMenuBackBtn = (Button) backPauseView.findViewById(R.id.back_main_menu);
        TextView musicTitle = (TextView) backPauseView.findViewById(R.id.music_title);
        TextView musicLicense = (TextView) backPauseView.findViewById(R.id.music_license);

        ToggleButton soundOnOff = (ToggleButton) backPauseView.findViewById(R.id.sound_toggle);
        soundOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        });

        switch (intent.getStringExtra("category")) {
            case "animals":
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.animalsmusic);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                musicTitle.setClickable(true);
                musicLicense.setClickable(true);
                musicTitle.setMovementMethod(LinkMovementMethod.getInstance());
                musicTitle.setText(Html.fromHtml("<a href='https://soundcloud.com/nicolai-heidlas'>Nicolai Heidlas</a> - " +
                        "<a href='https://soundcloud.com/nicolai-heidlas/95-bpm-back-in-summer-upbeat-ukulele-background-music'>Back In Summer</a>"));
                musicLicense.setText(Html.fromHtml("<a href='https://creativecommons.org/licenses/by/4.0/'>CC BY 4.0</a>"));
                break;
            case "dogs":
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dogsmusic);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                musicTitle.setClickable(true);
                musicLicense.setClickable(true);
                musicTitle.setMovementMethod(LinkMovementMethod.getInstance());
                musicTitle.setText(Html.fromHtml("<a href='https://soundcloud.com/nicolai-heidlas'>Nicolai Heidlas</a> - " +
                "<a href='https://soundcloud.com/nicolai-heidlas/the-happy-song-free-comedy-background-music'>The Happy Song</a>"));
                musicLicense.setText(Html.fromHtml("<a href='https://creativecommons.org/licenses/by/4.0/'>CC BY 4.0</a>"));
                break;
            case "attractions":
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.attractionsmusic);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                musicTitle.setClickable(true);
                musicLicense.setClickable(true);
                musicTitle.setMovementMethod(LinkMovementMethod.getInstance());
                musicTitle.setText(Html.fromHtml("<a href='http://betterwithmusic.com/'>Jahzzar (betterwithmusic.com)</a> - " +
                "<a href='http://freemusicarchive.org/music/Jahzzar/Travellers_Guide/Siesta'>Siesta</a>"));
                musicLicense.setText(Html.fromHtml("<a href='https://creativecommons.org/licenses/by-sa/3.0/'>CC BY-SA 3.0</a>"));
                break;
            case "flags":
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.flagsmusic);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                musicTitle.setClickable(true);
                musicLicense.setClickable(true);
                musicTitle.setMovementMethod(LinkMovementMethod.getInstance());
                musicTitle.setText(Html.fromHtml("<a href='http://betterwithmusic.com/'>Jahzzar (betterwithmusic.com)</a> - " +
                        "<a href='http://freemusicarchive.org/music/Jahzzar/Tumbling_Dishes_Like_Old-Mans_Wishes/Please_Listen_Carefully'>Please Listen Carefully</a>"));
                musicLicense.setText(Html.fromHtml("<a href='https://creativecommons.org/licenses/by-sa/3.0/'>CC BY-SA 3.0</a>"));
                break;
            case "weapons":
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.weaponsmusic);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                musicTitle.setClickable(true);
                musicLicense.setClickable(true);
                musicTitle.setMovementMethod(LinkMovementMethod.getInstance());
                musicTitle.setText(Html.fromHtml("<a href='https://soundcloud.com/nicolai-heidlas'>Nicolai Heidlas</a> - " +
                        "<a href='https://soundcloud.com/nicolai-heidlas/orchestral-battle-music-legendary-sina'>Legendary SINA Awakes</a>"));
                musicLicense.setText(Html.fromHtml("<a href='https://creativecommons.org/licenses/by/4.0/'>CC BY 4.0</a>"));
                break;
        }

        backButtonPause.setVisibility(View.GONE);

        pauseBox = (ConstraintLayout) findViewById(R.id.pause_box);
        pauseBox.setVisibility(View.INVISIBLE);

        livesBox = (ConstraintLayout) findViewById(R.id.lives_box);
        scoreBox = (ConstraintLayout) findViewById(R.id.score_box);
        timerBox = (ConstraintLayout) findViewById(R.id.timer_box);
        fading = (ImageView) findViewById(R.id.fading);
        gradient = (ImageView) findViewById(R.id.gradient);
        imageView1 = (SimpleDraweeView) findViewById(R.id.picture_1);
        imageView2 = (SimpleDraweeView) findViewById(R.id.picture_2);
        harderBox = (ConstraintLayout) findViewById(R.id.harder_box);

        harderBox.setVisibility(View.INVISIBLE);

        heart1 = (ImageView) findViewById(R.id.live1);
        heart2 = (ImageView) findViewById(R.id.live2);
        heart3 = (ImageView) findViewById(R.id.live3);
        heart4 = (ImageView) findViewById(R.id.live4);
        heart5 = (ImageView) findViewById(R.id.live5);
        heartsIcons.add(heart1);
        heartsIcons.add(heart2);
        heartsIcons.add(heart3);
        heartsIcons.add(heart4);
        heartsIcons.add(heart5);

        score = (TextView) findViewById(R.id.score);
        timer = (TextView) findViewById(R.id.timer);

        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        button3 = (Button) findViewById(R.id.btn3);
        button4 = (Button) findViewById(R.id.btn4);

        attributionBox = (ToggleButton) findViewById(R.id.attribution_box);
        attrBox = (TableLayout) findViewById(R.id.attr_box);

        LayoutInflater attribution = getLayoutInflater();
        View view = attribution.inflate(R.layout.attribution, null, false);
        popUp = (ConstraintLayout) findViewById(R.id.popup);
        popUp.addView(view);
        popUp.setVisibility(View.INVISIBLE);

        paragraph1 = (TextView) view.findViewById(R.id.paragraph_1);
        paragraph2 = (TextView) view.findViewById(R.id.paragraph_2);
        paragraph1.setText(Html.fromHtml(logic.getAttribution()[0]));
        paragraph2.setText(Html.fromHtml(logic.getAttribution()[1]));
        paragraph1.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        paragraph2.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        attributionBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openedAttribution = true;
                    countDownTimer.pause();
                    popUp.startAnimation(popUpAnimationOn);
                } else {
                    openedAttribution = false;
                    countDownTimer.resume();
                    popUp.startAnimation(popUpAnimationOff);
                }
            }
        });

        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        Collections.shuffle(buttonList);

        buttonBox = (LinearLayout) findViewById(R.id.buttonBox);
        buttonBox.removeAllViews();
        for (int i = 0; i < buttonList.size(); i++) {
            buttonBox.addView(buttonList.get(i));
        }

        button1.setText(logic.getFirstAnswer());
        button2.setText(logic.getSecondAnswer());
        button3.setText(logic.getThirdAnswer());
        button4.setText(logic.getFourthAnswer());

        countDownTimer = new CountDownTimer(seconds, 1000) {
            public void onTick(long millisUntilFinished) {
                logic.minusTime();
                timer.setText(String.valueOf(logic.getTime()));

                if (timer.getText().equals("0")) {
                    countDownTimer.onFinish();
                }
            }

            public void onFinish() {
                if (heartsIcons.size() != 0 && !alertIsOpened) {
                    countDownTimer.cancel();
                    button1.setEnabled(false);
                    button2.setEnabled(false);
                    button3.setEnabled(false);
                    button4.setEnabled(false);
                    button1.setBackground(getResources().getDrawable(R.drawable.quiz_button_false));
                    setLogicTime();
                }
            }
        }.start();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPref.getBoolean("alert_lives", false)) {
            helpAlert();
            countDownTimer.pause();
        }

        Uri uri = Uri.parse(logic.getImage());
        imageView1.setImageURI(uri);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://nikolaykopa.eu/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        request = retrofit.create(Request.class);

        fetcherEasy();
        fetcherMedium();
        fetcherHard();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn1:
                        countDownTimer.cancel();
                        if (!isNetworkConnected()) {
                            Toast.makeText(LivesGameActivity.this,
                                    getResources().getString(R.string.internet_access),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        button1.setEnabled(false);
                        button2.setEnabled(false);
                        button3.setEnabled(false);
                        button4.setEnabled(false);

                        button1.setBackground(getResources().getDrawable(R.drawable.quiz_button_true));
                        setLogic(button1.getText().toString());
                        break;
                    case R.id.btn2:
                        countDownTimer.cancel();
                        if (!isNetworkConnected()) {
                            Toast.makeText(LivesGameActivity.this,
                                    getResources().getString(R.string.internet_access),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        button1.setEnabled(false);
                        button2.setEnabled(false);
                        button3.setEnabled(false);
                        button4.setEnabled(false);

                        button1.setBackground(getResources().getDrawable(R.drawable.quiz_button_true));
                        button2.setBackground(getResources().getDrawable(R.drawable.quiz_button_false));
                        setLogic(button2.getText().toString());
                        break;
                    case R.id.btn3:
                        countDownTimer.cancel();
                        if (!isNetworkConnected()) {
                            Toast.makeText(LivesGameActivity.this,
                                    getResources().getString(R.string.internet_access),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        button1.setEnabled(false);
                        button2.setEnabled(false);
                        button3.setEnabled(false);
                        button4.setEnabled(false);

                        button1.setBackground(getResources().getDrawable(R.drawable.quiz_button_true));
                        button3.setBackground(getResources().getDrawable(R.drawable.quiz_button_false));
                        setLogic(button3.getText().toString());
                        break;
                    case R.id.btn4:
                        countDownTimer.cancel();
                        if (!isNetworkConnected()) {
                            Toast.makeText(LivesGameActivity.this,
                                    getResources().getString(R.string.internet_access),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        button1.setEnabled(false);
                        button2.setEnabled(false);
                        button3.setEnabled(false);
                        button4.setEnabled(false);

                        button1.setBackground(getResources().getDrawable(R.drawable.quiz_button_true));
                        button4.setBackground(getResources().getDrawable(R.drawable.quiz_button_false));
                        setLogic(button4.getText().toString());
                        break;
                    case R.id.back_continue:
                        closeBackMenu();
                        break;
                    case R.id.back_go_to_cat:
                        finish();
                        System.gc();
                        Intent goToCat = new Intent(LivesGameActivity.this, ChoiceActivity.class);
                        goToCat.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(goToCat);
                        break;
                    case R.id.back_main_menu:
                        finish();
                        System.gc();
                        Intent goToMainMenu = new Intent(LivesGameActivity.this, StartActivity.class);
                        goToMainMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(goToMainMenu);
                        break;
                }
            }
        };

        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
        continueBackBtn.setOnClickListener(onClickListener);
        goToCatBackBtn.setOnClickListener(onClickListener);
        mainMenuBackBtn.setOnClickListener(onClickListener);
    }

    public void setLogic(String text) {
        if (text.equals(logic.getFirstAnswer())) {
            if (!logic.noMoreAnswersFunc()) {
                score.setText(logic.getScore());
                logic.setCounterPositive();
                logic.removeAnswer();
                if (logic.changedLevel()) {
                    harderBox.startAnimation(harder1);
                }
            } else {
                noMoreAnswersAlert();
                return;
            }
        } else {
            logic.minusLive();

            ((ViewGroup) heartsIcons.get(heartsIcons.size() - 1).getParent())
                    .removeView(heartsIcons.get(heartsIcons.size() - 1));
            heartsIcons.remove(heartsIcons.get(heartsIcons.size() - 1));

            if (logic.checkLives()) {
                Intent intent = new Intent(LivesGameActivity.this,
                        GameOverActivity.class);
                String[] arr = new String[]{score.getText().toString(), getResources().getString(R.string.score), logic.getImage(), logic.getAuthor(), logic.getLicense(), logic.getFirstAnswer(), logic.getLink()};
                intent.putExtra("arr", arr);
                Intent intentCat = getIntent();
                intent.putExtra("category_info", intentCat.getStringExtra("category_with_type"));
                finish();
                startActivity(intent);
            } else {
                logic.setCounterNegative();
            }
        }

        logic.setData();
        if (imageView1.getVisibility() == View.VISIBLE) {
            Uri uri = Uri.parse(logic.getImage());
            imageView2.setImageURI(uri);
        } else if (imageView2.getVisibility() == View.VISIBLE) {
            Uri uri = Uri.parse(logic.getImage());
            imageView1.setImageURI(uri);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);

                button1.setBackground(getResources().getDrawable(R.drawable.quiz_button_usual));
                button2.setBackground(getResources().getDrawable(R.drawable.quiz_button_usual));
                button3.setBackground(getResources().getDrawable(R.drawable.quiz_button_usual));
                button4.setBackground(getResources().getDrawable(R.drawable.quiz_button_usual));

                Collections.shuffle(buttonList);
                buttonBox = (LinearLayout) findViewById(R.id.buttonBox);
                buttonBox.removeAllViews();
                for (int i = 0; i < buttonList.size(); i++) {
                    buttonBox.addView(buttonList.get(i));
                }

                button1.setText(logic.getFirstAnswer());
                button2.setText(logic.getSecondAnswer());
                button3.setText(logic.getThirdAnswer());
                button4.setText(logic.getFourthAnswer());

                paragraph1.setText(Html.fromHtml(logic.getAttribution()[0]));
                paragraph2.setText(Html.fromHtml(logic.getAttribution()[1]));
                paragraph1.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
                paragraph2.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

                if (imageView1.getVisibility() == View.VISIBLE) {
                    imageView1.startAnimation(translate);
                } else if (imageView2.getVisibility() == View.VISIBLE) {
                    imageView2.startAnimation(translate);
                }

                logic.updateTimeForScore();
                logic.updateTime();
                countDownTimer.start();
                handler.removeCallbacksAndMessages(null);
            }
        }, 800);
    }

    public void setLogicTime() {
        logic.minusLive();

        ((ViewGroup) heartsIcons.get(heartsIcons.size() - 1).getParent())
                .removeView(heartsIcons.get(heartsIcons.size() - 1));
        heartsIcons.remove(heartsIcons.get(heartsIcons.size() - 1));

        if (logic.checkLives()) {
            Intent intent = new Intent(LivesGameActivity.this,
                    GameOverActivity.class);
            String[] arr = new String[]{score.getText().toString(), getResources().getString(R.string.score), logic.getImage(), logic.getAuthor(), logic.getLicense(), logic.getFirstAnswer(), logic.getLink()};
            intent.putExtra("arr", arr);
            Intent intentCat = getIntent();
            intent.putExtra("category_info", intentCat.getStringExtra("category_with_type"));
            finish();
            startActivity(intent);
        } else {
            logic.setCounterNegative();
        }

        logic.setData();

        if (imageView1.getVisibility() == View.VISIBLE) {
            Uri uri = Uri.parse(logic.getImage());
            imageView2.setImageURI(uri);
        } else if (imageView2.getVisibility() == View.VISIBLE) {
            Uri uri = Uri.parse(logic.getImage());
            imageView1.setImageURI(uri);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);

                button1.setBackground(getResources().getDrawable(R.drawable.quiz_button_usual));

                Collections.shuffle(buttonList);
                buttonBox = (LinearLayout) findViewById(R.id.buttonBox);
                buttonBox.removeAllViews();
                for (int i = 0; i < buttonList.size(); i++) {
                    buttonBox.addView(buttonList.get(i));
                }

                button1.setText(logic.getFirstAnswer());
                button2.setText(logic.getSecondAnswer());
                button3.setText(logic.getThirdAnswer());
                button4.setText(logic.getFourthAnswer());

                paragraph1.setText(Html.fromHtml(logic.getAttribution()[0]));
                paragraph2.setText(Html.fromHtml(logic.getAttribution()[1]));
                paragraph1.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
                paragraph2.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

                if (imageView1.getVisibility() == View.VISIBLE) {
                    imageView1.startAnimation(translate);
                } else if (imageView2.getVisibility() == View.VISIBLE) {
                    imageView2.startAnimation(translate);
                }

                logic.updateTimeForScore();
                logic.updateTime();
                countDownTimer.start();

                handler.removeCallbacksAndMessages(null);
            }
        }, 1000);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == popUpAnimationOn) {
            buttonBox.setVisibility(View.INVISIBLE);
            livesBox.setVisibility(View.INVISIBLE);
            scoreBox.setVisibility(View.INVISIBLE);
            timerBox.setVisibility(View.INVISIBLE);
            fading.setVisibility(View.INVISIBLE);
            gradient.setVisibility(View.INVISIBLE);
            pauseBox.setVisibility(View.VISIBLE);
        } else if (animation == popUpAnimationOff) {
            buttonBox.setVisibility(View.VISIBLE);
            livesBox.setVisibility(View.VISIBLE);
            scoreBox.setVisibility(View.VISIBLE);
            timerBox.setVisibility(View.VISIBLE);
            fading.setVisibility(View.VISIBLE);
            gradient.setVisibility(View.VISIBLE);
            pauseBox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == translate) {
            switch (translateAnimationState) {
                case 0:
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    translateAnimationState = 1;
                    break;
                case 1:
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    translateAnimationState = 0;
                    break;
            }
        } else if (animation == popUpAnimationOn) {
            attributionBox.setChecked(true);
            popUp.setVisibility(View.VISIBLE);
        } else if (animation == popUpAnimationOff) {
            attributionBox.setChecked(false);
            popUp.setVisibility(View.INVISIBLE);
        } else if (animation == harder1) {
            harderBox.setVisibility(View.VISIBLE);
            harderBox.startAnimation(harder2);
        } else if (animation == harder2) {
            harderBox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void noMoreAnswersAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LivesGameActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(getResources().getString(R.string.no_more_answers_1))
                .setMessage(getResources().getString(R.string.no_more_answers_2))
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.no_more_answers_3),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alertIsOpened = true;
                                finish();
                                Intent intent = new Intent(LivesGameActivity.this,
                                        GameOverActivity.class);
                                String[] arr = new String[]{score.getText().toString(), getResources().getString(R.string.no_more_answers_4), logic.getImage(), logic.getAuthor(), logic.getLicense(), logic.getFirstAnswer(), logic.getLink()};
                                intent.putExtra("arr", arr);
                                Intent intentCat = getIntent();
                                intent.putExtra("category_info", intentCat.getStringExtra("category_with_type"));
                                startActivity(intent);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void helpAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_help_lives, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        final TextView textHelp = (TextView) dialogView.findViewById(R.id.text_help);
        final ImageView imgHelp = (ImageView) dialogView.findViewById(R.id.help_img);
        final TextView goodLuck = (TextView) dialogView.findViewById(R.id.good_luck);

        final TextView next = (TextView) dialogView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!next.getText().equals(getResources().getString(R.string.help_6))) {
                    textHelp.setText(getResources().getString(R.string.help_3));
                    imgHelp.setImageResource(R.drawable.attribution_author_dark);
                    imgHelp.getLayoutParams().height = 100;
                    imgHelp.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    goodLuck.setVisibility(View.VISIBLE);
                    next.setText(getResources().getString(R.string.help_6));
                } else {
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("alert_lives", true);
                    editor.apply();
                    dialog.cancel();
                    countDownTimer.resume();
                }
            }
        });

        dialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static Request getApi() {
        return request;
    }

    private void fetcherEasy() {
        Intent intent = getIntent();

        LivesGameActivity.getApi().getData("ru", intent.getStringExtra("resolution"), intent.getStringExtra("category"), "easy")
                .enqueue(new Callback<List<Variant>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Variant>> call,
                                           @NonNull Response<List<Variant>> response) {
                        logic.putData("easy", response.body());
                        call.cancel();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Variant>> call, @NonNull Throwable t) {
                        Log.d("RETROFIT", "ERROR");
                    }
                });
    }

    private void fetcherMedium() {
        Intent intent = getIntent();

        LivesGameActivity.getApi().getData("ru", intent.getStringExtra("resolution"), intent.getStringExtra("category"), "medium")
                .enqueue(new Callback<List<Variant>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Variant>> call,
                                           @NonNull Response<List<Variant>> response) {
                        logic.putData("medium", response.body());
                        call.cancel();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Variant>> call, @NonNull Throwable t) {
                        Log.d("RETROFIT", "ERROR");
                    }
                });
    }

    public void fetcherHard() {
        Intent intent = getIntent();

        LivesGameActivity.getApi().getData("ru", intent.getStringExtra("resolution"), intent.getStringExtra("category"), "hard")
                .enqueue(new Callback<List<Variant>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Variant>> call,
                                           @NonNull Response<List<Variant>> response) {
                        logic.putData("hard", response.body());
                        call.cancel();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Variant>> call, @NonNull Throwable t) {
                        Log.d("RETROFIT", "ERROR");
                    }
                });
    }

    private void closeBackMenu() {
        if (!backBtnPressed) {
            countDownTimer.pause();

            buttonBox.setVisibility(View.INVISIBLE);
            livesBox.setVisibility(View.INVISIBLE);
            scoreBox.setVisibility(View.INVISIBLE);
            timerBox.setVisibility(View.INVISIBLE);
            fading.setVisibility(View.INVISIBLE);
            gradient.setVisibility(View.INVISIBLE);
            pauseBox.setVisibility(View.INVISIBLE);
            attrBox.setVisibility(View.INVISIBLE);
            harderBox.setVisibility(View.INVISIBLE);

            backButtonPause.setVisibility(View.VISIBLE);
            backBtnPressed = true;
        } else {
            if (!openedAttribution) {
                countDownTimer.resume();
                buttonBox.setVisibility(View.VISIBLE);
                livesBox.setVisibility(View.VISIBLE);
                scoreBox.setVisibility(View.VISIBLE);
                timerBox.setVisibility(View.VISIBLE);
                fading.setVisibility(View.VISIBLE);
                gradient.setVisibility(View.VISIBLE);
                attrBox.setVisibility(View.VISIBLE);
            } else {
                pauseBox.setVisibility(View.VISIBLE);
                attrBox.setVisibility(View.VISIBLE);
            }

            backButtonPause.setVisibility(View.INVISIBLE);
            backBtnPressed = false;
        }
    }

    @Override
    public void onBackPressed() {
        closeBackMenu();
    }

    @Override
    protected void onDestroy() {
        translate.setAnimationListener(null);
        popUpAnimationOn.setAnimationListener(null);
        popUpAnimationOff.setAnimationListener(null);
        harder1.setAnimationListener(null);
        harder2.setAnimationListener(null);
        mediaPlayer.stop();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        translate.setAnimationListener(this);
        popUpAnimationOn.setAnimationListener(this);
        popUpAnimationOff.setAnimationListener(this);
        harder1.setAnimationListener(this);
        harder2.setAnimationListener(this);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        translate.setAnimationListener(null);
        popUpAnimationOn.setAnimationListener(null);
        popUpAnimationOff.setAnimationListener(null);
        harder1.setAnimationListener(null);
        harder2.setAnimationListener(null);
        mediaPlayer.stop();
        super.onPause();
    }
}
