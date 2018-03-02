package com.iut.guarssif3u.boutique.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
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
import com.iut.guarssif3u.boutique.Serialiser.DateSerializer;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;

import com.google.gson.Gson;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by younes on 18/01/2018.
 */

public class HTTPRequest<T extends Object> extends AsyncTask<String, Void, String> {

    protected ProgressBar loader;
    protected ListView listView;

    protected ActiviteEnAttenteAvecResultat activite;
    protected DAO dao;

    protected String method;
    protected boolean error;
    protected Object data;
    protected Class deserializationClass;

    public HTTPRequest(ActiviteEnAttenteAvecResultat activite, DAO dao, String method, T data, Class deserializationClass){
        this.activite = activite;
        this.dao = dao;
        this.method = method;
        this.data = data;
        this.deserializationClass = deserializationClass;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... urls) {
        String urlRequete = urls[0];
        StringBuffer resultat = new StringBuffer(1024);

        switch (this.method){
            case (HTTPRequestMethod.GET):
                resultat = doInBackgroundGet(urlRequete);
                break;
            case (HTTPRequestMethod.POST):
                resultat = doInBackgroundPost(urlRequete);
                break;
            case (HTTPRequestMethod.PUT):
                resultat = doInBackgroundPut(urlRequete);
                break;
            case (HTTPRequestMethod.DELETE):
                resultat = doInBackgroundDelete(urlRequete);
                break;
        }

        if(resultat == null) return null;
        return resultat.toString();
    }

    @Override
    protected void onPostExecute(String result){
        if(result != null){
            switch (this.method){
                case (HTTPRequestMethod.GET):
                    postExecuteGet(result);
                    break;
                case (HTTPRequestMethod.POST):
                    postExecutePost(result);
                    break;
                case (HTTPRequestMethod.PUT):
                    postExecutePut();
                    break;
                case (HTTPRequestMethod.DELETE):
                    postExecuteDelete();
                    break;
            }
        }
        else{
            activite.notifyRetourRequete(null, null, this.error);
        }

    }

    protected StringBuffer doInBackgroundGet(String url){
        StringBuffer resultat = new StringBuffer(1024);

        try{
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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
            this.error = true;
            resultat = null;
        }

        return resultat;
    }

    protected StringBuffer doInBackgroundPost(String url){
        StringBuffer resultat = new StringBuffer(1024);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        Gson gson = gsonBuilder.create();

        try{
            String json = gson.toJson(this.data);

            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1500);
            connection.setRequestMethod(this.method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setAllowUserInteraction(false);
            connection.setDoInput(true);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            connection.connect();
            InputStream input = connection.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while((line = in.readLine()) != null){
                resultat.append(line);
            }
            in.close();

        } catch (Exception e){
            this.error = true;
            return null;
        }

        return resultat;
    }

    protected StringBuffer doInBackgroundPut(String url){
        StringBuffer resultat = new StringBuffer(1024);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());

        Gson gson = gsonBuilder.create();

        try{
            String json = gson.toJson(this.data);

            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1500);
            connection.setRequestMethod(this.method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setAllowUserInteraction(false);
            connection.setDoInput(true);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            connection.connect();
            InputStream input = connection.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while((line = in.readLine()) != null){
                resultat.append(line);
            }
            in.close();

        } catch (Exception e){
            this.error = true;
            return null;
        }

        return resultat;
    }

    protected StringBuffer doInBackgroundDelete(String url){
        StringBuffer resultat = new StringBuffer(1024);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());

        Gson gson = gsonBuilder.create();

        try{
            String json = gson.toJson(this.data);

            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1500);
            connection.setRequestMethod(this.method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setAllowUserInteraction(false);
            connection.setDoInput(true);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            connection.connect();
            InputStream input = connection.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while((line = in.readLine()) != null){
                resultat.append(line);
            }
            in.close();

        } catch (Exception e){
            this.error = true;
            return null;
        }

        return resultat;
    }

    protected void postExecuteGet(String result){
        Gson gson = new Gson();
        ArrayList<T> liste = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(result).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            Object object = gson.fromJson(array.get(i), this.deserializationClass);
            liste.add((T) object);
        }

        activite.notifyRetourRequeteFindAll(liste);
    }

    protected void postExecutePost(String result){
        Gson gson = new Gson();
        Object object = gson.fromJson(result, this.deserializationClass);

        activite.notifyRetourRequete((T) object, this.method, this.error);
    }

    protected void postExecutePut(){
        activite.notifyRetourRequete(this.data, this.method, this.error);
    }

    protected void postExecuteDelete(){
        activite.notifyRetourRequete(this.data, this.method, this.error);
    }

}
