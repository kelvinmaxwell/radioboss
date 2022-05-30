package com.karimax.radioboss;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.karimax.radioboss.databinding.ActivityHomeactivityBinding;

public class homeactivity extends AppCompatActivity {
    private Button btn;
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    private ImageButton imageButton;

    ImageView imageView,imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);

        imageView = findViewById(R.id.imageView4);
        imageView2 = findViewById(R.id.imageView2);
        imageButton = findViewById(R.id.imageButton);




        // Adding the gif here using glide library
        Glide.with(this).load(R.drawable.giphygif).into(imageView);
        Glide.with(this).load(R.drawable.speakergif).into(imageView2);




        imageView.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(this);
       imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playPause) {
                    //btn.setText("Pause Streaming");
                    Glide.with(getApplicationContext()).load(R.drawable.pause_foreground).into(imageButton);
                    imageView.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);

                    if (initialStage) {
                        new Player().execute("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }

                    playPause = true;

                } else {
                   // btn.setText("Launch Streaming");
                    Glide.with(getApplicationContext()).load(R.drawable.play_foreground).into(imageButton);
                    imageView.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;

//                        btn.setText("Launch Streaming");
                        Glide.with(getApplicationContext()).load(R.drawable.play_foreground).into(imageButton);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                imageView.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);

                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("............");
            progressDialog.show();
            imageView.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
        }
    }
}