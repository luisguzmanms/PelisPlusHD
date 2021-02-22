package com.lamesa.netfilms.otros;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amplitude.api.Amplitude;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static com.lamesa.netfilms.App.mFirebaseAnalytics;
import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.otros.statics.constantes.mixFalloEpisodio;
import static com.lamesa.netfilms.otros.statics.constantes.mixReporteFilm;


public class fire extends AppCompatActivity {


    //region ENVIAR A Firebase


    public static void EnviarSolicitud(final Context mContext, String mensaje) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando solicitud...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("reportes").child("solicitud");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(nombreRandom).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(nombreRandom).child("mensaje").setValue(mensaje);

                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Solicitud enviada.", TipDialog.TYPE.SUCCESS);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la solicitud.", TipDialog.TYPE.ERROR);

            }
        });

    }


    public static void EnviarSugerencia(final Context mContext, String mensaje) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando sugerencia...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("reportes").child("sugerencia");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(nombreRandom).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(nombreRandom).child("mensaje").setValue(mensaje);

                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la sugerencia.", TipDialog.TYPE.ERROR);

            }
        });

    }

    public static void ReportarEpisodio(final Context mContext, String mensaje, String idFilm, String idEpisodio, String nombreEpisodio, String nombreFilm, String idioma) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando reporte...").setCancelable(true);


        /*
        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

         */


        String nombreGuardar = idFilm + "-" + idEpisodio + "-" + idioma;

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("reportes").child("falloEpisodio");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(nombreGuardar).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(nombreGuardar).child("mensaje").setValue(mensaje);
                dataSnapshot.getRef().child(nombreGuardar).child("idFilm").setValue(idFilm);
                dataSnapshot.getRef().child(nombreGuardar).child("nombreEpisodio").setValue(nombreEpisodio);
                dataSnapshot.getRef().child(nombreGuardar).child("nombreFilm").setValue(nombreFilm);
                dataSnapshot.getRef().child(nombreGuardar).child("idEpisodio").setValue(idEpisodio);
                dataSnapshot.getRef().child(nombreGuardar).child("idioma").setValue(idioma);


                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Reporte enviado.", TipDialog.TYPE.SUCCESS);

                //region MIX FalloEpisodio para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("IdFilm", idFilm);
                    props.put("NombreFilm", nombreFilm);
                    props.put("Idioma", idioma);
                    props.put("IdEpisodio", idEpisodio);
                    props.put("NombreEpisodio", nombreEpisodio);
                    props.put("Mensaje", mensaje);
                    Bundle params = new Bundle();
                    params.putString("IdFilm", idFilm);
                    params.putString("NombreFilm", nombreFilm);
                    params.putString("Idioma", idioma);
                    params.putString("IdEpisodio", idEpisodio);
                    params.putString("NombreEpisodio", nombreEpisodio);
                    params.putString("Mensaje", mensaje);


                    mFirebaseAnalytics.logEvent(mixFalloEpisodio, params);
                    mixpanel.track(mixFalloEpisodio, props);
                    Amplitude.getInstance().logEvent(mixFalloEpisodio, props);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //endregion


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar reporte.", TipDialog.TYPE.ERROR);

            }
        });

    }

    public static void ReportarFilm(final Context mContext, String mensaje, String idFilm, String nombreFilm) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando reporte...").setCancelable(true);


        /*
        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

         */


        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("reportes").child("reporteFilm").child(idFilm);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Random random = new Random();
                int nombreGuardar = random.nextInt(99999);

                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("mensaje").setValue(mensaje);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("idFilm").setValue(idFilm);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("nombreFilm").setValue(nombreFilm);


                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Reporte enviado.", TipDialog.TYPE.SUCCESS);

                //region MIX ReporteFilm para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("IdFilm", idFilm);
                    props.put("NombreFilm", nombreFilm);
                    props.put("Mensaje", mensaje);
                    props.put("Fecha", fecha);
                    Bundle params = new Bundle();
                    params.putString("IdFilm", idFilm);
                    params.putString("NombreFilm", nombreFilm);
                    params.putString("Mensaje", mensaje);
                    params.putString("Fecha", fecha);


                    mFirebaseAnalytics.logEvent(mixReporteFilm, params);
                    mixpanel.track(mixReporteFilm, props);
                    Amplitude.getInstance().logEvent(mixReporteFilm, props);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //endregion

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar reporte.", TipDialog.TYPE.ERROR);

            }
        });

    }


    public static boolean LoginDeUsuario() {
        // NECESARIO PARA COMPROBAR EL INICIO DE SESION DE UN USUARIO
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    //endregion


}
