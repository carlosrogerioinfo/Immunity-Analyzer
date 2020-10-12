package com.life.medicineandnutrition.smartdlitebr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import java.util.regex.Matcher;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.regex.Pattern;
import android.view.Gravity;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;


public class CalciumUrineCalculateActivity extends AppCompatActivity {

    BarChart barChart;

    private int TIME_SPLASH = 1000; //7000
    private int status = 0;
    DecimalFormat precision = new DecimalFormat("0.00");
    private ColorStateList originalColorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcium_urine_calculate);

        CheckTermsAccept();

        ConfigureToolBar();
        ConfigureFieldsNumberAndDecimal();
        SetVisibilityOnResultFields(View.GONE);

        TextView textResult = findViewById(R.id.textResultCalciumUrine);
        originalColorText = textResult.getTextColors();

        ClearAll(null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == 16908332){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }

    public void Calculate(View v){

        //Esta rotina foi removida, pois é de propriedade intelectual.

        SetVisibilityOnResultFields(View.GONE);
        SetVisibilityOnResultPositive(View.GONE);
        SetVisibilityOnResultNegativeUp(View.GONE);
        SetVisibilityOnResultNegativeDown(View.GONE);

        StartProcess();
    }

    private void SetFocusCalcium() {
        TextInputEditText tit = findViewById(R.id.textCalcium);
        tit.setFocusable(true);
        tit.requestFocus();
    }

    private void SetNoFocus() {
        TextInputEditText titCalcium = findViewById(R.id.textCalcium);
        TextInputEditText titVolume = findViewById(R.id.textVolume);

        titCalcium.setFocusable(true);
        titVolume.setFocusable(true);

        titCalcium.requestFocus();
        titVolume.requestFocus();
    }

    private void SetFocusVolume() {
        TextInputEditText tit = findViewById(R.id.textVolume);
        tit.setFocusable(true);
        tit.requestFocus();
    }

    public void ClearAll(View v){

        ((EditText) findViewById(R.id.textVolume)).setText("");
        ((EditText) findViewById(R.id.textCalcium)).setText("");

        TextView textResult = findViewById(R.id.textResultCalciumUrine);
        textResult.setTextColor(originalColorText);

        SetVisibilityOnResultFields(View.GONE);

        SetVisibilityOnResultPositive(View.GONE);
        SetVisibilityOnResultNegativeUp(View.GONE);
        SetVisibilityOnResultNegativeDown(View.GONE);

        textResult.setTextColor(originalColorText);
        SetFocusCalcium();
    }

    public void GoToMainActivity(View view){
        this.finish();
    }

    public void GoToInfoActivity(View view){
        Intent intent = new Intent(CalciumUrineCalculateActivity.this, InfoActivity.class);
        intent.putExtra("parameter", "calcium24h");
        startActivity(intent);
    }

    private void ConfigureFieldsNumberAndDecimal() {
        TextInputEditText textInputKg = findViewById(R.id.textCalcium);
        textInputKg.setFilters(new InputFilter[] {new CalciumUrineCalculateActivity.DecimalDigitsInputFilter(5,2)});

        TextInputEditText textInputAw = findViewById(R.id.textVolume);
        textInputAw.setFilters(new InputFilter[] {new CalciumUrineCalculateActivity.DecimalDigitsInputFilter(5,2)});
    }

    private void SetVisibilityOnResultFields(int view) {
        TableLayout containerResult = findViewById(R.id.containerInformation);
        containerResult.setVisibility(view);

        TableLayout containerGraphic = findViewById(R.id.containerGraphic);
        containerGraphic.setVisibility(view);

    }

    private void SetVisibilityOnResultPositive(int view) {
        TableLayout containerResult = findViewById(R.id.containerResultPositive);
        containerResult.setVisibility(view);
    }

    private void SetVisibilityOnResultNegativeUp(int view) {
        TableLayout containerResult = findViewById(R.id.containerResultNegativeUp);
        containerResult.setVisibility(view);
    }

    private void SetVisibilityOnResultNegativeDown(int view) {
        TableLayout containerResult = findViewById(R.id.containerResultNegativeDown);
        containerResult.setVisibility(view);
    }

    private void SetVisibilityProgress(int view) {
        ProgressBar progress = findViewById(R.id.pgProcess);
        progress.setVisibility(view);
    }

    private void ConfigureToolBar() {
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ShowSingleDialog(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(getString(R.string.urine24h_single_dialog_title))
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

    private void ShowToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void StartProcess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SetVisibilityOnResultFields(View.VISIBLE);
                if (status == 0) {
                    SetVisibilityOnResultPositive(View.VISIBLE);
                    SetVisibilityOnResultNegativeUp(View.GONE);
                    SetVisibilityOnResultNegativeDown(View.GONE);
                } else if (status == -1) {
                    SetVisibilityOnResultPositive(View.GONE);
                    SetVisibilityOnResultNegativeUp(View.GONE);
                    SetVisibilityOnResultNegativeDown(View.VISIBLE);
                } else if (status == 1) {
                    SetVisibilityOnResultPositive(View.GONE);
                    SetVisibilityOnResultNegativeUp(View.VISIBLE);
                    SetVisibilityOnResultNegativeDown(View.GONE);
                }

                SetVisibilityProgress(View.GONE);

            }
        },TIME_SPLASH);
    }

    public Integer ReadInfo(View v) {

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Integer info = myPreferences.getInt("AcceptTerms", -1);

        return info;
    }

    private void CheckTermsAccept() {

        Integer checkTerms = ReadInfo(null);
        if (checkTerms.equals(-1) || checkTerms.equals(0)) {
            ShowToast(getString(R.string.general_single_dialog_legal_advice_required));
            this.finish();

        } /*else {
            OnlyProVersion();
        }
        */
    }

    private void OnlyProVersion() {
        ShowToast(getString(R.string.general_single_dialog_only_pro_version));
        this.finish();
    }

    private void loadGraphics(Double valueCalciumPerLiters, Double valueCalcium) {

        TextView textLabelGraphicMsg = findViewById(R.id.textLabelGraphicMsg);
        textLabelGraphicMsg.setText(getString(R.string.urine24h_graphic_result) + String.valueOf(precision.format(valueCalciumPerLiters)) + " mg/L");

        barChart = (BarChart) findViewById(R.id.barChart);

        List<BarEntry> barCalcium = new ArrayList<>();
        List<BarEntry> barCalciumPerLiters = new ArrayList<>();

        barCalcium.add(new BarEntry(0f, valueCalcium.floatValue()));
        barCalciumPerLiters.add(new BarEntry(1f, valueCalciumPerLiters.floatValue()));

        BarDataSet dataSetCalcium = new BarDataSet(barCalcium, getString(R.string.urine24h_graphic_calcium_urine));
        BarDataSet dataSetCalciumPerLiters = new BarDataSet(barCalciumPerLiters, getString(R.string.urine24h_graphic_relation_calcium));

        dataSetCalcium.setColors(new int[] {Color.parseColor("#4BBCE5")});

        if (valueCalciumPerLiters >= 100.0 && valueCalciumPerLiters <= 150.0) {
            dataSetCalciumPerLiters.setColors(new int[] {Color.parseColor("#9AF9A6")});
        }

        if (valueCalciumPerLiters < 100.0) {
            dataSetCalciumPerLiters.setColors(new int[] {Color.parseColor("#F9F100")});
        }

        if (valueCalciumPerLiters >= 151.0 && valueCalciumPerLiters <= 180.0) {
            dataSetCalciumPerLiters.setColors(new int[] {Color.parseColor("#FF9D00")});
        }

        if (valueCalciumPerLiters > 181.0) {
            dataSetCalciumPerLiters.setColors(new int[] {Color.parseColor("#FF0000")});
        }

        BarData barData = new BarData(dataSetCalcium);
        barData.addDataSet(dataSetCalciumPerLiters);
        barData.setBarWidth(0.5f); // set custom bar width
        barChart.setData(barData);

        //SETA O VALOR MINIMO E MAXIMO DO EIXO Y
        YAxis left = barChart.getAxisLeft();
        left.setAxisMaximum(valueCalcium.intValue()); //VALOR MAXIMO EIXO Y
        left.setAxisMinimum(0);

        //DESABILITA O EIXO X E DA DIREITA
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        //LEGENDA E DESCRIPTIONLABEL
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        barChart.getLegend().setTextSize(12f);
        barChart.getLegend().setYOffset(10f);

        barChart.setFitBars(true);
        barChart.invalidate();

    }


}
