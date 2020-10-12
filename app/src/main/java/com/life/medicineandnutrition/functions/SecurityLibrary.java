package com.life.medicineandnutrition.functions;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;

import java.security.Key;
import java.util.UUID;
import android.content.SharedPreferences;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecurityLibrary {

    //CHAVE usar 16, 24 ou 32 caracteres   01234567890123456789012345678901 = 32
    private static String encryptionKey = "&#$@8547TYTreg*ikdtrqRt4652sRte*";

    public static String GetPhoneId (Context context) {

        String androidId = null;

        try {
            androidId = Settings.Secure.getString(
                    ((Activity)(context)).getBaseContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );

        } catch (Exception e) {

        }

        return androidId.toUpperCase();
    }

    public synchronized static String GetUniqueId (Context context) {

        String uniqueID = null;
        final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

        try {

            if (uniqueID == null) {
                SharedPreferences sharedPrefs = context.getSharedPreferences(
                        PREF_UNIQUE_ID, Context.MODE_PRIVATE);
                uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
                if (uniqueID == null) {
                    uniqueID = UUID.randomUUID().toString();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(PREF_UNIQUE_ID, uniqueID);
                    editor.commit();
                }
            }

        } catch (Exception e) {

        }

        return uniqueID.toUpperCase();
    }

    public static String Encrypt(String mensagem) throws Exception {

        //Esta rotina foi removida, pois é de propriedade intelectual.
    }

    public static String Decrypt(String decmensagem) throws Exception {

        //Esta rotina foi removida, pois é de propriedade intelectual.
    }

    private static String nullPadString(String original) {
        StringBuffer output = new StringBuffer(original);
        int remain = output.length() % 16;
        if (remain != 0) {
            remain = 16 - remain;
            for (int i = 0; i < remain; i++) {
                output.append((char) 0);
            }
        }
        return output.toString();
    }

    private static String fromHex(byte[] hex) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < hex.length; i++) {
            sb.append( Integer.toString( ( hex[i] & 0xff ) + 0x100, 16).substring( 1 ) );
        }
        return sb.toString();
    }

    private static byte[] toHex(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }



}
