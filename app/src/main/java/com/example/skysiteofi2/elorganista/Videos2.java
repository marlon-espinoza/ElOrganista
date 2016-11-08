package com.example.skysiteofi2.elorganista;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Videos2 extends Fragment {
    private ListView lView;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, VideosListAdapter> listDataChild;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView;
        rootView = inflater.inflate(R.layout.activity_videos2, container, false);
        context = getActivity();


        String idSubnivel=getArguments().getString("idsubnivel");
        String labelSubnivel=getArguments().getString("subnivel");


        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        // preparing list data
        try {
            getCanciones(idSubnivel);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    public void completeTask(String result) {
        try {
            JSONArray jObject = new JSONArray(result);
            ArrayList<String> cancionesArray = new ArrayList<String>();

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, VideosListAdapter>();

            for (int i = 0; i < jObject.length(); i++) {
                JSONObject temp = (JSONObject)jObject.get(i);
                // Adding child data
                listDataHeader.add(temp.getString("cancion"));
                JSONArray  videos = temp.getJSONArray("videos");

                ArrayList<VideoItem> videoItems = new ArrayList<VideoItem>();
                for (int z=0;z<videos.length();z++){
                    JSONObject tempVideo = (JSONObject)videos.get(z);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_whats_hot);
                    videoItems.add(new VideoItem(tempVideo.getString("titulo"),tempVideo.getString("descripcion"),tempVideo.getString("videoUrl"),bitmap));
                }
                VideosListAdapter videosAdapterTemp;
                videosAdapterTemp = new VideosListAdapter(context,videoItems);
                listDataChild.put(listDataHeader.get(i), videosAdapterTemp); // Header, Child data
            }

            listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
            // Listview Group click listener
            expListView.setOnGroupClickListener(new OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    // Toast.makeText(getApplicationContext(),
                    // "Group Clicked " + listDataHeader.get(groupPosition),
                    // Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            // Listview Group expanded listener
            expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {

                }
            });
            // Listview Group collasped listener
            expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {

                }
            });

            // Listview on child click listener
            expListView.setOnChildClickListener(new OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub
                    //listDataHeader.get(groupPosition)

                    VideoItem videoClick = (VideoItem) listDataChild.get(listDataHeader.get(groupPosition)).getItem(childPosition);

                    Intent intent = new Intent(context,ReproductorVideo.class);
                    intent.putExtra("titulo",videoClick.getTitulo());
                    intent.putExtra("descripcion",videoClick.getDescripcion());
                    intent.putExtra("url",videoClick.getUrl());
                    //intent.putExtra("imagen",videoClick.getVista());
                    startActivity(intent);

                    return false;
                }
            });

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCanciones(String idSubnivel) throws ExecutionException, InterruptedException, JSONException{
        new WebServices("subnivel_id="+idSubnivel){
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                completeTask(result);
            }
        }.execute("cancionesapi/");
    }

}
