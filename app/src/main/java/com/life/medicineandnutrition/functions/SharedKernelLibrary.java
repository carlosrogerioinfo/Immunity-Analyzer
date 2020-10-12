package com.life.medicineandnutrition.functions;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Locale;

public class SharedKernelLibrary {

    private static int D3_UI = 40000;
    private static int D3_K2PARAMETER = 10000;

    //STRINGS
    public static Integer CountWords(String value) {

        String words = value.trim();

        if (words.isEmpty())
            return 0;

        return words.split("\\s+").length;

    }


    //DOUBLE
    public static Double ConvertToMiligrams(Double val) {
        return val / D3_UI;
    }

    public static Double CalculateK2DosageProportional(Double dosageVitaminD) {

        Double result;

        //result = kg * ctK2;
        result = (dosageVitaminD * 100)/D3_K2PARAMETER;
        return result;

    }

    public static Double CalculateMgDosageProportional(Double weight){

        Double ctMg = 5.0;
        return weight * ctMg;

    }

    public static double ConvertToNgMl(double value){

        return Math.round(value/2.5);

    }

    public static Boolean HasInternetConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) ((Activity)(context)).getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( connectivityManager != null ) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;

    }

    /*
    Locale.getDefault().getLanguage()       ---> en
    Locale.getDefault().getISO3Language()   ---> eng
    Locale.getDefault().getCountry()        ---> US
    Locale.getDefault().getISO3Country()    ---> USA
    Locale.getDefault().getDisplayCountry() ---> United States
    Locale.getDefault().getDisplayName()    ---> English (United States)
    Locale.getDefault().toString()          ---> en_US
    Locale.getDefault().getDisplayLanguage()---> English
    Locale.getDefault().toLanguageTag()     ---> en-US
    */

    public static String GetLanguage() {

        return Locale.getDefault().getLanguage();

    }

    public static String GetLanguageTag() {

        return Locale.getDefault().toString();

    }

    public static String GetCountry() {

        return Locale.getDefault().getCountry();

    }

    public static Boolean IsLocaleBrazil() {

        return GetLanguageTag().toLowerCase().equals("pt_br") && GetCountry().toLowerCase().equals("br");

    }


}
