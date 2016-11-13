package com.example.skysiteofi2.elorganista;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by skysiteofi2 on 27/10/16.
 */

public class VideosListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<VideoItem> videos;

    public VideosListAdapter(Context context, ArrayList<VideoItem> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int i) {
        return videos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.video_list_item, null);
        }
        ImageView imgIcon = (ImageView) view.findViewById(R.id.zoom_video);
        ImageView imgAccion = (ImageView) view.findViewById(R.id.ic_accion);
        TextView txtTitle = (TextView) view.findViewById(R.id.titulo_video);
        TextView txtCount = (TextView) view.findViewById(R.id.descripcion_video);

        imgIcon.setImageBitmap(videos.get(position).getVista());
        txtTitle.setText(videos.get(position).getTitulo());
        txtCount.setText(videos.get(position).getDescripcion());

        return view;
    }

}
