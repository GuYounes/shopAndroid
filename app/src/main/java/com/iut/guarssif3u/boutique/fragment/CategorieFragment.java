package com.iut.guarssif3u.boutique.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iut.guarssif3u.boutique.R;
import com.iut.guarssif3u.boutique.adapter.CategorieAdapter;
import com.iut.guarssif3u.boutique.modele.metier.Categorie;

import java.util.ArrayList;

/**
 * Created by younes on 12/01/2018.
 */

public class CategorieFragment extends Fragment {

    protected ArrayList<Categorie> categories;

    protected ListView listView;

    public CategorieFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_categorie, null);

        CategorieAdapter categorieAdapter = new CategorieAdapter(getActivity(), categories);

        listView = (ListView) view.findViewById(R.id.liste);
        listView.setAdapter(categorieAdapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Bundle args = this.getArguments();
        this.categories = args.getParcelableArrayList("categories");
    }
}
