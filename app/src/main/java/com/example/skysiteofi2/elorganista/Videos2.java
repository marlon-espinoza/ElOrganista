package com.example.skysiteofi2.elorganista;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    private ProgressBar progressBar=null;
    private Context context;
    private static ArrayList<GuardarVideo> descargas=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView;
        setHasOptionsMenu(true);
        context = getActivity();
        rootView = inflater.inflate(R.layout.activity_videos2, container, false);
        context = getActivity();
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar4);
        setHasOptionsMenu(true);
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
                    videoItems.add(new VideoItem(tempVideo.getString("id"),tempVideo.getString("titulo"),tempVideo.getString("descripcion"),tempVideo.getString("videoUrl"),bitmap));
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
                    ImageView imgAccion = (ImageView) listDataChild.get(listDataHeader.get(groupPosition)).getView(childPosition,v,parent).findViewById(R.id.ic_accion);
                    ProgressBar progressBar = (ProgressBar) listDataChild.get(listDataHeader.get(groupPosition)).getView(childPosition,v,parent).findViewById(R.id.progress_accion);
                    View child = listDataChild.get(listDataHeader.get(groupPosition)).getView(childPosition,v,parent);
//                    Intent intent = new Intent(context,ReproductorVideo.class);
//                    intent.putExtra("titulo",videoClick.getTitulo());
//                    intent.putExtra("descripcion",videoClick.getDescripcion());
//                    intent.putExtra("url",videoClick.getUrl());
//                    //intent.putExtra("imagen",videoClick.getVista());
//                    startActivity(intent);
                    if (videoClick.getGuardado()){
                        Intent intent = new Intent(context,ReproductorVideo.class);
                        intent.putExtra("titulo",videoClick.getTitulo());
                        intent.putExtra("descripcion",videoClick.getDescripcion());
                        intent.putExtra("url",videoClick.getUrl());
                        String path=context.getFilesDir().getAbsolutePath()+"/"+videoClick.getId()+".mp4";
                        intent.putExtra("path",path);
                        //intent.putExtra("imagen",videoClick.getVista());
                        startActivity(intent);
                    }
                    else{
                        if (!videoClick.getGuardando()){
                            videoClick.setGuardando(true);
                            GuardarVideo evento=new GuardarVideo(videoClick,imgAccion,progressBar,child);
                            String path=context.getFilesDir().getAbsolutePath()+"/"+videoClick.getId()+".mp4";


                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
//                            new GuardarVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,videoClick.getUrl(),path);
                                evento.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,videoClick.getUrl(),path);
                            else
//                            new GuardarVideo().execute(videoClick.getUrl(),path);
                                evento.execute(videoClick.getUrl(),path);
                            descargas.add(evento);
                        }

                    }


                    return false;
                }
            });

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.atras:
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void getCanciones(String idSubnivel) throws ExecutionException, InterruptedException, JSONException{
        new WebServices("subnivel_id="+idSubnivel){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressBar.setVisibility(View.GONE);
                if (!result.equals("Error"))
                    completeTask(result);
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Error de conección");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        }.execute("cancionesapi/");
    }


    private class GuardarVideo extends AsyncTask<String, Integer, Long> {
        final int TIMEOUT_CONNECTION = 15000;//15sec
        final int TIMEOUT_SOCKET = 30000;//30sec
        ImageView img = null;
        ProgressBar pg = null;
        View child = null;
        VideoItem videoClick=null;
        boolean flagFirstConnect = false;

        public void ulrConnect(URL url, String path) throws IOException{
            //Open a connection to that URL.
            URLConnection ucon = url.openConnection();
            ucon.setRequestProperty("connection", "close");
            ucon.setReadTimeout(TIMEOUT_CONNECTION);
            ucon.setConnectTimeout(TIMEOUT_SOCKET);


            //Define InputStreams to read from the URLConnection.
            // uses 3KB download buffer
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(path);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            long total = ucon.getContentLength();
            long progreso=0;
            long progreso2=0;
            while ((len = inStream.read(buff)) != -1)
            {
                if (isCancelled()){
                    //clean up
                    outStream.flush();
                    outStream.close();
                    inStream.close();
                    onCancelled(path);

                    break;
                }

                progreso2+=len > 0 ? len : -len;
                outStream.write(buff,0,len);
//                int d = (int)outStream.getChannel().size();

                progreso = (progreso2*100)/total;
                if(progreso<0)
                    Log.e("!!!!!!!!!!!!",""+progreso);
                publishProgress((int)progreso);
            }

            //clean up
            outStream.flush();
            outStream.close();
            inStream.close();
            videoClick.setGuardado(true);
        }

        public GuardarVideo(VideoItem videoClick,ImageView img,ProgressBar pg,View child) {
            this.img = img;
            this.pg = pg;
            this.child = child;
            this.videoClick=videoClick;
            this.img.setVisibility(View.GONE);
            this.pg.setVisibility(View.VISIBLE);
            this.child.setClickable(false);
            this.pg.setProgress(0);
            Log.e("Pre","Inicia proceso");
        }
//        protected void onPreExecute(){
////            img.setVisibility(View.GONE);
////            pg.setVisibility(View.VISIBLE);
////            child.setClickable(false);
////            pg.setProgress(0);
//            Log.e("Pre","Inicia proceso");
//        }
        protected Long doInBackground(String... params){
            //params[0] -> url
            //params[1] -> filename
            System.setProperty("http.keepAlive", "false");
            URL url = null;
            try{
                url = new URL(params[0]);
                ulrConnect(url, params[1]);

            }catch (IOException e){
                if(!flagFirstConnect){
                    flagFirstConnect = true;
                    try{
                        ulrConnect(url, params[1]);
                    }catch (IOException z){
                        z.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
            return null;
        }



        protected void onProgressUpdate(Integer... progress) {
            Log.e("PROGRESO: ","****: "+progress[0]);
            pg.setProgress(progress[0]);
        }

        protected void onPostExecute(Long result) {
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.ic_play);
            pg.setVisibility(View.GONE);
            descargas.remove(this);
            Log.e("Post","Termina proceso");
            videoClick.setGuardando(true);

        }
        protected void onCancelled(String path) {
            try {
                // delete the original file
                File fdelete = new File(path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Log.e("Borrar: ","file Deleted :" + path);
                    } else {
                        Log.e("Borrar: ","file not Deleted :" + path);
                    }
                }
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        for (GuardarVideo evento:descargas) {
            evento.cancel(true);
        }
        super.onDestroy();

    }
}
