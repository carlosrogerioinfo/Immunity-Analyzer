package com.life.medicineandnutrition.functions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalStorageLibrary {

    //WRITE VALUES TYPE
    public static void WriteStringKeyValue(String key, String value, Context context) {

        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)(context)));
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putString(key, value);

            editor.commit();

        } catch (Exception e) {

        }

    }

    public static void WriteIntegerKeyValue(String key, Integer value, Context context) {

        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)(context)));
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putInt(key, value);

            editor.commit();

        } catch (Exception e) {

        }

    }

    public static void WriteBooleanKeyValue(String key, Boolean value, Context context) {

        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)(context)));
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putBoolean(key, value);

            editor.commit();

        } catch (Exception e) {

        }

    }

    //WRITE VALUES TYPE
    public static String ReadStringKeyValue (String key, Context context) {

        String result = null;

        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            result = myPreferences.getString(key, "");

        } catch (Exception e) {

        }

        return result;
    }

    public static Integer ReadIntegerKeyValue (String key, Context context) {

        Integer result = null;

        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            result = myPreferences.getInt(key, -1);

        } catch (Exception e) {

        }

        return result;
    }

    public static Boolean ReadBooleanKeyValue (String key, Context context) {

        Boolean result = null;

        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            result =  myPreferences.getBoolean(key, false);

        } catch (Exception e) {

        }

        return result;
    }



}
