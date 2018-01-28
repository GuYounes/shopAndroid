package com.iut.guarssif3u.boutique.async;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.CategorieAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by younes on 13/01/2018.
 */

public class ImageFromURL extends AsyncTask<String, Integer, Bitmap> {

    protected CategorieAdapter categorieAdapter;
    protected ImageView imageView;
    protected Drawable substitut;
    protected ProgressBar loader;

    public ImageFromURL(CategorieAdapter categorieAdapter, ImageView imageView, Drawable substitut, ProgressBar loader){
        this.categorieAdapter = categorieAdapter;
        this.imageView = imageView;
        this.substitut = substitut;
        this.loader = loader;
    }

    @Override
    protected void onPreExecute(){
        this.afficheLoad();
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

        this.cacheLoadDisplayImage();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlIcone = urls[0];
        Bitmap icone = null;

        try{
            InputStream in = new java.net.URL(urlIcone).openConnection().getInputStream();
            icone = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e){
            Log.i("Pas d'icone", "subsitut utilisé à la place");
        } catch (IOException e){
            Log.i("Pas d'icone", "subsitut utilisé à la place");
        }

        return icone;
    }

    public void afficheLoad(){
        this.loader.setVisibility(View.VISIBLE);
    }

    public void cacheLoadDisplayImage(){
        this.loader.setVisibility(View.GONE);
        this.imageView.setVisibility(View.VISIBLE);
    }
}
