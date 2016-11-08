package com.example.skysiteofi2.elorganista;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class ReproductorVideo extends AppCompatActivity {
    private VideoView reproductor;
    Bundle newBundy = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_video);
        reproductor =(VideoView)findViewById(R.id.video);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();


        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar1);


        TextView titulo = (TextView) findViewById(R.id.titulo_video_visor);
        TextView descripcion = (TextView) findViewById(R.id.descripcion_video_visor);

        String tituloVideo = getIntent().getStringExtra("titulo");
        String descripcionVideo = getIntent().getStringExtra("descripcion");
        String urlVideo = getIntent().getStringExtra("url");
        Log.e("url video",urlVideo);
        Log.e("titulo",tituloVideo);
        Log.e("descripcion",descripcionVideo);

        titulo.setText(tituloVideo);
        descripcion.setText(descripcionVideo);


        Uri videoUri = Uri.parse(urlVideo);
        reproductor.setVideoURI(videoUri);
        reproductor.setOnErrorListener(mOnErrorListener);
        //Para reproducir archivos almacenados en la memoria SDCard:
//        reproductor.setVideoPath("/mnt/sdcard/videoEjemplo.mp4");
        reproductor.setMediaController(new MediaController(this));
        progressBar.setVisibility(View.VISIBLE);
        reproductor.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                   int arg2) {
                        // TODO Auto-generated method stub
                        progressBar.setVisibility(View.GONE);
                        mp.start();
                    }
                });
            }
        });
        reproductor.start();
        reproductor.requestFocus();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            System.out.println("landscape");
            getSupportActionBar().hide();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            System.out.println("postrait");
            getSupportActionBar().show();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("newBundy", newBundy);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getBundle("newBundy");
    }
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ReproductorVideo.this);
            builder1.setMessage("Video no puede ser mostrado");
            builder1.setCancelable(false);
            builder1.setTitle("Error");
            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }
    };
}
