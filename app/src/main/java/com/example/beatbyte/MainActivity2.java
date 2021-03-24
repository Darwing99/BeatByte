package com.example.beatbyte;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity2 extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener, View.OnClickListener {

    String DEVELOPER_KEY = "AIzaSyCe4T7jI617stWae7hyxAmALQVV3wp41A0";
    YouTubePlayerView youTubePlayer;
    private YouTubePlayer player;
    private boolean fueRestaurado;
    TextView textView;
    TextView txtDesc;
    String titulo, desc;
    private YouTubePlayer.Provider provider;
    private Handler mHandler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        titulo=getIntent().getStringExtra("getTitulo");
        desc=getIntent().getStringExtra("getDescripcion");

        textView=findViewById(R.id.txtTituloVideo);
        textView.setText(titulo);
        txtDesc=findViewById(R.id.txtDescripcionVideo);

        youTubePlayer=findViewById(R.id.video);
        youTubePlayer.initialize(DEVELOPER_KEY,this);
        mHandler = new Handler();


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean fueRestaurado) {
        String codigo = getIntent().getStringExtra("getId");

        if(!fueRestaurado){
            youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL );
            youTubePlayer.cueVideo(codigo);

        }
        // player.setPlayerStateChangeListener(mPlayerStateChangeListener);player.setPlaybackEventListener(mPlaybackEventListener);

    }

    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
        }
    };

    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();
        }
    };


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this,1).show();

        }else{
            String error="Error al iniciar Youtube"+youTubeInitializationResult.toString();
            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
        }
    }


    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==1){

            getYoutubePlayerProvider().initialize(DEVELOPER_KEY,this);
        }
    }
    protected YouTubePlayer.Provider getYoutubePlayerProvider(){

        return youTubePlayer;
    }


    private void displayCurrentTime() {
        if (null == player) return;
        String formattedTime = formatTime(player.getDurationMillis() - player.getCurrentTimeMillis());
        txtDesc.setText(formattedTime);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };
    @Override
    public void onPlaying() {
        mHandler.postDelayed(runnable, 100);
        displayCurrentTime();
    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }


    @Override
    public void onClick(View v) {

    }
}