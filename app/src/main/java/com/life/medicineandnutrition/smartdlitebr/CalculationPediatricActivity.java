package com.life.medicineandnutrition.smartdlitebr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

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
import android.widget.Switch;
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
import android.widget.CompoundButton;

//GRAFICOS
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
//GRAFICOS

import java.util.ArrayList;
import java.util.List;


public class CalculationPediatricActivity extends AppCompatActivity {

    BarChart barChart;

    private int TIME_SPLASH = 1000; //7000
    private int D3_UI = 40000;
    private int DOSAGE = 200;
    private int status = 0;
    DecimalFormat precision = new DecimalFormat("0.00");
    private ColorStateList originalColorText;
    private Switch swtImc;

    Double dailyDoseMg;
    Boolean useImc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_pediatric);

        CheckTermsAccept();

        ConfigureToolBar();
        ConfigureSwitchImc();
        ConfigureFieldsNumberAndDecimal();
        SetVisibilityOnResultFields(View.GONE);

        TextView textResult = findViewById(R.id.textResultDoseDiaPed);
        originalColorText = textResult.getTextColors();

        ClearAllNoFocus(null);

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

        if (useImc) {
            CalculateImc(null);
            return;
        }

        String textFieldPeso = ((EditText)findViewById(R.id.textPesoPed)).getText().toString();

        //Esta rotina foi removida, pois é de propriedade intelectual.

        SetVisibilityOnResultFields(View.GONE);

        StartProcess();
    }

    public void CalculateImc(View v){

        String textFieldHeight = ((EditText)findViewById(R.id.textHeight)).getText().toString();
        String textFieldPeso = ((EditText)findViewById(R.id.textPesoPed)).getText().toString();

        //Esta rotina foi removida, pois é de propriedade intelectual.

        SetVisibilityOnResultFields(View.GONE);

        StartProcess();
    }


    public void ShowResultInMiligramsPed(View view) {

        String msg;

        msg = getString(R.string.children_single_dialog_convertion)
              + String.valueOf(precision.format(dailyDoseMg)) + " mg";

        ShowSingleDialog(msg);
    }

    private Double ConvertToMiligrams(Double val) {
        return val / D3_UI;
    }

    private void SetFocusHeight() {
        TextInputEditText tit = findViewById(R.id.textHeight);
        tit.setFocusable(true);
        tit.requestFocus();
    }

    public void ClearAll(View v){

        ((EditText) findViewById(R.id.textHeight)).setText("");
        ((EditText) findViewById(R.id.textPesoPed)).setText("");

        TextView textResult = findViewById(R.id.textResultDoseDiaPed);
        textResult.setTextColor(originalColorText);

        SetVisibilityOnResultFields(View.GONE);
        SetFocusHeight();
    }

    public void ClearAllNoFocus(View v){

        ((EditText) findViewById(R.id.textHeight)).setText("");
        ((EditText) findViewById(R.id.textPesoPed)).setText("");

        TextView textResult = findViewById(R.id.textResultDoseDiaPed);
        textResult.setTextColor(originalColorText);

        SetVisibilityOnResultFields(View.GONE);
    }

    public void GoToMainActivity(View view){
        this.finish();
    }

    public void GoToInfoActivity(View view){
        Intent intent = new Intent(CalculationPediatricActivity.this, InfoActivity.class);
        intent.putExtra("parameter", "pediatric");
        startActivity(intent);
    }

    private void ConfigureFieldsNumberAndDecimal() {
        TextInputEditText textInputAw = findViewById(R.id.textPesoPed);
        textInputAw.setFilters(new InputFilter[] {new CalculationPediatricActivity.DecimalDigitsInputFilter(3,3)});
    }

    private void SetVisibilityOnResultFields(int view) {
        TableLayout containerResult = findViewById(R.id.containerInformationPed);
        containerResult.setVisibility(view);
    }

    private void SetVisibilityOnResultFieldsImc(int view) {

        TableLayout containerHeight = findViewById(R.id.containerHeight);
        containerHeight.setVisibility(view);

        ((EditText)findViewById(R.id.textHeight)).setText("");
    }

    private void SetVisibilityGraphic(int view) {

        TableLayout containerGraphic = findViewById(R.id.containerGraphic);
        containerGraphic.setVisibility(view);

        if (useImc) {
            EditText tit = findViewById(R.id.textHeight);
            tit.setFocusable(true);
            tit.requestFocus();

        }

    }

    private void SetVisibilityProgress(int view) {
        ProgressBar progress = findViewById(R.id.pgProcessPed);
        progress.setVisibility(view);
    }

    private void ConfigureToolBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ShowSingleDialog(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(getString(R.string.children_single_dialog_title_dialog))
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                SetVisibilityProgress(View.GONE);

                if (useImc){
                    SetVisibilityGraphic(View.VISIBLE);
                }


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
        }
    }

    private Double CalculateIMC(Double peso, Integer height) {

        Double heightM;
        Double result;
        Double IMC;

        heightM = height/100.00;
        result = heightM * heightM;
        IMC = peso / result;

        return IMC;

    }

    private void loadGraphics(Double imc, Double pounds, Double height) {

        TextView textLabelGraphicResult = findViewById(R.id.textLabelGraphicResult);
        barChart = (BarChart) findViewById(R.id.barChart);

        List<BarEntry> barHeight = new ArrayList<>();
        List<BarEntry> barPounds = new ArrayList<>();
        List<BarEntry> barImc = new ArrayList<>();

        barHeight.add(new BarEntry(0f, height.intValue()));
        barPounds.add(new BarEntry(1f, pounds.floatValue()));
        barImc.add(new BarEntry(2f, imc.floatValue()));

        BarDataSet dataSetHeight = new BarDataSet(barHeight, getString(R.string.children_graphic_height));
        BarDataSet dataSetPounds = new BarDataSet(barPounds, getString(R.string.children_graphic_weight));
        BarDataSet dataSetImc = new BarDataSet(barImc, "IMC");

        dataSetPounds.setColors(new int[] {Color.parseColor("#4BBCE5")});
        dataSetHeight.setColors(new int[] {Color.parseColor("#1C85A8")});

        if (imc >= 18.5 && imc <= 24.9) {
            dataSetImc.setColors(new int[] {Color.parseColor("#9AF9A6")});
            textLabelGraphicResult.setText("IMC: " + String.valueOf(precision.format(imc)) + getString(R.string.children_graphic_normal_weight));
        }

        if (imc < 18.5) {
            dataSetImc.setColors(new int[] {Color.parseColor("#F9F100")});
            textLabelGraphicResult.setText("IMC: " + String.valueOf(precision.format(imc)) + getString(R.string.children_graphic_low_weight));
        }

        if (imc >= 25.0 && imc <= 29.9) {
            dataSetImc.setColors(new int[] {Color.parseColor("#FF9D00")});
            textLabelGraphicResult.setText("IMC: " + String.valueOf(precision.format(imc)) + getString(R.string.children_graphic_high_weight));
        }

        if (imc >= 30.0 && imc <= 34.9) {
            dataSetImc.setColors(new int[] {Color.parseColor("#FF5D00")});
            textLabelGraphicResult.setText("IMC: " + String.valueOf(precision.format(imc)) + getString(R.string.children_graphic_obesity_I));
        }

        if (imc >= 35.0 && imc <= 39.9) {
            dataSetImc.setColors(new int[] {Color.parseColor("#D12900")});
            textLabelGraphicResult.setText("IMC: " + String.valueOf(precision.format(imc)) + getString(R.string.children_graphic_obesity_II));
        }

        if (imc > 40.0) {
            dataSetImc.setColors(new int[] {Color.parseColor("#FF0000")});
            textLabelGraphicResult.setText("IMC: " + String.valueOf(precision.format(imc)) + getString(R.string.children_graphic_obesity_III));
        }
        //dataset.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE});

        BarData barData = new BarData(dataSetHeight);
        barData.addDataSet(dataSetPounds);
        barData.addDataSet(dataSetImc);
        barData.setBarWidth(0.5f); // set custom bar width
        barChart.setData(barData);

        //SETA O VALOR MINIMO E MAXIMO DO EIXO Y
        YAxis left = barChart.getAxisLeft();
        left.setAxisMaximum(height.intValue());//50
        left.setAxisMinimum(0);

        //DESABILITA O EIXO X E DA DIREITA
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        //XAxis bottomAxis = barChart.getXAxis();
        //bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //bottomAxis.setAxisMinimum(0);

        //LEGENDA E DESCRIPTIONLABEL
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        barChart.getLegend().setTextSize(12f);
        barChart.getLegend().setYOffset(10f);

        barChart.setFitBars(true);
        barChart.invalidate();

    }

    private void ConfigureSwitchImc() {
        swtImc = (Switch)findViewById(R.id.swtImc);
        swtImc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    useImc = true;
                    SetVisibilityOnResultFieldsImc(View.VISIBLE);
                    SetVisibilityGraphic(View.GONE);
                } else {
                    useImc = false;
                    SetVisibilityOnResultFieldsImc(View.GONE);
                    SetVisibilityGraphic(View.GONE);
                }
            }
        });
    }

}
