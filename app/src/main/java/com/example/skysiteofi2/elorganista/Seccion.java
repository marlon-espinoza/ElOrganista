package com.example.skysiteofi2.elorganista;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skysiteofi2.elorganista.DB.NivelDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
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
    private ArrayList<String> arregloIds = new ArrayList<String>();
//    private ProgressBar progressBar=null;
    private AlertDialog dialogCargar=null;
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
                String idnivel =  temp.getString("id");
                NivelDB niveldb = new NivelDB(idnivel, nivel);
                niveldb.save();
                arregloIds.add(idnivel);
                arregloString.add(nivel);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.list_simple_item, arregloString);
            lView.setAdapter(adapter);
            lView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> arg0, View arg1,int posicion, long arg3)
                {
//                    String str = ((TextView) arg1).getText().toString();
//                    Intent intent = new Intent(context,Nivel.class);
//                    intent.putExtra("nivel", str);
//                    intent.putExtra("idnivel",arregloIds.get(posicion));
//                    startActivity(intent);
                    String str = ((TextView) arg1).getText().toString();
                    Nivel nextFrag= new Nivel().newInstance(str);
                    Bundle bdl = new Bundle();
                    bdl.putString("nivel", str);
                    bdl.putString("idnivel", arregloIds.get(posicion));
                    nextFrag.setArguments(bdl);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, nextFrag,"Nivel")
                            .addToBackStack(null)
                            .commit();
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void getNiveles() throws ExecutionException, InterruptedException, JSONException{


        new WebServices(""){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater2 = getActivity().getLayoutInflater();
                builder.setView(inflater2.inflate(R.layout.dialog_cargando, null));
                builder.setTitle("Cargando...");
                builder.setCancelable(false);
                dialogCargar = builder.create();
                dialogCargar.show();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialogCargar.dismiss();
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
        }.execute("nivelesapi/");
        //Log.e("error",result+" prueba");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView;
        context = getActivity();
        
        String curso = getArguments().getString("seccion");
        if(curso.equals("inicio")){

            rootView = inflater.inflate(R.layout.fragment_home, container, false);

        }else {

            rootView = inflater.inflate(R.layout.fragment_seccion, container, false);
            lView = (ListView) rootView.findViewById(R.id.niveles);
//            progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar2);


            try {
                List<NivelDB> niveles = NivelDB.listAll(NivelDB.class);
                System.out.println(niveles.size());
                if(niveles.size()==0)
                    getNiveles();
                else{
                    System.out.println("prueba");
                    ArrayList<String> arregloString = new ArrayList<String>();
                    for (NivelDB niveldb:niveles) {
                        String nivel =  niveldb.getTitulo();
                        String idnivel =  niveldb.getIdNivel();
                        arregloIds.add(idnivel);
                        arregloString.add(nivel);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.list_simple_item, arregloString);
                    lView.setAdapter(adapter);
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        public void onItemClick(AdapterView<?> arg0, View arg1,int posicion, long arg3)
                        {
//                    String str = ((TextView) arg1).getText().toString();
//                    Intent intent = new Intent(context,Nivel.class);
//                    intent.putExtra("nivel", str);
//                    intent.putExtra("idnivel",arregloIds.get(posicion));
//                    startActivity(intent);
                            String str = ((TextView) arg1).getText().toString();
                            Nivel nextFrag= new Nivel().newInstance(str);
                            Bundle bdl = new Bundle();
                            bdl.putString("nivel", str);
                            bdl.putString("idnivel", arregloIds.get(posicion));
                            nextFrag.setArguments(bdl);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, nextFrag,"Nivel")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        context = getActivity();
//        Toast.makeText(context,curso, Toast.LENGTH_LONG).show();

        return rootView;
    }

}
