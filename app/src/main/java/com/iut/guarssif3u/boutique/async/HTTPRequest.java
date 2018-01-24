package com.iut.guarssif3u.boutique.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.iut.guarssif3u.boutique.DAO.DAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;

import com.google.gson.Gson;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by younes on 18/01/2018.
 */

public class HTTPRequest<T extends Object> extends AsyncTask<String, Void, String> {

    protected ProgressBar loader;
    protected ListView listView;

    protected ActiviteEnAttenteAvecResultat activite;
    protected DAO dao;

    protected String method;
    protected String error;
    protected Object data;
    protected Class deserializationClass;

    public HTTPRequest(ActiviteEnAttenteAvecResultat activite, DAO dao, String method, Object data, Class deserializationClass,
                       ProgressBar loader, ListView listView){
        this.activite = activite;
        this.dao = dao;
        this.method = method;
        this.data = data;
        this.deserializationClass = deserializationClass;
        this.loader = loader;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute(){
        this.activite.afficheLoader();
    }

    @Override
    protected String doInBackground(String... urls) {
        String urlRequete = urls[0];
        StringBuffer resultat = new StringBuffer(1024);
        try{
            final HttpURLConnection connection = (HttpURLConnection) new URL(urlRequete).openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1500);
            connection.setRequestMethod(this.method);
            connection.setDoInput(true);

            connection.connect();
            InputStream input = connection.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while((line = in.readLine()) != null){
                resultat.append(line);
            }
            in.close();

        } catch (Exception e){
            this.error = "Error : " + e.getMessage();
            return null;
        }

        return resultat.toString();
    }

    @Override
    protected void onPostExecute(String result){
        Gson gson = new Gson();
        ArrayList<T> liste = new ArrayList<T>();

        if(result != null){
            switch (this.method){
                case (HTTPRequestMethod.GET):
                    JsonParser parser = new JsonParser();
                    JsonArray array = parser.parse(result).getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        Object object = gson.fromJson(array.get(i), this.deserializationClass);
                        liste.add((T) object);
                    }

                    activite.notifyRetourRequeteFindAll(liste);
                    break;
               /* case (HTTPRequestMethod.POST ): activite.notifyRetourRequete((T) gson.fromJson(result, T));*/
            }
        } else {

        }

        this.activite.cacheLoaderAfficheContenu();
    }

    /*public void afficheLoader(){
        this.loader.setVisibility(View.VISIBLE);
    }

    public void cacheLoaderAfficheContenu() {
        this.loader.setVisibility(View.INVISIBLE);
        this.listView.setVisibility(View.VISIBLE);
    }*/
}
