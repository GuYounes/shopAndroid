package com.iut.guarssif3u.boutique.async;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by younes on 13/01/2018.
 */

public class ImageFromURL extends AsyncTask<String, Void, Bitmap> {

    protected Activity activity;
    protected ImageView imageView;
    protected Drawable substitut;

    public ImageFromURL(Activity activite, ImageView imageView, Drawable substitut){
        this.activity = activite;
        this.imageView = imageView;
        this.substitut = substitut;
    }

    @Override
    protected void onPreExecute(){
        this.activity.afficheLoad();
    }

    @Override
    protected void onProgressUpdate(Integer... params){

    }

    @Override
    protected void onPostExecute(Bitmap result){
        if(result == null){
            imageView.setImageDrawable(substitut);
        }
        else {
            imageView.setImageBitmap(result);
        }

        this.activity.cacheLoad();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlIcone = urls[0];
        Bitmap icone = null;

        try{
            InputStream in = new java.net.URL(urlIcone).openStream();
            icone = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e){
            Log.i("Pas d'icone", "subsitut utilisé à la place");
        } catch (IOException e){
            Log.i("Pas d'icone", "subsitut utilisé à la place");
        }

        return icone;
    }
}
