package com.lamesa.netfilms.video.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.v3.TipDialog;

public class WindowPermissionCheck {

    public static boolean checkPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(activity)) {

            //TipDialog.show((AppCompatActivity) activity, "Se requiere permisos para usar video flotante", TipDialog.TYPE.WARNING).setCancelable(true).setTipTime(10000);
            Toast.makeText(activity, "Se requiere permisos para usar video flotante.", Toast.LENGTH_SHORT).show();
            activity.startActivityForResult(
                    new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + activity.getPackageName())), 0);
            return false;
        }
        return true;
    }



    public static void onActivityResult(Activity activity,
                                        int requestCode,
                                        int resultCode,
                                        Intent data,
                                        OnWindowPermissionListener onWindowPermissionListener) {
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !Settings.canDrawOverlays(activity)) {
                TipDialog.show((AppCompatActivity) activity, "Error al obtener permisos de ventana flotante.", TipDialog.TYPE.ERROR).setCancelable(true).setTipTime(10000);;

                if(onWindowPermissionListener!=null)
                    onWindowPermissionListener.onFailure();
            }else {
                TipDialog.show((AppCompatActivity) activity, "Permisos obtenidos.", TipDialog.TYPE.SUCCESS).setCancelable(true).setTipTime(10000);;

                if(onWindowPermissionListener!=null)
                    onWindowPermissionListener.onSuccess();
            }
        }
    }




    public interface OnWindowPermissionListener{
        void onSuccess();
        void onFailure();
    }

}
