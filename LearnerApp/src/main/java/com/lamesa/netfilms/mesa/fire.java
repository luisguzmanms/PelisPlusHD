package com.lamesa.netfilms.mesa;


import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class fire extends AppCompatActivity {





    //region ENVIAR A Firebase

    public static void EnviarFilm(Context mContext, String idFilm, String ano,String calidad, String categoria,String descrip, String imagen, String nombre, String tipo, String puntaje, String red, String fechaActualizado){

        WaitDialog.show((AppCompatActivity) mContext, "Enviando film...").setCancelable(true);



        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(String.valueOf(idFilm));
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("info").child("ano").setValue(ano);
                dataSnapshot.getRef().child("info").child("calidad").setValue(calidad);
                dataSnapshot.getRef().child("info").child("categoria").setValue(categoria);
                dataSnapshot.getRef().child("info").child("descrip").setValue(descrip);
                dataSnapshot.getRef().child("info").child("id").setValue(idFilm);
                dataSnapshot.getRef().child("info").child("imagen").setValue(imagen);
                dataSnapshot.getRef().child("info").child("nombre").setValue(nombre);
                dataSnapshot.getRef().child("info").child("tipo").setValue(tipo);
                dataSnapshot.getRef().child("info").child("puntaje").setValue(puntaje);
                dataSnapshot.getRef().child("info").child("red").setValue(red);
                dataSnapshot.getRef().child("info").child("fechaActualizado").setValue(fechaActualizado);



                WaitDialog.dismiss();
                //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

                System.out.println("FILM SUBIDO == "+idFilm+" - "+nombre);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar film "+databaseError.getMessage(), TipDialog.TYPE.ERROR);

            }
        });
    }

    public static void DestacarFilm(final Context mContext, String idFilm) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Destacando film...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("destacados");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if( dataSnapshot.child(idFilm).exists()){
                    dataSnapshot.getRef().child(idFilm).removeValue();
                    Toast.makeText(mContext, "Eliminado de destacados", Toast.LENGTH_SHORT).show();
                    TipDialog.show((AppCompatActivity) mContext, "Eliminado de destacados", TipDialog.TYPE.SUCCESS);
                } else {
                    dataSnapshot.getRef().child(idFilm).setValue(idFilm);
                    TipDialog.show((AppCompatActivity) mContext, "Agregado a destacados", TipDialog.TYPE.SUCCESS);
                    Toast.makeText(mContext, "Agregado a destacados", Toast.LENGTH_SHORT).show();

                }






                WaitDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la solicitud. error = "+databaseError, TipDialog.TYPE.ERROR);

            }
        });

    }

    public static void EnviarCambioEpisodio(final Context mContext, String idFilm,String idioma, String idEpisodio, String link,String nombreEpisodio) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando link...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(idFilm).child("episodios").child(idioma).child(idEpisodio);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(link!=null  && !link.isEmpty()) {
                    dataSnapshot.getRef().child("links").child(nombreRandom).setValue(link);
                }
                dataSnapshot.getRef().child("nombre").setValue(nombreEpisodio);


                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Cambios realizados", TipDialog.TYPE.SUCCESS);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la solicitud. error = "+databaseError, TipDialog.TYPE.ERROR);

            }
        });

    }


    public static void EliminarLinkExistente(final Context mContext, String idFilm,String idioma, String idEpisodio, String linkExistente) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Eliminando link...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(idFilm).child("episodios").child(idioma).child(idEpisodio).child("links");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {


                    if (snapshot.exists()) {


                        if(snapshot.getValue().toString().toLowerCase().contains(linkExistente.toLowerCase())){
                            dataSnapshot.getRef().child(snapshot.getKey()).removeValue();
                            TipDialog.show((AppCompatActivity) mContext, "Link eliminado", TipDialog.TYPE.SUCCESS).setCancelable(true);
                            Toast.makeText(mContext, "Link eliminado", Toast.LENGTH_SHORT).show();
                        }


                    }

                }





                WaitDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la solicitud.", TipDialog.TYPE.ERROR);

            }
        });

    }



    public static void EnviarEpisodio(final Context mContext, String idFilm,String idioma, String idEpisodio, String link,String nombreEpisodio) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando episodio...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(idFilm).child("episodios").child(idioma.toLowerCase()).child(idEpisodio.toUpperCase());
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(link!=null  && !link.isEmpty()) {
                    dataSnapshot.getRef().child("links").child(nombreRandom).setValue(link);
                }
                dataSnapshot.getRef().child("nombre").setValue(idEpisodio+" "+nombreEpisodio);
                dataSnapshot.getRef().child("id").setValue(idFilm);
                dataSnapshot.getRef().child("idEpisodio").setValue(idEpisodio);

                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Cambios realizados", TipDialog.TYPE.SUCCESS);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la solicitud. error = "+databaseError, TipDialog.TYPE.ERROR);

            }
        });

    }


    public static void EliminarEpisodio(final Context mContext, String idFilm,String idioma, String idEpisodio) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Eliminando link...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(idFilm).child("episodios").child(idioma);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {


                    if (snapshot.exists()) {


                        if(snapshot.getKey().toLowerCase().contains(idEpisodio.toLowerCase())){
                            dataSnapshot.getRef().child(snapshot.getKey()).removeValue();
                            TipDialog.show((AppCompatActivity) mContext, "Episodio eliminado", TipDialog.TYPE.SUCCESS).setCancelable(true);
                            Toast.makeText(mContext, "Episodio eliminado", Toast.LENGTH_SHORT).show();
                        }


                    }

                }





                WaitDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la solicitud.", TipDialog.TYPE.ERROR);

            }
        });

    }



    public static void ReportarEpisodio (final Context mContext, String mensaje, String idFilm, String idEpisodio, String nombreEpisodio, String nombreFilm, String idioma) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando reporte...").setCancelable(true);


        /*
        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

         */


        String nombreGuardar = idFilm + "-" + idEpisodio+"-"+idioma;

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar reporte.", TipDialog.TYPE.ERROR);

            }
        });

    }

    public static void ReportarFilm (final Context mContext, String mensaje, String idFilm, String nombreFilm) {


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





                Random  random = new Random();
                int nombreGuardar = random.nextInt(99999);

                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("mensaje").setValue(mensaje);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("idFilm").setValue(idFilm);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("nombreFilm").setValue(nombreFilm);


                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Reporte enviado.", TipDialog.TYPE.SUCCESS);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar reporte.", TipDialog.TYPE.ERROR);

            }
        });

    }


    //endregion




}
