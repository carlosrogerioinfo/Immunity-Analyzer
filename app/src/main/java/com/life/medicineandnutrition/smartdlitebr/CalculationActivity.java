package com.life.medicineandnutrition.smartdlitebr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.life.medicineandnutrition.functions.InstantMessageLibrary;
import com.life.medicineandnutrition.functions.SharedKernelLibrary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.DecimalFormat;

import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;

public class CalculationActivity extends AppCompatActivity {

    private int TIME_SPLASH = 1000; //7000
    private int DOSAGE = 200;
    private Boolean language_br = false;

    private Switch swtOptionRound;
    private Switch swtOptionAge;
    private Switch swtOptionAnticoagulant;
    private Switch swtOptionOnlyWeigh;

    Double actualLevelD3;

    Double dailyDoseMg;
    Double manteiningDoseMg;
    Double valueTotalDailyD3;
    Double valueTotalManteiningD3;
    Double valueTotalK2;
    Double valueTotalK2Anticoagulant;
    Double valueTotalK2Manteining;
    Double valueTotalK2ManteiningAnticoagulant;
    Double valueTotalMG;
    Integer daysTreatment;

    Boolean useRound = true;
    Boolean useAge = false;
    Boolean useAnticoagulant = false;
    Boolean useOnlyWeigh = false;
    DecimalFormat precision = new DecimalFormat("0.00");

    TextView textDailyDose;
    TextView textManteiningDose;
    TextView textDailyDoseK2;
    TextView textManteiningDoseK2;

    private ColorStateList originalColorText;

    MaterialSpinner spinnerPeriod;
    Double tg; //Dias
    Double weight; //Peso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        VerifyLocaleLanguage();
        CheckTermsAccept();

        ConfigureDropDown();
        ConfigureToolBar();
        ConfigureFieldsNumberAndDecimal();
        SetVisibilityOnResultFields(View.GONE);
        HideParameters();

        ConfigureSwitchRound();
        ConfigureSwitchAge();
        ConfigureSwitchAnticoagulant();
        ConfigureSwitchOnlyWeigh();

        tg = -1.0;

    }

    private void ConfigureDropDown() {

        spinnerPeriod = (MaterialSpinner) findViewById(R.id.Spinner);
        spinnerPeriod.setItems(getString(R.string.adult_dropdown_period),
                getString(R.string.adult_dropdown_period45),
                getString(R.string.adult_dropdown_period60),
                getString(R.string.adult_dropdown_period90),
                getString(R.string.adult_dropdown_period120));
        spinnerPeriod.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                if (position == 0) {
                    tg = -1.0;
                } else if (position == 1) {
                    tg = 45.0;
                } else if (position == 2) {
                    tg = 60.0;
                } else if (position == 3) {
                    tg = 90.0;
                } else if (position == 4) {
                    tg = 120.0;
                }

                //Snackbar.make(view, "TG: " + tg + " Clicked " + item + " index: " + position, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == 16908332) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

    public void Calculate(View v) {

        //Esta rotina foi removida, pois é de propriedade intelectual.

        K2Calculate();
        MgCalculate();

        StartProcess();

        daysTreatment = tg.intValue();
        ShowInformationDailyDosage(daysTreatment);

    }

    public void CalculateOnlyByWeigh() {
        EditText ekg;
        String skg;
        Double kg;

        SetVisibilityOnResultFields(View.GONE);

        //Esta rotina foi removida, pois é de propriedade intelectual.

        K2Calculate();
        MgCalculate();

        StartProcess();
        ShowInformationDailyDosage(0);

    }

    public void ShowResultInMiligrams(View view) {
        String msg;

        msg = getString(R.string.adult_single_dialog_show_miligrams_1)
                + String.valueOf(precision.format(dailyDoseMg)) + " mg\n" +
                getString(R.string.adult_single_dialog_show_miligrams_2) +
                String.valueOf(precision.format(manteiningDoseMg)) + " mg";// +

        InstantMessageLibrary.ShowSingleDialog(
                getString(R.string.general_single_dialog_title),
                msg,
                CalculationActivity.this);
    }

    private void SetFocusKg() {
        TextInputEditText tit = findViewById(R.id.kg);
        tit.setFocusable(true);
        tit.requestFocus();
    }

    private void SetFocusAge() {
        TextInputEditText tit = findViewById(R.id.textAge);
        tit.setFocusable(true);
        tit.requestFocus();
    }

    private void K2Calculate() {
        Double result;
        Double resultManteining;

        TextView textK2 = findViewById(R.id.textK2);
        TextView textK2Manteining = findViewById(R.id.textK2Manteining);

        result = SharedKernelLibrary.CalculateK2DosageProportional(valueTotalDailyD3);
        resultManteining = SharedKernelLibrary.CalculateK2DosageProportional(valueTotalManteiningD3);

        valueTotalK2 = result;

        if (useAnticoagulant && result > 45) {
            InstantMessageLibrary.ShowSingleDialog(
                    getString(R.string.general_single_dialog_title),
                    getString(R.string.adult_single_dialog_anticoagulant_1) +
                            String.valueOf(Math.round(result)) + " mcg, " +
                            getString(R.string.adult_single_dialog_anticoagulant_2),
                    CalculationActivity.this);

            valueTotalK2Anticoagulant = 45.0;
            textK2.setText("45 mcg");
        } else {
            textK2.setText(String.valueOf(Math.round(result)) + " mcg");
        }

        if (useAnticoagulant && resultManteining > 45) {

            valueTotalK2ManteiningAnticoagulant = 45.0;
            valueTotalK2Manteining = valueTotalK2ManteiningAnticoagulant;

            if (!useOnlyWeigh) {
                textK2Manteining.setText("\n45 mcg");
            } else {
                textK2Manteining.setText("45 mcg");
            }

        } else {

            valueTotalK2ManteiningAnticoagulant = resultManteining;
            valueTotalK2Manteining = resultManteining;

            if (!useOnlyWeigh) {
                textK2Manteining.setText("\n" + String.valueOf(Math.round(resultManteining)) + " mcg");
            } else {
                textK2Manteining.setText(String.valueOf(Math.round(resultManteining)) + " mcg");
            }
        }

    }

    private void MgCalculate() {
        EditText ekg;
        String skg;
        Double kg;
        Double ctMg = 5.0;

        ekg = (EditText) findViewById(R.id.kg);
        skg = ekg.getText().toString();
        skg = skg.replace(",", ".");
        kg = Double.valueOf(skg).doubleValue();

        TextView textMg = findViewById(R.id.textMg);

        valueTotalMG = SharedKernelLibrary.CalculateMgDosageProportional(kg);
        textMg.setText(String.valueOf(Math.round(valueTotalMG)) + " mg");

    }

    public void ClearAll(View v) {

        ((EditText) findViewById(R.id.textAge)).setText("");
        ((EditText) findViewById(R.id.kg)).setText("");
        ((EditText) findViewById(R.id.aw)).setText("");
        ((EditText) findViewById(R.id.zw)).setText("");
        spinnerPeriod.setSelectedIndex(0);

        ((TextView) findViewById(R.id.textTreatment)).setText("");

        ((TextView) findViewById(R.id.textK2)).setText("");
        ((TextView) findViewById(R.id.textMg)).setText("");

        SetVisibilityOnResultFields(View.GONE);
        SetFocusKg();
    }


    private void ConfigureFieldsNumberAndDecimal() {
        TextInputEditText textInputKg = findViewById(R.id.kg);
        textInputKg.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        TextInputEditText textInputAw = findViewById(R.id.aw);
        textInputAw.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        TextInputEditText textInputZw = findViewById(R.id.zw);
        textInputZw.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
    }


    private void ConfigureToolBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void StartProcess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SetVisibilityOnResultFields(View.VISIBLE);
                SetVisibilityProgress(View.GONE);

                if (useOnlyWeigh) {
                    SetVisibilityButtonGraphics(View.GONE);
                } else {
                    SetVisibilityButtonGraphics(View.VISIBLE);
                }

            }
        }, TIME_SPLASH);
    }


    private void ShowInformationDailyDosage(int days) {

        if (days > 0) {
            textDailyDose = findViewById(R.id.textDailyDose);
            textDailyDose.setText(getString(R.string.adult_label_title_daily_dose_by) + String.valueOf(days) + getString(R.string.adult_label_title_days));

            textDailyDoseK2 = findViewById(R.id.textDailyDoseK2);
            textDailyDoseK2.setText(getString(R.string.adult_label_title_daily_dose_by) + String.valueOf(days) + getString(R.string.adult_label_title_days));

            textManteiningDose = findViewById(R.id.textManteiningDose);
            textManteiningDose.setText(getString(R.string.adult_label_title_manteining_dose_by) + String.valueOf(days) + getString(R.string.adult_label_title_days));

            textManteiningDoseK2 = findViewById(R.id.textManteiningDoseK2);
            textManteiningDoseK2.setText(getString(R.string.adult_label_title_manteining_dose_by) + String.valueOf(days) + getString(R.string.adult_label_title_days));
        } else {
            textDailyDose = findViewById(R.id.textDailyDose);
            textDailyDose.setText(getString(R.string.adult_label_title_daily_dose_text));

            textDailyDoseK2 = findViewById(R.id.textDailyDoseK2);
            textDailyDoseK2.setText(getString(R.string.adult_label_title_daily_dose_text));

            textManteiningDose = findViewById(R.id.textManteiningDose);
            textManteiningDose.setText(getString(R.string.adult_label_title_manteining_dose_text));

            textManteiningDoseK2 = findViewById(R.id.textManteiningDoseK2);
            textManteiningDoseK2.setText(getString(R.string.adult_label_title_manteining_dose_text));

        }
    }

    //VISIBILITY AND PARAMETERS
    private void SetVisibilityOnResultFields(int view) {
        TableLayout tableLayoutD3 = findViewById(R.id.containerResult);
        tableLayoutD3.setVisibility(view);

        TableLayout tableLayoutK2 = findViewById(R.id.containerResultK2);
        tableLayoutK2.setVisibility(view);

        TableLayout tableLayoutMg = findViewById(R.id.containerResultPositive);
        tableLayoutMg.setVisibility(view);

        if (language_br) {
            TableLayout containerButtonPharmacy = findViewById(R.id.containerButtonPharmacy);
            containerButtonPharmacy.setVisibility(view);
        }

    }

    private void SetVisibilityButtonGraphics(int view) {

        TableLayout containerButtonGraphics = findViewById(R.id.containerButtonGraphics);
        containerButtonGraphics.setVisibility(view);

    }

    private void SetVisibilityOnOnlyWeigh(int view) {
        SetFocusKg();
    }

    private void HideParameters() {
        TextInputLayout tilAge = findViewById(R.id.tilAge);
        tilAge.setVisibility(View.GONE);
    }

    private void SetVisibilityFieldActualLevel(int view) {
        TableLayout container = findViewById(R.id.containerActualLevel);
        container.setVisibility(view);
    }

    private void SetVisibilityFieldTargetLevel(int view) {
        TableLayout container = findViewById(R.id.containerTargetLevel);
        container.setVisibility(view);
    }

    private void SetVisibilityFieldPeriod(int view) {
        TableLayout container = findViewById(R.id.containerPeriod);
        container.setVisibility(view);
    }

    private void SetVisibilityFieldAge(int view) {
        TextInputLayout tilAge = findViewById(R.id.tilAge);
        tilAge.setVisibility(view);
    }

    private void SetVisibilityProgress(int view) {
        ProgressBar progress = findViewById(R.id.pgProcess);
        progress.setVisibility(view);
    }


    //GOTO ACTIVITIES
    public void GoToMainActivity(View view) {
        this.finish();
    }

    public void GoToPharmacy(View view) {

        if (SharedKernelLibrary.HasInternetConnection(this)) {

            Intent intent = new Intent(this, PharmacyActivity.class);

            intent.putExtra("paramd3", String.format("%.0f", valueTotalDailyD3) + " UI");
            intent.putExtra("dosaged3manteining", String.format("%.0f", valueTotalManteiningD3) + " UI");

            if (useAnticoagulant) {
                intent.putExtra("paramk2", String.format("%.0f", valueTotalK2Anticoagulant) + " mcg");
                intent.putExtra("dosagek2manteining", String.format("%.0f", valueTotalK2ManteiningAnticoagulant) + " mcg");
            } else {
                intent.putExtra("paramk2", String.format("%.0f", valueTotalK2) + " mcg");
                intent.putExtra("dosagek2manteining", String.format("%.0f", valueTotalK2Manteining) + " mcg");
            }

            intent.putExtra("parammg", String.format("%.0f", valueTotalMG) + " mg");
            intent.putExtra("weight", String.format("%.0f", weight) + " kg");

            //CAMPOS NOVOS
            if (daysTreatment == 0) {
                intent.putExtra("period", " 60 (dias)");
            } else {
                intent.putExtra("period", daysTreatment.toString() + " (dias)");
            }

            startActivity(intent);
        } else {
            InstantMessageLibrary.ShowCustomizedDialog(
                    getString(R.string.general_single_dialog_title_connection_error),
                    getString(R.string.general_single_dialog_msg_connection_error),
                    getString(R.string.general_label_btn_ok_understand),
                    this
            );
        }

    }

    public void GoToInfoActivity(View view) {
        Intent intent = new Intent(CalculationActivity.this, InfoActivity.class);
        intent.putExtra("parameter", "calculator");
        startActivity(intent);
    }

    public void GoToGraphicsD3(View view) {

        Calculate(null);

        Intent intent = new Intent(CalculationActivity.this, CalculationGraphicsActivity.class);

        //VITAMINA D3 NIVEL
        intent.putExtra("vitamind", actualLevelD3);
        intent.putExtra("vitamindmin", 40.0);
        intent.putExtra("vitamindmax", 100.0);

        startActivity(intent);
    }

    //MESSAGESDIALOGS
    public void RecomendedLevelD3(View v) {

        String complementText = getString(R.string.adult_single_dialog_complement_text);

        InstantMessageLibrary.ShowCustomizedDialog(
                getString(R.string.adult_single_dialog_recomended_level_title),
                getString(R.string.adult_single_dialog_recomended_level_msg) +
                        complementText,
                getString(R.string.general_label_btn_ok_understand),
                CalculationActivity.this
        );

    }

    public void OptionsInformation(View v) {

        InstantMessageLibrary.ShowCustomizedDialog(
                getString(R.string.adult_single_dialog_options_info_title),
                getString(R.string.adult_single_dialog_options_info_msg),
                getString(R.string.general_label_btn_ok_understand),
                CalculationActivity.this);

    }

    public void Dosage25OHD3(View v) {

        InstantMessageLibrary.ShowCustomizedDialog(
                getString(R.string.adult_single_dialog_dosage25ohd3_title),
                getString(R.string.adult_single_dialog_dosage25ohd3_msg),
                getString(R.string.general_label_btn_ok_understand),
                CalculationActivity.this);

    }

    public void ShowInfoPeriod(View v) {

        InstantMessageLibrary.ShowCustomizedDialog(
                getString(R.string.adult_single_dialog_info_period_title),
                getString(R.string.adult_single_dialog_info_period_msg),
                getString(R.string.general_label_btn_ok_understand),
                CalculationActivity.this);

    }

    //AVISO LEGAL
    public Integer ReadInfo(View v) {

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Integer info = myPreferences.getInt("AcceptTerms", -1);

        return info;
    }

    private void CheckTermsAccept() {

        Integer checkTerms = ReadInfo(null);
        if (checkTerms.equals(-1) || checkTerms.equals(0)) {
            InstantMessageLibrary.ShowToast(
                    getString(R.string.general_single_dialog_legal_advice_required),
                    CalculationActivity.this);
            this.finish();
        }
    }

    //CONFIGURAÇÃO DOS SWITCH
    private void ConfigureSwitchRound() {
        swtOptionRound = (Switch) findViewById(R.id.swtRound);
        swtOptionRound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    useRound = true;
                    SetVisibilityOnResultFields(View.GONE);
                } else {
                    useRound = false;
                    SetVisibilityOnResultFields(View.GONE);
                    InstantMessageLibrary.ShowSingleDialog(
                            getString(R.string.general_single_dialog_title),
                            getString(R.string.adult_switch_option_round),
                            CalculationActivity.this);
                }
            }
        });
    }

    private void ConfigureSwitchAge() {
        swtOptionAge = (Switch) findViewById(R.id.swtAge);
        swtOptionAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SetVisibilityFieldAge(View.VISIBLE);
                    SetVisibilityOnResultFields(View.GONE);
                    SetFocusAge();
                    useAge = true;
                } else {
                    SetVisibilityFieldAge(View.GONE);
                    SetVisibilityOnResultFields(View.GONE);
                    useAge = false;
                }
            }
        });
    }

    private void ConfigureSwitchAnticoagulant() {
        swtOptionAnticoagulant = (Switch) findViewById(R.id.swtAnticoagulant);
        swtOptionAnticoagulant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    useAnticoagulant = true;
                    SetVisibilityOnResultFields(View.GONE);
                } else {
                    useAnticoagulant = false;
                    SetVisibilityOnResultFields(View.GONE);
                }
            }
        });
    }

    private void ConfigureSwitchOnlyWeigh() {
        swtOptionOnlyWeigh = (Switch) findViewById(R.id.swtOnlyWeigh);
        swtOptionOnlyWeigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    useOnlyWeigh = true;
                    SetVisibilityOnResultFields(View.GONE);
                    SetVisibilityOnOnlyWeigh(View.GONE);

                    SetVisibilityFieldActualLevel(View.GONE);
                    SetVisibilityFieldTargetLevel(View.GONE);
                    SetVisibilityFieldPeriod(View.GONE);

                    SetVisibilityButtonGraphics(View.GONE);
                    ClearAll(null);
                } else {
                    useOnlyWeigh = false;
                    SetVisibilityOnResultFields(View.GONE);
                    SetVisibilityOnOnlyWeigh(View.VISIBLE);

                    SetVisibilityFieldActualLevel(View.VISIBLE);
                    SetVisibilityFieldTargetLevel(View.VISIBLE);
                    SetVisibilityFieldPeriod(View.VISIBLE);
                }
            }
        });
    }

    private void VerifyLocaleLanguage() {

        language_br = SharedKernelLibrary.IsLocaleBrazil();

    }


}
