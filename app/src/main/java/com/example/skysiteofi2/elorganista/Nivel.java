package com.example.skysiteofi2.elorganista;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Nivel extends Fragment {
    private Context context;
    private ListView lView;
    private ArrayList<String> arregloIds = new ArrayList<String>();
    private ProgressBar progressBar=null;

    public Nivel() {
        super();
    }
    public final Nivel newInstance(String subnivel)  {
        Nivel f = new Nivel();
        Bundle bdl = new Bundle(1);
        bdl.putString("subnivel", subnivel);
        f.setArguments(bdl);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView;
        setHasOptionsMenu(true);
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_subnivel, container, false);
        lView = (ListView) rootView.findViewById(R.id.subniveles);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar3);
        String idnivel=getArguments().getString("idnivel");
        String labelnivel=getArguments().getString("nivel");

        try {
            getSubNiveles(idnivel);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        context = getActivity();
        return rootView;
    }

    public void completeTask(String result) {
        try {
            JSONArray jObject = new JSONArray(result);
            ArrayList<String> arregloString = new ArrayList<String>();
            for (int i = 0; i < jObject.length(); i++) {
                JSONObject temp = (JSONObject)jObject.get(i);
                String subnivel =  temp.getString("subnivel");
                String idnivel =  temp.getString("id");
                arregloIds.add(idnivel);
                arregloString.add(subnivel);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, arregloString);
            lView.setAdapter(adapter);
            lView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> arg0, View arg1,int posicion, long arg3)
                {
                    String str = ((TextView) arg1).getText().toString();
                    Videos2 nextFrag= new Videos2();
                    Bundle bdl = new Bundle();
                    bdl.putString("subnivel", str);
                    bdl.putString("idsubnivel", arregloIds.get(posicion));
                    nextFrag.setArguments(bdl);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, nextFrag,"Subnivel")
                            .addToBackStack(null)
                            .commit();
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

    private void getSubNiveles(String idnivel) throws ExecutionException, InterruptedException, JSONException{
        new WebServices("nivel_id="+idnivel){
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
        }.execute("subnivelesapi/");


    }


}
