package com.life.medicineandnutrition.smartdlitebr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import java.util.Calendar;
import java.util.UUID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.life.medicineandnutrition.functions.SecurityLibrary;

import com.life.medicineandnutrition.model.Activations;


public class ConfigurePartnerActivity extends AppCompatActivity {

    //UUID
    private String uniqueID = null;
    private final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private String documentId;
    private String secretKeyPartnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_partner);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ConfigurePartnerActivity.this);
        documentId = myPreferences.getString("documentid", "");

        if (!documentId.trim().isEmpty())
        {
            ShowSingleDialog("Entrou no empty");
            ReadSingleContact();

        } else {
            ShowSingleDialog("Entrou na gravação do banco");
            secretKeyPartnerId = GetPhoneId().toUpperCase();
            RegisterSecretKeyOnFirebase(USER_FIREBASE, PASSWORD_FIREBASE);
        }

    }

    private void RegisterSecretKeyOnFirebase(String email, String password) {

        SetVisibilityProgress(View.VISIBLE);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            SavePartnerActivatedKeyInFirebase();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ShowSingleDialog("Ocorreu um erro na criação do orçamento. Verifique se está conectado a internet.");
                    }
                });

    }

    private void SavePartnerActivatedKeyInFirebase() {

        try {
            String TableNameDocument = "partnersactives";// Deve alterar sempre quando trocar de cadastro

            Activations activations = new Activations(
                    secretKeyPartnerId,
                    "",
                    false
            );

            FirebaseFirestore.getInstance().collection(TableNameDocument)
                    .add(activations)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            DocumentReference partners = FirebaseFirestore.getInstance().collection("partnersactives").document(documentReference.getId());
                            documentId = documentReference.getId(); //Seto o valor retornado na variável global

                            partners.update("documentid", documentReference.getId())
                                    .addOnSuccessListener(new OnSuccessListener < Void > () {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            SetVisibilityProgress(View.GONE);
                                            SetVisibilityButton(View.VISIBLE);
                                            WriteSecretKeyPartner(documentId);
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finish();
                        }
                    });
        } catch (Exception e) {

        }

    }

    private void SetValueDocumentId(String value) {

        EditText textDocumentId = findViewById(R.id.textDocumentId);
        textDocumentId.setText(value);

    }

    private void WriteSecretKeyPartner(String value) {

        SetValueDocumentId(value);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ConfigurePartnerActivity.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("documentid", value);

        myEditor.commit();

    }

    private void ReadSingleContact() {

        FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference user = firebaseFirestore.collection("partnersactives").document(documentId);
        user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult(); //Obtem o resultado da consulta pelo id

                    if (Boolean.valueOf(doc.get("activated").toString())) {
                        ShowSingleDialog("Sistema habilitado e ativado");
                    }
                    else
                    {
                        ShowSingleDialog("Sistema não ativado");
                        SetValueDocumentId(doc.get("documentid").toString());
                        SetVisibilityButton(View.VISIBLE);
                    }

                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    private void ReadSingleContact(String documentId) {

        FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference partners = firebaseFirestore.collection("partnersactives").document(documentId);
        partners.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    ShowSingleDialog("Aguarde ativação");
                    DocumentSnapshot doc = task.getResult();
                    CheckStatus(Boolean.valueOf(doc.get("active").toString()));
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void CheckStatus(Boolean value) {
        if (value == false) {
            ShowSingleDialog("Aguarde ativação");
        }
    }

    private void SetVisibilityProgress(int view) {
        ProgressBar progress = findViewById(R.id.pgProcess);
        progress.setVisibility(view);
    }

    private void SetVisibilityButton(int view) {
        TableLayout containerButton = findViewById(R.id.containerButton);
        containerButton.setVisibility(view);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void SendWhatsApp(View v) {

        EditText textDocumentId = findViewById(R.id.textDocumentId);
        OpenWhatsApp("Solicito ativação do App registro número:\n\n\n" +
                textDocumentId.getText().toString(),
                "+18622152961");
    }

    public void OpenWhatsApp(String msg, String phoneNumber) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(
                    Uri.parse("http://api.whatsapp.com/send?phone=" +
                            phoneNumber + "&text=" +
                            msg));

            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String GetPhoneId () {
        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public synchronized String GetUniqueId (Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }    return uniqueID.toUpperCase();
    }

    public String GeneratePartnerSecretId() {

        String secretKey;

        try {

            secretKey = SecurityLibrary.Encrypt(Calendar.getInstance().getTime().toString()) +
                    GetPhoneId() +
                    GetUniqueId(ConfigurePartnerActivity.this) +
                    SecurityLibrary.Encrypt(Calendar.getInstance().getTime().toString());

            return secretKey.toUpperCase().replace("-", "");

        } catch (Exception e) {
            ShowSingleDialog("Error PSUtils: Contate o suporte técnico. " + e.getMessage().toString());
            return null;
        }

    }

    private void ShowSingleDialog(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Smart D3 | Informação")
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Set something
                    }

                })
                .show();
    }


}
