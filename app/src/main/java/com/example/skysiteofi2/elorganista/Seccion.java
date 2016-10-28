package com.example.skysiteofi2.elorganista;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
    public Seccion() {
        super();
    }
    public final Seccion newInstance(String curso)  {
        Seccion f = new Seccion();
        Bundle bdl = new Bundle(1);
        bdl.putString("curso", curso);
        f.setArguments(bdl);
        return f;
    }
    private ArrayList<String> getNiveles() throws ExecutionException, InterruptedException, JSONException{
        String result = new WebServices("nivel").execute().get();
        JSONArray jObject = new JSONArray(result);

        ArrayList<String> arregloString = new ArrayList<String>();
        for (int i = 0; i < jObject.length(); i++) {
            JSONObject temp = (JSONObject)jObject.get(i);
            String nivel =  temp.getString("nivel");
            arregloString.add(nivel);
        }
        return arregloString;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seccion, container, false);
        final Context context = getActivity();
        //generate list
        ArrayList<String> nivelList = null;
        try {
            nivelList=getNiveles();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Array[] niveles = nivel.toString();
        String curso = getArguments().getString("curso");
        Toast.makeText(context,curso, Toast.LENGTH_LONG).show();
        ListView lView = (ListView) rootView.findViewById(R.id.niveles);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, nivelList);

        lView.setAdapter(adapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                String str = ((TextView) arg1).getText().toString();
                Toast.makeText(context,str, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context,Nivel.class);
                intent.putExtra("nivel", str);
                startActivity(intent);
            }
        });
        return rootView;
    }

}
