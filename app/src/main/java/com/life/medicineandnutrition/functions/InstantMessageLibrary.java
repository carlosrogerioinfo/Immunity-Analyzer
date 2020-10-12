package com.life.medicineandnutrition.functions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class InstantMessageLibrary {

    public static void OpenWhatsApp(String msg, String phoneNumber, Context context) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(
                    Uri.parse("http://api.whatsapp.com/send?phone=" +
                            phoneNumber + "&text=" +
                            msg));

            ((Activity)(context)).startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void ShowToast(String msg, Context context) {

        try {

            Toast toast = Toast.makeText(((Activity)(context)), msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void ShowSingleDialog(String title, String msg, Context context) {

        try {
            new AlertDialog.Builder(((Activity)(context)))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }

                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void ShowCustomizedDialog(String title, String msg, String labelButton, Context context) {

        try {
            new AlertDialog.Builder(((Activity)(context)))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(labelButton, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
