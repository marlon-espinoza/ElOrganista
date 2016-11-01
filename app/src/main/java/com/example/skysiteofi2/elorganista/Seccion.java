package com.example.skysiteofi2.elorganista;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Seccion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Seccion extends Fragment {

    String[] niveles;
    private ListView lView;
    private Context context;
    public Seccion() {
        super();
    }
    public final Seccion newInstance(String seccion)  {
        Seccion f = new Seccion();
        Bundle bdl = new Bundle(1);
        bdl.putString("seccion", seccion);
        f.setArguments(bdl);
        return f;
    }


    public void completeTask(String result) {
        try {
            JSONArray jObject = new JSONArray(result);
            ArrayList<String> arregloString = new ArrayList<String>();
            for (int i = 0; i < jObject.length(); i++) {
                JSONObject temp = (JSONObject)jObject.get(i);
                String nivel =  temp.getString("nivel");
                arregloString.add(nivel);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, arregloString);
            lView.setAdapter(adapter);
            lView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                {
                    String str = ((TextView) arg1).getText().toString();
                    Intent intent = new Intent(context,Nivel.class);
                    intent.putExtra("nivel", str);
                    startActivity(intent);
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void getNiveles() throws ExecutionException, InterruptedException, JSONException{
        new WebServices(""){
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                completeTask(result);
            }
        }.execute("nivelesapi/");
        //Log.e("error",result+" prueba");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView;

        String curso = getArguments().getString("seccion");
        if(curso.equals("inicio")){

            rootView = inflater.inflate(R.layout.fragment_home, container, false);

        }else {

            rootView = inflater.inflate(R.layout.fragment_seccion, container, false);
            lView = (ListView) rootView.findViewById(R.id.niveles);

            try {
                getNiveles();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        context = getActivity();
        Toast.makeText(context,curso, Toast.LENGTH_LONG).show();




        return rootView;
    }

}
