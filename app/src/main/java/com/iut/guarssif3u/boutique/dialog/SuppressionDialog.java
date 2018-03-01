package com.iut.guarssif3u.boutique.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.iut.guarssif3u.boutique.BoutiqueActivity;
import com.iut.guarssif3u.boutique.R;


/**
 * Created by younes on 27/02/2018.
 */

public class SuppressionDialog extends DialogFragment {

    int parentPosition;
    String titre;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentPosition = getArguments().getInt("position");
        this.titre = getArguments().getString("titre");
        this.message = getArguments().getString("message");
    }

    public static SuppressionDialog newInstance(int position, String titre, String message) {
        SuppressionDialog fragment = new SuppressionDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("titre", titre);
        args.putString("message", message);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Fragment parent = ((BoutiqueActivity)getActivity()).getViewPagerAdapter().getItem(this.parentPosition);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(this.message)
                .setTitle(this.titre);

        DialogInterface.OnClickListener ecouteur = (DialogInterface.OnClickListener)parent;
        builder.setPositiveButton(R.string.positif, ecouteur);
        builder.setNegativeButton(R.string.negatif, ecouteur);

        return builder.create();
    }
}
