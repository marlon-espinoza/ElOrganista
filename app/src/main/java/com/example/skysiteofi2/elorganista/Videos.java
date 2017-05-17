package com.example.skysiteofi2.elorganista;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Videos extends AppCompatActivity {
    private VideosListAdapter videos;
    private ListView listViewVideos;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context = getBaseContext();
        listViewVideos = (ListView) findViewById(R.id.lista_videos);
        ArrayList videoItems = new ArrayList();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_whats_hot);
        videoItems.add(new VideoItem("video 1","coen zap el lamb ans cosh","",bitmap));
        videoItems.add(new VideoItem("video 2","coen zap el lamb ans cosh","",bitmap));
        videoItems.add(new VideoItem("video 3","coen zap el lamb ans cosh","",bitmap));

        videos = new VideosListAdapter(getApplicationContext(),videoItems);
        listViewVideos.setAdapter(videos);
        listViewVideos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                VideoItem videoItem = (VideoItem)listViewVideos.getAdapter().getItem(position);
                String value = videoItem.getTitulo();
                Intent intent = new Intent(context,ReproductorVideo.class);
                Toast.makeText(context,value, Toast.LENGTH_LONG).show();
                intent.putExtra("titulo",videoItem.getTitulo());
                intent.putExtra("descripcion",videoItem.getDescripcion());
                intent.putExtra("url",videoItem.getUrl());
                startActivity(intent);
            }
        });
        String titulo = getIntent().getStringExtra("subnivel");
        setTitle(titulo);
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
}
