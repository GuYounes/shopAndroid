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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iut.guarssif3u.boutique.DAO.ArticleDAO;
import com.iut.guarssif3u.boutique.DAO.PromotionDAO;
import com.iut.guarssif3u.boutique.HTTPRequest.HTTPRequestMethod;
import com.iut.guarssif3u.boutique.fragment.ActiviteEnAttenteAvecResultat;
import com.iut.guarssif3u.boutique.modele.metier.Article;
import com.iut.guarssif3u.boutique.modele.metier.Promotion;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManagePromotionActivity extends AppCompatActivity implements ActiviteEnAttenteAvecResultat, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    protected TextView lblPromotion;

    protected Button btnOk;
    protected Button btnRetour;

    protected EditText pourcentage;
    protected EditText dateDebut;
    protected EditText dateFin;
    protected ProgressBar loader;
    protected ProgressBar loaderList;
    protected Spinner spinnerArticle;

    private ArrayList<Integer> minimumDate;
    private String method;
    private Promotion newPromotion;
    private Calendar calendar;
    protected int currentEditTextId;
    private int currentYear;
    private int currentMonth;
    private int currentDay;

    protected ArrayList articles;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_promotion);

        if (savedInstanceState != null) {
            articles = savedInstanceState.getStringArrayList("articlesArray");
        } else {
            articles = new ArrayList();
        }

        this.calendar = Calendar.getInstance();
        this.currentYear = calendar.get(Calendar.YEAR);
        this.currentMonth = calendar.get(Calendar.MONTH);
        this.currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        this.minimumDate = new ArrayList<>(3);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("articlesArray", articles);
    }

    @Override
    protected void onStart() {
        super.onStart();

        lblPromotion = this.findViewById(R.id.labelPromotion);
        btnOk = this.findViewById(R.id.btnOkPromotion);
        btnRetour = this.findViewById(R.id.btnRetour);
        spinnerArticle = this.findViewById(R.id.articleSpinner);
        dateDebut = this.findViewById(R.id.dateDebut);
        dateFin = this.findViewById(R.id.dateFin);
        pourcentage = this.findViewById(R.id.editPourcentage);
        loader = this.findViewById(R.id.loader);
        loaderList = this.findViewById(R.id.loaderlist);

        // request to get spinner items
        if(articles.isEmpty()) {
            this.afficheLoaderListe();
            ArticleDAO articleDAO = ArticleDAO.getInstance(this);
            articleDAO.findAll();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.articles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArticle.setAdapter(adapter);
            this.cacheLoaderAfficheListe();
        }

        // get method PUT or POST
        method = (String) this.getIntent().getExtras().get("method");

        btnOk.setOnClickListener(this);
        btnRetour.setOnClickListener(this);

        // set calendar pop up for date debut
        dateDebut.setOnClickListener(this);

        // set calendar pop up for date fin
        dateFin.setOnClickListener(this);

        // changing layout if request equals PUT and filling inputs with selected item info
        if(method.equals(HTTPRequestMethod.PUT)) {
            Promotion promotion = (Promotion) this.getIntent().getExtras().get("promotion");

            spinnerArticle.setEnabled(false);
            dateDebut.setEnabled(false);

            if(promotion != null) {
                lblPromotion.setText(R.string.modif_promotion);

                Calendar calendar = Calendar.getInstance();

                Date edtDateDebut = promotion.getDate_debut();
                Date edtDateFin = promotion.getDate_fin();

                // set date for date debut
                calendar.setTime(edtDateDebut);
                dateDebut.setText(String.format("%1$d/%2$d/%3$d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR)));

                // set date for date fin
                calendar.setTime(edtDateFin);
                dateFin.setText(String.format("%1$d/%2$d/%3$d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR)));

                pourcentage.setText(String.valueOf(promotion.getPourcentage()));

                btnOk.setText(R.string.modifier);
                newPromotion = promotion;
            }
        }

    }

    public void ajouterPromotion() {
        Article article = (Article) spinnerArticle.getSelectedItem();
        Date newDateDebut = null, newDateFin = null;
        int newPourcentage = 0;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if(!pourcentage.getText().toString().equals("")) {
            newPourcentage = Integer.valueOf(pourcentage.getText().toString());
        }

        try {
            newDateDebut = formatter.parse(dateDebut.getText().toString());
            newDateFin = formatter.parse(dateFin.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
        }

        try {
            // ajout catégorie
            Promotion newPromotion = new Promotion(article, newDateDebut, newDateFin, newPourcentage);
            PromotionDAO.getInstance(this).insert(newPromotion);
            this.afficheLoader();
        } catch (IllegalArgumentException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void editPromotion() {
        Article article = (Article) spinnerArticle.getSelectedItem();
        Date newDateDebut = null, newDateFin = null;
        int newPourcentage = 0;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if(!pourcentage.getText().toString().equals("")) {
            newPourcentage = Integer.valueOf(pourcentage.getText().toString());
        }

        try {
            newDateDebut = formatter.parse(dateDebut.getText().toString());
            newDateFin = formatter.parse(dateFin.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            // ajout catégorie
            Promotion newPromotion = new Promotion(article, newDateDebut, newDateFin, newPourcentage);
            PromotionDAO.getInstance(this).update(newPromotion);
            this.afficheLoader();
        } catch (IllegalArgumentException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.dateDebut):
                // date picker dialog
                datePickerDialog = new DatePickerDialog(view.getContext(), this, currentYear, currentMonth, currentDay);
                datePickerDialog.getDatePicker().setMinDate(this.calendar.getTimeInMillis());
                datePickerDialog.show();
                this.currentEditTextId = R.id.dateDebut;
                break;
            case(R.id.dateFin):
                Calendar calendar = Calendar.getInstance();

                if(this.minimumDate.size() == 0){
                    calendar = this.calendar;
                    datePickerDialog = new DatePickerDialog(view.getContext(), this, this.currentYear, this.currentMonth, this.currentDay);
                } else {
                    calendar.set(this.minimumDate.get(2), this.minimumDate.get(1)-1, this.minimumDate.get(0));
                    datePickerDialog = new DatePickerDialog(view.getContext(), this, this.minimumDate.get(2), this.minimumDate.get(1)-1, this.minimumDate.get(0));
                }

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
                this.currentEditTextId = R.id.dateFin;
                break;
            case (R.id.btnOkPromotion):
                if(method.equals(HTTPRequestMethod.POST)) ajouterPromotion();
                if(method.equals(HTTPRequestMethod.PUT)) editPromotion();
                break;
            case(R.id.btnRetour):
                this.finish();
                break;
        }
    }

    @Override
    public void afficheLoaderListe() {
        this.spinnerArticle.setVisibility(View.INVISIBLE);
        this.loaderList.setVisibility(View.VISIBLE);
        btnOk.setEnabled(false);
    }

    @Override
    public void cacheLoaderAfficheListe() {
        this.spinnerArticle.setVisibility(View.VISIBLE);
        this.loaderList.setVisibility(View.INVISIBLE);
        btnOk.setEnabled(true);
    }

    @Override
    public void afficheLoader() {
        this.btnOk.setVisibility(View.GONE);
        this.loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void cacheLoaderAfficheContenu() {
        this.loader.setVisibility(View.GONE);
        this.btnOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyRetourRequete(Object resultat, String method, boolean error) {
        this.cacheLoaderAfficheContenu();
        if(error){
            Toast.makeText(this, R.string.erreur_serveur, Toast.LENGTH_LONG).show();
            return;
        }
        switch (method){
            case HTTPRequestMethod.POST:
                Toast.makeText(this, R.string.ajout_ok, Toast.LENGTH_LONG).show();
                this.pourcentage.setText("");
                this.dateDebut.setText("");
                this.dateFin.setText("");
                break;
            case HTTPRequestMethod.PUT:
                Toast.makeText(this, R.string.modif_ok, Toast.LENGTH_LONG).show();
                this.finish();
                break;
        }
    }

    @Override
    public void notifyRetourRequeteFindAll(ArrayList list) {
        this.cacheLoaderAfficheListe();
        this.articles.clear();
        this.articles.addAll(list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.articles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticle.setAdapter(adapter);
        if(method.equals(HTTPRequestMethod.PUT)) {
            int spinnerPosition = list.indexOf(newPromotion.getArticle());
            spinnerArticle.setSelection(spinnerPosition, false);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(this.currentEditTextId == R.id.dateDebut){
            dateDebut.setText(String.format("%1$d/%2$d/%3$d", dayOfMonth, month+1, year));
            this.minimumDate.clear();
            this.minimumDate.add(dayOfMonth);
            this.minimumDate.add(month+1);
            this.minimumDate.add(year);
        }
        if(this.currentEditTextId == R.id.dateFin) dateFin.setText(String.format("%1$d/%2$d/%3$d", dayOfMonth, month+1, year));
    }
}
