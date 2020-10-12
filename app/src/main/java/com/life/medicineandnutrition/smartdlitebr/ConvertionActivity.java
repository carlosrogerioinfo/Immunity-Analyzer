package com.life.medicineandnutrition.smartdlitebr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.life.medicineandnutrition.functions.InstantMessageLibrary;
import com.life.medicineandnutrition.functions.SharedKernelLibrary;

public class ConvertionActivity extends AppCompatActivity {

    private int TIME_SPLASH = 1000; //7000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertion);

        CheckTermsAccept();
        ConfigureToolBar();
    }

    private void CheckTermsAccept() {

        Integer checkTerms = ReadInfo(null);
        if (checkTerms.equals(-1) || checkTerms.equals(0)) {
            ShowToast(getString(R.string.general_single_dialog_legal_advice_required));
            this.finish();
        }
    }

    public Integer ReadInfo(View v) {

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Integer info = myPreferences.getInt("AcceptTerms", -1);

        return info;
    }

    private void ShowToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void ClearAll(View v){

        ((EditText) findViewById(R.id.textConvertionNmolLiter)).setText("");

        TextView textResultConvertion = findViewById(R.id.textResultConvertion);
        textResultConvertion.setText("");

        SetVisibilityOnResultFields(View.GONE);

    }

    public void ConvertToNMolL(View v){

        SetVisibilityProgress(View.VISIBLE);
        StartProcess();

        String value = ((EditText) findViewById(R.id.textConvertionNmolLiter)).getText().toString();
        value = value.replace(",", ".");

        if (value.isEmpty()) {
            InstantMessageLibrary.ShowSingleDialog(
                    getString(R.string.general_single_dialog_title),
                    getString(R.string.adult_single_dialog_required_fields),
                    ConvertionActivity.this);
            return;
        }

        double result = SharedKernelLibrary.ConvertToNgMl(Double.valueOf(value).doubleValue());

        TextView textResultConvertion = findViewById(R.id.textResultConvertion);
        textResultConvertion.setText(String.format("%.0f", result) + " ng/ml");

        SetVisibilityOnResultFields(View.GONE);
        SetVisibilityProgress(View.VISIBLE);
        StartProcess();
    }

    private void StartProcess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SetVisibilityOnResultFields(View.VISIBLE);
                SetVisibilityProgress(View.GONE);

            }
        },TIME_SPLASH);
    }

    private void SetVisibilityOnResultFields(int view) {
        TableLayout containerInformationConv = findViewById(R.id.containerInformationConv);
        containerInformationConv.setVisibility(view);


    }

    private void SetVisibilityProgress(int view) {
        ProgressBar progress = findViewById(R.id.pgProcess);
        progress.setVisibility(view);
    }

    private void ConfigureToolBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == 16908332){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }





}