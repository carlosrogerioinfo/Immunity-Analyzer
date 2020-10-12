package com.life.medicineandnutrition.functions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeLibrary {

    //myEditor.putString("dateini", "2020-06-14 08:00:00");
    //Incrementa em dias o DateTime atual
    public static Date AddDaysOnCurrentDateTime (Integer daysIncrement) {

        Date currentDateTime = Calendar.getInstance().getTime();

        try {

            Calendar calendarDateTime = Calendar.getInstance();
            calendarDateTime.setTime(currentDateTime);
            calendarDateTime.add(Calendar.DATE, daysIncrement);
            currentDateTime = calendarDateTime.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentDateTime;

    }

    public static String DateTimeDiferenceToString(Date dateIni, Date dateEnd) {

        String dayDifference = null;

        try {

            long difference = Math.abs(dateEnd.getTime() - dateIni.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            dayDifference = Long.toString(differenceDates);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dayDifference;
    }

    public static long DateTimeDiference(Date dateIni, Date dateEnd) {

        long differenceDates = 0;

        try {

            long difference = Math.abs(dateEnd.getTime() - dateIni.getTime());
            differenceDates = difference / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return differenceDates;
    }

    public static long DateTimeDiference(Date dateIni) {

        Date currentDateTime = Calendar.getInstance().getTime();
        long differenceDates = 0;

        try {

            long difference = Math.abs(currentDateTime.getTime() - dateIni.getTime());
            differenceDates = difference / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return differenceDates;
    }

    public static long DateTimeDiference(String dateIni) {

        Date currentDateIni;
        long differenceDates = 0;

        try {

            Date currentDateTime = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PARAM_DATE_FORMAT, Locale.getDefault());
            currentDateIni = dateFormat.parse(dateIni);

            long difference = Math.abs(currentDateTime.getTime() - currentDateIni.getTime());
            differenceDates = difference / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return differenceDates;
    }

    public static String GetCurrentDateTimeFormat () {

        String strDateTime = null;

        try {

            Date currentDateTime = Calendar.getInstance().getTime();
            strDateTime = new SimpleDateFormat(Constants.PARAM_DATE_FORMAT).format(currentDateTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDateTime;

    }

}



