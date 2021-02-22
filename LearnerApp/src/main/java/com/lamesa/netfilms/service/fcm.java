package com.lamesa.netfilms.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.amplitude.api.Amplitude;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.activity.act_film;
import com.lamesa.netfilms.activity.act_main;
import com.lamesa.netfilms.otros.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.crashlytics.android.Crashlytics.log;
import static com.lamesa.netfilms.App.mFirebaseAnalytics;
import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.otros.statics.constantes.TBidFilm;
import static com.lamesa.netfilms.otros.statics.constantes.mixActualizarApp;
import static com.lamesa.netfilms.otros.statics.constantes.mixNotificacionClic;
import static com.lamesa.netfilms.otros.statics.constantes.setDebugActivo;
import static com.lamesa.netfilms.otros.statics.constantes.NotiAbierta;
import static com.lamesa.netfilms.otros.metodos.setDebug;

public class fcm  extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("TAG","token fcm: "+s);
        //GuardarToken() para guardar en FB
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        setDebug("fcm","onMessageReceived","d","Mensaje recibido de "+from,setDebugActivo);

        if(remoteMessage.getNotification()!=null){
            setDebug("fcm","onMessageReceived","d","El titulo es "+remoteMessage.getNotification().getTitle(), setDebugActivo);
            setDebug("fcm","onMessageReceived","d","El mensaje es "+remoteMessage.getNotification().getBody(), setDebugActivo);

         //   MostrarNotificacion();


        }


        if(remoteMessage.getData().size()>0){
            EnviarNotificacion(this,remoteMessage.getData().get("idFilm"),remoteMessage.getData().get("titulo"),remoteMessage.getData().get("mensaje"),remoteMessage.getData().get("summaryText"));
        }

    }



    public static void EnviarNotificacion(Context mContext, String idFilm, String titulo, String mensaje, String summaryText){

        String id = summaryText;

        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
     NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,summaryText);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id,summaryText,NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            nc.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }


        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
       // style.bigText("这里是点击通知后要显示的正文，可以换行可以显示很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长");
        style.setBigContentTitle(titulo);

        //SummaryText没什么用 可以不设置
        style.setSummaryText(summaryText);


        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getApplicationContext().getResources(), R.drawable.icon2);

        Random random = new Random();
        int idNotify= random.nextInt(9999);

        builder.setAutoCancel(false)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(mensaje)
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_sinfondo), 128, 128, false))
                .setContentIntent(clicknoti(mContext, idFilm, titulo, mensaje, nm, idNotify))
                .setContentInfo(summaryText)
                .setStyle(style);





        assert nm != null;
        nm.notify(idNotify,builder.build());


       // TipDialog.show(mContext,"Notificación enviada a todos.", TipDialog.TYPE.SUCCESS).setTipTime(5000);





    }

    public static PendingIntent clicknoti(Context mContext, String idFilm, String titulo, String mensaje, NotificationManager nm, int idNotif){
        TinyDB tinyDB = new TinyDB(mContext);
        Class<?> activity = act_main.class;

        //si la notificacion tiene una idFilm enviar a act_film
        if(idFilm!=null && !idFilm.isEmpty()) {
            tinyDB.putString(TBidFilm, idFilm);
            // guardar boleano para saber si se ha abierto act_film pero no act_main
            NotiAbierta = true;

            // si ennia a acta film primero crear un uintent al main para que al dar onbackpred no se salga de la app
            Intent intent = new Intent(mContext.getApplicationContext(), act_main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity = act_film.class;
        }


        Intent intent = new Intent(mContext.getApplicationContext(), activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);




        //cerrar notificacion al dar clic
        assert nm != null;
        nm.cancel(idNotif);



        //region MIX mixNotificacionClic para estadisticas
        JSONObject props = new JSONObject();
        try {
            props.put("idFilm", idFilm);
            props.put("Titulo-NotificacionClic", titulo);
            props.put("Mensaje-NotificacionClic", mensaje);


            Bundle params = new Bundle();
            params.putString("idFilm", idFilm);
            params.putString("Titulo-NotificacionClic", titulo);
            params.putString("Mensaje-NotificacionClic", mensaje);



            mFirebaseAnalytics.logEvent(mixNotificacionClic, params);
            mixpanel.track(mixNotificacionClic, props);
            Amplitude.getInstance().logEvent(mixNotificacionClic, props);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return PendingIntent.getActivity(mContext,0,intent,0);

    }


    public static void EnviarNotiTodos(Context mContext,String idFilm, String titulo, String mensaje, String summaryText) {

        RequestQueue mmyrequest = Volley.newRequestQueue(mContext.getApplicationContext());
        JSONObject json = new JSONObject();

        try {

            json.put("to","/topics/"+"todos");
            JSONObject notification = new JSONObject();
            notification.put("idFilm",idFilm);
            notification.put("titulo", titulo);
            notification.put("mensaje",mensaje);
            notification.put("summaryText",summaryText);

            json.put("data",notification);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAiz1yUKA:APA91bFP90cC5t81URyUfOU1YdoYjojoVnP2lSJBt_1gZIV-YlibiPKlk339DgD8Ey1niL9AD-Cye3FMB5W3gQBkqBn1GRhVs-YH7KeXbXLKmhr_dIXAEPFbKXZ2jFtjlH2IeVCqXCv9");
                    return header;
                }
            };

            mmyrequest.add(request);


            Toast.makeText(mContext, "Notificación enviada a todos.", Toast.LENGTH_SHORT).show();


        } catch(Exception e){
            e.printStackTrace();
        }
    }


}
