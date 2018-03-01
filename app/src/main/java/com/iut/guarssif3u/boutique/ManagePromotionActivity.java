package com.iut.guarssif3u.boutique;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
import com.iut.guarssif3u.boutique.DAO.PromotionDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Article;
import com.iut.guarssif3u.boutique.modele.metier.Promotion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManagePromotionActivity extends AppCompatActivity implements ActiviteEnAttenteAvecResultat, View.OnClickListener {

    protected TextView lblPromotion;
    protected Button btnOk;
    protected Button btnRetour;
    protected EditText pourcentage;
    protected EditText dateDebut;
    protected EditText dateFin;
    protected Spinner spinnerArticle;
    private String method;
    private Promotion newPromotion;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_promotion);
    }

    @Override
    protected void onStart() {
        super.onStart();

        lblPromotion = this.findViewById(R.id.labelPromotion);
        btnOk = this.findViewById(R.id.btnOkPromotion);
        btnRetour = this.findViewById(R.id.btnRetour);
        spinnerArticle = (Spinner) this.findViewById(R.id.articleSpinner);
        dateDebut = (EditText) this.findViewById(R.id.dateDebut);
        dateFin = (EditText) this.findViewById(R.id.dateFin);
        pourcentage = (EditText) this.findViewById(R.id.editPourcentage);

        // get method PUT or POST
        method = (String) this.getIntent().getExtras().get("method");

        // set btn click
        btnOk.setOnClickListener(this);

        // request to get spinner items
        ArticleDAO articleDAO = ArticleDAO.getInstance(this);
        articleDAO.findAll();

        // set calendar pop up for date debut
        dateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH);
                int currentDay = c.get(Calendar.DAY_OF_MONTH);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateDebut.setText(day + "/"
                                + (month + 1) + "/" + year);
                    }
                }, currentYear, currentMonth, currentDay);

                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        // set calendar pop up for date fin
        dateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH);
                int currentDay = c.get(Calendar.DAY_OF_MONTH);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateFin.setText(day + "/"
                                + (month + 1) + "/" + year);
                    }
                }, currentYear, currentMonth, currentDay);

                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        // changing layout if request equals PUT and filling inputs with selected item info
        if(method.equals(HTTPRequestMethod.PUT)) {
            Promotion promotion = (Promotion) this.getIntent().getExtras().get("promotion");
            if(promotion != null) {
                lblPromotion.setText("Modification promotion");

                Calendar calendar = Calendar.getInstance();

                Date edtDateDebut = promotion.getDate_debut();
                Date edtDateFin = promotion.getDate_fin();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // set date for date debut
                calendar.setTime(edtDateDebut);
                dateDebut.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

                // set date for date fin
                calendar.setTime(edtDateFin);
                dateFin.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

                pourcentage.setText(String.valueOf(promotion.getPourcentage()));

                btnOk.setText("Modifier");
                newPromotion = promotion;
            }
        }

    }

    public void ajouterPromotion() {
        Article article = (Article) spinnerArticle.getSelectedItem();
        Date newDateDebut = null, newDateFin = null;
        Float newPourcentage = null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if(!pourcentage.getText().toString().equals("")) {
            newPourcentage = Float.valueOf(pourcentage.getText().toString());
        }

        try {
            String date_debut = (dateDebut.getText().toString());
            String date_fin = (dateFin.getText().toString());
            newDateDebut = formatter.parse(date_debut);
            newDateFin = formatter.parse(date_fin);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(article != null && newDateDebut != null && newDateFin != null && newPourcentage > 0 && newPourcentage < 1) {
            // ajout catégorie
            Promotion newPromotion = new Promotion(article, newDateDebut, newDateFin, newPourcentage);
            PromotionDAO.getInstance(this).insert(newPromotion);
            Toast.makeText(this,"Ajout", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Les champs saisis sont incorrects !", Toast.LENGTH_LONG).show();
        }
    }

    public void editPromotion() {
        Article article = (Article) spinnerArticle.getSelectedItem();
        Date newDateDebut = null, newDateFin = null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Float newPourcentage = null;

        if(!pourcentage.getText().toString().equals("")) {
            newPourcentage = Float.valueOf(pourcentage.getText().toString());
        }

        try {
            String date_debut = (dateDebut.getText().toString());
            String date_fin = (dateFin.getText().toString());
            newDateDebut = formatter.parse(date_debut);
            newDateFin = formatter.parse(date_fin);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(article != null && newDateDebut != null && newDateFin != null && newPourcentage > 0) {
            Promotion newPromotion = new Promotion(article, newDateDebut, newDateFin, newPourcentage);
                PromotionDAO.getInstance(this).update(newPromotion);
                Toast.makeText(this,"Modification", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Les champs saisis sont incorrects !", Toast.LENGTH_LONG).show();
        }
    }

    public void retour(View view) {
        this.finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.dateDebut) {

        }
        if(method.equals(HTTPRequestMethod.POST)) {
            ajouterPromotion();
        } else if(method.equals(HTTPRequestMethod.PUT)) {
            editPromotion();
        }
    }

    @Override
    public void afficheLoader() {

    }

    @Override
    public void cacheLoaderAfficheContenu() {

    }

    @Override
    public void notifyRetourRequete(Object resultat, String method, boolean error) {

    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticle.setAdapter(adapter);
        if(method.equals(HTTPRequestMethod.PUT)) {
            int spinnerPosition = list.indexOf(newPromotion.getArticle());
            spinnerArticle.setSelection(spinnerPosition, false);
        }
    }
}
