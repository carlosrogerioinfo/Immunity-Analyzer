package com.life.medicineandnutrition.smartdlitebr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.os.Process;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.navigation.NavigationView;
import com.life.medicineandnutrition.functions.InstantMessageLibrary;
import com.life.medicineandnutrition.functions.LocalStorageLibrary;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this, Dashboard.class));
        //GoToRegister(null);

        CheckTermsAccept();

        drawerLayout = findViewById(R.id.layoutMain);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.setItemIconTintList(null);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_navigation_bar,
                R.string.close_navigation_bar);

        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_secondary, menu);
        return true;
    }
    */

    /* PUBLIC METHODS */

    public void GoToCalculationActivity(View view) {
        startActivity(new Intent(this, CalculationActivity.class));
    }

    public void GoToInfoActivity(View view){

        Integer checkTerms = ReadInfo(null);

        if (checkTerms.equals(-1) || checkTerms.equals(0)) {
            ShowToast(getString(R.string.general_single_dialog_legal_advice_required));
        } else {
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtra("parameter", "dashboard");
            startActivity(intent);
        }
    }

    public void GoToConvertionsActivity(View view) {
        startActivity(new Intent(this, ConvertionActivity.class));
    }

    public void GoToProportionsActivity(View view) {
        startActivity(new Intent(this, ProportionK2D3Activity.class));
    }


    public void GoToLegalAdviceActivity(View v){
        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        intent.putExtra("parameter", "legaladvice");
        startActivity(intent);
    }

    public void GoToUrineCalculateActivity(View view){
        startActivity(new Intent(this, CalciumUrineCalculateActivity.class));
    }

    public void GoToRegister(View view){
        startActivity(new Intent(this, CreateUserActivity.class));
    }

    public void GoToPharmacy(View view){
        startActivity(new Intent(this, PharmacyActivity.class));
    }

    public void GoToAdjustMegaDosis(View view){
        startActivity(new Intent(this, AdjustMegaDoseActivity.class));
    }

    public void GoToPediatricCalculationActivity(View view){
        startActivity(new Intent(this, CalculationPediatricActivity.class));
    }

    private void ShowToast(String msg) {
        InstantMessageLibrary.ShowToast(msg, MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        int idProcess = Process.myPid();
        Process.killProcess(idProcess);
    }

    private void CheckTermsAccept() {

        Integer checkTerms = ReadInfo(null);

        if (checkTerms.equals(-1) || checkTerms.equals(0)) {
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtra("parameter", "legaladvice");
            startActivity(intent);
        }

    }

    public Integer ReadInfo(View v) {

        return LocalStorageLibrary.ReadIntegerKeyValue("AcceptTerms", MainActivity.this);

    }

    public void GoToUrl(String url){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        //pass the url to intent data
        intent.setData(Uri.parse(url));

        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_contact:
                    InstantMessageLibrary.ShowCustomizedDialog(
                            getString(R.string.general_single_dialog_title_suggestion),
                            getString(R.string.main_single_dialog_contact),
                            "OK",
                            MainActivity.this);
                break;

            case R.id.nav_telegram:
                GoToUrl(Constants.PARAM_TELEGRAM_SMARTD3);
                break;

            case R.id.nav_othersapps:
                GoToUrl(Constants.PARAM_PLAY_STORE);
                break;

            case R.id.nav_legaladvices:
                GoToLegalAdviceActivity(null);
                break;

        }

        drawerLayout.closeDrawers();
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /*
    public void setDefaultTab (int index){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.select();
    }*/


}
