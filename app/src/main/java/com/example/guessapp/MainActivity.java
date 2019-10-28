package com.example.guessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, ShakeDetector.ShakeListener {
    int x;
    byte wrong;
    TextView rightWrongText, countText, guess;

    boolean gameStarted;

    Button startButton;
    ArrayList<TextView> views = new ArrayList<>();
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rightWrongText = findViewById(R.id.textView);
        countText = findViewById(R.id.textView11);
        startButton = findViewById(R.id.button);

        guess=findViewById(R.id.guess);


        rightWrongText.setText("");
        tts = new TextToSpeech(this, this);

        Sensey.getInstance().init(this);

        Sensey.getInstance().startShakeDetection(this);
    }

    public void start(View view) {

        guess.setText("Guess The Random Number choosen by computer")  ;

        wrong = 0;
        gameStarted = true;
        rightWrongText.setText("");
        //countText.setText("");

        Random random = new Random();

        x = random.nextInt(9) + 1;

        // Toast.makeText(this, "" + x, Toast.LENGTH_SHORT).show();

        for (TextView textView : views) {
            textView.setEnabled(true);
        }

        views.clear();


    }

    public void Answer(View view) {

        if (gameStarted == false) {
//            Toast.makeText(this, "please click start", Toast.LENGTH_SHORT).show();

            YoYo.with(Techniques.Shake).duration(2000).playOn(startButton);
            return;
        }


        TextView tv = (TextView) view;
        tv.setEnabled(false);
        views.add(tv);
        int number = Integer.parseInt(tv.getText().toString());

        YoYo.with(Techniques.Shake).duration(2000).playOn(tv);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            tts.speak(number + "", TextToSpeech.QUEUE_FLUSH, null, null);
//        } else
//            tts.speak(number + "", TextToSpeech.QUEUE_FLUSH, null);

        if (number == x) {
            rightWrongText.setText("Right");
            gameStarted = false;


            guess.setText("Guess The Random Number choosen by computer")  ;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak("right" , TextToSpeech.QUEUE_FLUSH, null);
            } else
                tts.speak("right" , TextToSpeech.QUEUE_FLUSH, null);


        } else {
            rightWrongText.setText("wrong");
            wrong++;
            //countText.setText("" + wrong);

            if(wrong==1){
                guess.setText("2 chanses left");
            }if(wrong==2){
                guess.setText("1 chanses left");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak("wrong" , TextToSpeech.QUEUE_FLUSH, null);
            } else
                tts.speak("wrong" , TextToSpeech.QUEUE_FLUSH, null);
        }

        if (wrong == 3) {
            gameStarted = false;
            Toast.makeText(this, "game over", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { tts.speak("game over" , TextToSpeech.QUEUE_FLUSH, null);
            } else tts.speak("game over" , TextToSpeech.QUEUE_FLUSH, null);

            guess.setText("Guess Again")  ;

        }

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.ENGLISH);
//            tts.setSpeechRate(0.7f);
//            tts.setPitch(0.7f);
        }
    }

    @Override
    public void onShakeDetected() {

    }

    @Override
    public void onShakeStopped() {

        start(null);

    }
}