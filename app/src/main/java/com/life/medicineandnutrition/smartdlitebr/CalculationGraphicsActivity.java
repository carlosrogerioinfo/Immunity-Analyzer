package com.life.medicineandnutrition.smartdlitebr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;


public class CalculationGraphicsActivity extends AppCompatActivity {


    DecimalFormat precision = new DecimalFormat("0.00");

    Double vitamind;
    Double vitamindMin;
    Double vitamindMax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_graphics);

        ConfigureToolBar();
        GetParametersIntent();

        loadGraphicsVitaminDLevel(vitamindMin, vitamindMax, vitamind);

    }

    private void ConfigureToolBar() {

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void GoToMainActivity(View view){
        this.finish();
    }

    private void GetParametersIntent() {

        Intent it = getIntent();

        vitamind = it.getDoubleExtra("vitamind", 0);
        vitamindMin = it.getDoubleExtra("vitamindmin", 0);
        vitamindMax = it.getDoubleExtra("vitamindmax", 0);

    }

    //GRAFICO VITAMINA D3 LEVEL
    private void loadGraphicsVitaminDLevel(Double minValue, Double maxValue, Double resultValue) {

        BarChart barChart;

        TextView textLabelGraphicResultVitaminDLevel = findViewById(R.id.textLabelGraphicResultVitaminDLevel);
        barChart = (BarChart) findViewById(R.id.barChartVitaminDLevel);

        List<BarEntry> barMin = new ArrayList<>();
        List<BarEntry> barMax = new ArrayList<>();
        List<BarEntry> barResult = new ArrayList<>();

        barMin.add(new BarEntry(0f, minValue.intValue()));
        barMax.add(new BarEntry(1f, maxValue.floatValue()));
        barResult.add(new BarEntry(2f, resultValue.floatValue()));

        BarDataSet dataSetMin = new BarDataSet(barMin, getString(R.string.adult_graphic_min));
        BarDataSet dataSetMax = new BarDataSet(barMax, getString(R.string.adult_graphic_max));
        BarDataSet dataSetResult = new BarDataSet(barResult, getString(R.string.adult_graphic_result));

        dataSetMin.setColors(new int[] {Color.parseColor("#4BBCE5")});
        dataSetMax.setColors(new int[] {Color.parseColor("#1C85A8")});

        //DEFICIENTE
        if (resultValue >= 1 && resultValue <= 29) {
            dataSetResult.setColors(new int[] {Color.parseColor("#D8D8D8")});
            textLabelGraphicResultVitaminDLevel.setText(
                    getString(R.string.adult_graphic_result_label) +
                            String.valueOf(precision.format(resultValue)) +
                            getString(R.string.adult_graphic_result_deficiency));
        }

        //INSUFICIENTE
        if (resultValue >= 30 && resultValue <= 40) {
            dataSetResult.setColors(new int[] {Color.parseColor("#F9F100")});
            textLabelGraphicResultVitaminDLevel.setText(
                    getString(R.string.adult_graphic_result_label) +
                            String.valueOf(precision.format(resultValue)) +
                            getString(R.string.adult_graphic_result_insuficiency));
        }

        //BONS
        if (resultValue >= 41 && resultValue <= 50) {
            dataSetResult.setColors(new int[] {Color.parseColor("#9AF9A6")});
            textLabelGraphicResultVitaminDLevel.setText(
                    getString(R.string.adult_graphic_result_label) +
                            String.valueOf(precision.format(resultValue)) +
                            getString(R.string.adult_graphic_result_good));
        }

        //OTIMOS
        if (resultValue >= 51 && resultValue <= 70) {
            dataSetResult.setColors(new int[] {Color.parseColor("#39D390")});
            textLabelGraphicResultVitaminDLevel.setText(
                    getString(R.string.adult_graphic_result_label) +
                            String.valueOf(precision.format(resultValue)) +
                            getString(R.string.adult_graphic_result_optimum));
        }

        //EXCELENTES
        if (resultValue >= 71 && resultValue <= 100) {
            dataSetResult.setColors(new int[] {Color.parseColor("#00FF21")});
            textLabelGraphicResultVitaminDLevel.setText(
                    getString(R.string.adult_graphic_result_label) +
                            String.valueOf(precision.format(resultValue)) +
                            getString(R.string.adult_graphic_result_excelent));
        }

        // ACIMA
        if (resultValue > maxValue) {
            dataSetResult.setColors(new int[] {Color.parseColor("#FF0000")});
            textLabelGraphicResultVitaminDLevel.setText(
                    getString(R.string.adult_graphic_result_label) +
                            String.valueOf(precision.format(resultValue)) +
                            getString(R.string.adult_graphic_result_overload));
        }

        BarData barData = new BarData(dataSetMin);
        barData.addDataSet(dataSetMax);
        barData.addDataSet(dataSetResult);
        barData.setBarWidth(0.5f); // set custom bar width
        barChart.setData(barData);

        //SETA O VALOR MINIMO E MAXIMO DO EIXO Y
        YAxis left = barChart.getAxisLeft();
        if (resultValue > maxValue) {
            left.setAxisMaximum(resultValue.intValue());//50
        } else {
            left.setAxisMaximum(maxValue.intValue());//50
        }

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == 16908332){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
