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

import com.example.skysiteofi2.elorganista.DB.NivelDB;
import com.example.skysiteofi2.elorganista.DB.SubNivelDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Nivel extends Fragment {
    private Context context;
    private ListView lView;
    private ArrayList<String> arregloIds = new ArrayList<String>();
    private ProgressBar progressBar=null;
    NivelDB nivelDB;
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

        ((MainActivity) getActivity()).setTitle("Curso de Órgano | Partes");

        lView = (ListView) rootView.findViewById(R.id.subniveles);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar3);

        String idnivel=getArguments().getString("idnivel");
        System.out.println(idnivel);
        String labelnivel=getArguments().getString("nivel");
        System.out.println("Niveles: "+NivelDB.find(NivelDB.class,"id_nivel = ?",idnivel).size());
        this.nivelDB = NivelDB.find(NivelDB.class,"id_nivel = ?",idnivel).get(0);
        System.out.println(nivelDB.getIdNivel());

        try {
//            Consulta si los subniveles existen en la base de datos
            List<SubNivelDB> subniveles = SubNivelDB.find(SubNivelDB.class,"nivel = ?",nivelDB.getId().toString());
            System.out.println("Subnieveles: "+subniveles.size());
            Log.d(Nivel.class.getSimpleName(),""+subniveles.size());
            if (subniveles.size()==0)
                getSubNiveles(idnivel);
            else{
                ArrayList<String> arregloString = new ArrayList<String>();
                for (SubNivelDB subnivel: subniveles) {
                    String subnivel_titulo =  subnivel.getTitulo();
                    String subnivel_id =  subnivel.getIdSubnivel();

                    arregloIds.add(subnivel_id);
                    arregloString.add(subnivel_titulo);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.list_simple_item, arregloString);
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
            }
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
                System.out.println(temp);
                String subnivel =  temp.getString("subnivel");
                String idsubnivel =  temp.getString("id");
                arregloIds.add(idsubnivel);
                arregloString.add(subnivel);
                System.out.println(idsubnivel);
                SubNivelDB subniveldb = new SubNivelDB(subnivel, this.nivelDB,idsubnivel);
                subniveldb.save();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.list_simple_item, arregloString);
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
