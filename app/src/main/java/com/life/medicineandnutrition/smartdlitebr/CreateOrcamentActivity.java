package com.life.medicineandnutrition.smartdlitebr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;


import com.life.medicineandnutrition.functions.DateTimeLibrary;
import com.life.medicineandnutrition.functions.InstantMessageLibrary;
import com.life.medicineandnutrition.functions.LocalStorageLibrary;

import com.life.medicineandnutrition.functions.SharedKernelLibrary;
import com.life.medicineandnutrition.model.Orcament;

public class CreateOrcamentActivity extends AppCompatActivity {

    private Button btnOrcament;
    private Button btnClose;

    private EditText textName;
    private EditText textEmail;
    private EditText textContact;

    private TextView textPharmacy;
    private TextView textDosageD3;
    private TextView textDosageK2;
    private TextView textDosageMg;
    private TextView textPeriod;
    private TextView textDosageD3Manteining;
    private TextView textDosageK2Manteining;
    private TextView textDosageMgManteining;
    private TextView textHourIni;
    private TextView textHourEnd;

    private String uuid;
    private String dosaged3;
    private String dosagek2;
    private String dosagemg;
    private String name;
    private String email;
    private String contact;
    private String pharmacyname;
    private String dateorcament;
    private String period;
    private String dosaged3manteining;
    private String dosagek2manteining;
    private String timeinit;
    private String timefinal;
    private String weight;

    private Boolean HasProcessing = false;

    private int TIME_SPLASH = 3000; //7000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_orcament);

        //InitializeComponentsAndFields();
        InitializeComponents();
        ConfigureToolBar();
        GetParametersIntent();

    }

    private void RegisterOrcament(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            SaveOrcamentInFirebase();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        InstantMessageLibrary.ShowSingleDialog(
                                getString(R.string.general_single_dialog_title),
                                "Ocorreu um erro na criação do orçamento. Verifique se está conectado a internet.",
                                CreateOrcamentActivity.this);
                    }
                });

    }

    private void SaveOrcamentInFirebase() {

        String TableNameDocument = "orcaments";//Deve alterar sempre quando trocar de cadastro

        uuid = FirebaseAuth.getInstance().getUid();
        Orcament orcament = new Orcament(
                uuid,
                dosaged3,
                dosagek2,
                dosagemg,
                name,
                email,
                contact,
                pharmacyname,
                dateorcament,
                period,
                dosaged3manteining,
                dosagek2manteining,
                timeinit,
                timefinal,
                weight
        );

        FirebaseFirestore.getInstance().collection(TableNameDocument)
                .add(orcament)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        SetVisibilityButtonClose(View.VISIBLE);
                        SetVisibilityButtonOrcament(View.GONE);
                        ShowCustomizedDialog("Pedido de Orçamento",
                                "Seu pedido de orçamento foi enviado com sucesso para nossa central! \n\nEm até 24h, um dos nossos profissionais prescritores entrará em contato para avaliar e solicitar seu pedido.",
                                true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        HasProcessing = false;
                        SetVisibilityButtonClose(View.GONE);
                        SetVisibilityButtonOrcament(View.VISIBLE);
                        ShowCustomizedDialog("Orçamento", "Ocorreu um erro na criação do orçamento. Verifique se está conectado a internet.", false);

                    }
                });

    }

    public void StartProcess() {

        if (!SharedKernelLibrary.HasInternetConnection(this)){
            InstantMessageLibrary.ShowSingleDialog(
                    getString(R.string.general_single_dialog_title),
                    "Sem conexão | Para solicitar o orçamento é necessário estar conectado à internet.",
                    CreateOrcamentActivity.this);
            return;
        }

        if (!FieldsValidate()) {
            return;
        }

        HasProcessing = true;
        SetVisibilityProgress(View.VISIBLE);
        SetVisibilityOrcament(View.GONE);
        RegisterOrcament(Constants.USER_FIREBASE, Constants.PASSWORD_FIREBASE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SetVisibilityProgress(View.GONE);
                HasProcessing = false;

            }
        },TIME_SPLASH);
    }

    private void SetVisibilityProgress(int view) {
        ProgressBar progress = findViewById(R.id.pgProcess);
        progress.setVisibility(view);
    }

    private void SetVisibilityOrcament(int view) {
        TableLayout containerButtonOrcament = findViewById(R.id.containerButtonOrcament);
        containerButtonOrcament.setVisibility(view);
    }

    private Boolean FieldsValidate() {

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textContact = findViewById(R.id.textContact);
        textHourIni = findViewById(R.id.textHourIni);
        textHourEnd = findViewById(R.id.textHourEnd);

        if (textName.getText().toString().isEmpty() ||
                textEmail.getText().toString().isEmpty() ||
                textContact.getText().toString().isEmpty() ||
                textDosageD3.getText().toString().isEmpty() ||
                textDosageK2.getText().toString().isEmpty() ||
                textDosageMg.getText().toString().isEmpty() ||
                textHourIni.getText().toString().isEmpty() ||
                textHourEnd.getText().toString().isEmpty()) {

            InstantMessageLibrary.ShowSingleDialog(
                    getString(R.string.general_single_dialog_title),
                    "Todos os campos devem ser preenchidos. Verifique se as informações de dosagens das Vitaminas e minerais vieram preenchidos.",
                    CreateOrcamentActivity.this);

            return false;
        }

        if (SharedKernelLibrary.CountWords(textName.getText().toString()) <= 1) {

            InstantMessageLibrary.ShowSingleDialog(
                    getString(R.string.general_single_dialog_title),
                    "Por favor, informe o seu Nome e Sobrenome.",
                    CreateOrcamentActivity.this);

            return false;

        }

        name = textName.getText().toString();
        email = textEmail.getText().toString();
        contact = textContact.getText().toString();
        timeinit = textHourIni.getText().toString();
        timefinal = textHourEnd.getText().toString();
        dateorcament = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        return true;

    }

    private void GetParametersIntent() {

        Intent it = getIntent();

        dosaged3 = it.getStringExtra("dosaged3");
        dosagek2 = it.getStringExtra("dosagek2");
        dosagemg = it.getStringExtra("dosagemg");
        pharmacyname = it.getStringExtra("pharmacyname");
        weight = it.getStringExtra("weight");

        period = it.getStringExtra("period");
        dosaged3manteining = it.getStringExtra("dosaged3manteining");
        dosagek2manteining = it.getStringExtra("dosagek2manteining");

        textDosageD3.setText(dosaged3);
        textDosageK2.setText(dosagek2);
        textDosageMg.setText(dosagemg);
        textPharmacy.setText(pharmacyname);

        textPeriod.setText(period);
        textDosageD3Manteining.setText(dosaged3manteining);
        textDosageK2Manteining.setText(dosagek2manteining);
        textDosageMgManteining.setText(dosagemg);

    }

    @Override
    public void onBackPressed() {

        if (!HasProcessing) {
            super.onBackPressed();
        }
    }

    //BACK BUTTON HEADER TITLE
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == 16908332 && !HasProcessing){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ConfigureToolBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void InitializeComponents() {

        textPharmacy = findViewById(R.id.textPharmacy);
        textDosageD3 = findViewById(R.id.textDosageD3);
        textDosageK2 = findViewById(R.id.textDosageK2);
        textDosageMg = findViewById(R.id.textDosageMg);

        textPeriod = findViewById(R.id.textPeriod);
        textDosageD3Manteining = findViewById(R.id.textDosageD3Manteining);
        textDosageK2Manteining = findViewById(R.id.textDosageK2Manteining);
        textDosageMgManteining = findViewById(R.id.textDosageMgManteining);

        btnOrcament = findViewById(R.id.btnOrcament);
        btnClose = findViewById(R.id.btnClose);

        SetEventsOnClickComponents();

    }

    private void SetEventsOnClickComponents() {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseWindow();
            }
        });
        btnOrcament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateOrcament();
            }
        });

    }

    private void CloseWindow() {
        this.finish();
    }

    private void ShowCustomizedDialog(String title, String msg, final Boolean closeWindow) {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK, ENTENDI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (closeWindow) {
                            CloseWindow();
                        }
                    }

                })
                .show();
    }

    private void SetVisibilityButtonOrcament(int view) {

        TableLayout containerButtonOrcament = findViewById(R.id.containerButtonOrcament);
        containerButtonOrcament.setVisibility(view);

    }

    private void SetVisibilityButtonClose(int view) {

        TableLayout containerButtonClose = findViewById(R.id.containerButtonClose);
        containerButtonClose.setVisibility(view);

    }

    //CALCULO DE DATAS /DIAS
    private Boolean ControlOrcamentGenerator() {

        Boolean result = false;

        Integer countOrcament = 0;
        Integer qtd = 0;

        countOrcament = LocalStorageLibrary.ReadIntegerKeyValue(
                Constants.PARAM_LS_QTD_ORCAMENT,
                CreateOrcamentActivity.this);

        if (countOrcament <= 0) {
            qtd++;

        } else {
            qtd = countOrcament;
            qtd++;
        }

        if (qtd <= Constants.PARAM_LS_MAX_QTD_ORCAMENT) {

            LocalStorageLibrary.WriteIntegerKeyValue(
                    Constants.PARAM_LS_QTD_ORCAMENT,
                    qtd,
                    CreateOrcamentActivity.this);

            result = true;

        } else {

            result = false;

            //EXIBE MENSAGEM DE ADVERTENCIA
            InstantMessageLibrary.ShowCustomizedDialog(
                    "Solicitação de Orçamento",
                    getString(R.string.param_msg_qtd_orcament_exceded),
                    getString(R.string.general_label_btn_ok_understand),
                    CreateOrcamentActivity.this);

            //RESET DATE
            LocalStorageLibrary.WriteStringKeyValue(
                    Constants.PARAM_LS_DATE_ORCAMENT,
                    DateTimeLibrary.GetCurrentDateTimeFormat(),
                    CreateOrcamentActivity.this);

            //RESET QTD
            LocalStorageLibrary.WriteIntegerKeyValue(
                    Constants.PARAM_LS_QTD_ORCAMENT,
                    0,
                    CreateOrcamentActivity.this);

        }

        return result;

    }

    private void GenerateOrcament() {

        //GERA ORÇAMENTO SE NÃO HOUVER CHAVE GRAVADA NO LOCAL STORAGE
        if (LocalStorageLibrary.ReadStringKeyValue(
                Constants.PARAM_LS_DATE_ORCAMENT,
                CreateOrcamentActivity.this).isEmpty()) {

            if (ControlOrcamentGenerator()) {
                StartProcess();
            }

        } else {

            long days = DateTimeLibrary.DateTimeDiference(
                    LocalStorageLibrary.ReadStringKeyValue(
                            Constants.PARAM_LS_DATE_ORCAMENT,
                            CreateOrcamentActivity.this)
            );

            if (days >= Constants.PARAM_LS_MAX_DAYS_ORCAMENT) {

                if (ControlOrcamentGenerator()) {
                    StartProcess();
                }

            } else {

                InstantMessageLibrary.ShowCustomizedDialog(
                        "Solicitação de Orçamento",
                        getString(R.string.param_msg_qtd_orcament_exceded),
                        getString(R.string.general_label_btn_ok_understand),
                        CreateOrcamentActivity.this);
            }

        }

    }







}
