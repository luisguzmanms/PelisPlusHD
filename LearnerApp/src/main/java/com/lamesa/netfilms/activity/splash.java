package com.lamesa.netfilms.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.model.modelCategoria;
import com.lamesa.netfilms.modelAd.modelAddFilm;
import com.lamesa.netfilms.modelAd.modelAddVideoFilm;
import com.lamesa.netfilms.otros.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.lamesa.netfilms.otros.statics.constantes.TBpase;
import static com.lamesa.netfilms.otros.metodos.ListaCSV;


public class splash extends AppCompatActivity {


    private TinyDB tinydb;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tinydb = new TinyDB(this);


        CardView cvLogo = findViewById(R.id.cvLogo);

        cvLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*
                 AgregarFilm(splash.this,"peliculas3-04-20.csv");
                 AgregarFilm(splash.this,"series.csv");


                 */


               //  AgregarVideoSerie(splash.this, "guardar5.csv");


              // EliminarDato(splash.this);


            }
        });


        AppVersion();





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                DialogoPase(splash.this, tinydb);


            }
        }, 2000);








    }



    private void DialogoPase(Context mContext, TinyDB tiniDB){


        String pase = tinydb.getString(TBpase);
        String contrasena = "pplus";

        if(!pase.toLowerCase().contains(contrasena.toLowerCase())) {

            // new TraerListas(this).execute();

            // por defecto se añadira un token de usuario al tema todos donde se enviara notificaciones a todos
            FirebaseMessaging.getInstance().subscribeToTopic("todos").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   // Toast.makeText(getApplicationContext(),"Notificaciones activadas a todos",Toast.LENGTH_LONG).show();
                }
            });



            String titulo = "DIGITE LA CONTRASEÑA";
            String mensaje = "La contraseña es " + contrasena + "\n\n\n"+"Esta app aun sigue en desarrollo y se actualiza el contenido segun las sugerencias de los usuarios dando prioridad a las peliculas o series solicitadas, por favor participe enviando sugerencias y/o reportes de problemas para mejorar esta app."+"\n\n"+"apoyenos compartiendo esta app con sus amigos y familiares :,)";


            DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
            DialogSettings.theme = DialogSettings.THEME.DARK;


            InputDialog.build((AppCompatActivity) mContext)
                    //   .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                    .setTitle(titulo).setMessage(mensaje)
                    .setOkButton("ENTRAR", new OnInputDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                            if (inputStr.equals("") || !inputStr.toLowerCase().contains(contrasena.toLowerCase())) {
                                TipDialog.show((AppCompatActivity) mContext, "Error de contraseña", TipDialog.TYPE.ERROR);

                                return false;
                            } else {

                                tinydb.putString(TBpase, contrasena);
                                startActivity(new Intent(splash.this, act_main.class));
                                finish();

                                return true;
                            }
                        }
                    })
                    //  .setCancelButton("CANCELAR")
                    .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                    .setInputInfo(new InputInfo()
                            .setInputType(InputType.TYPE_CLASS_TEXT)
                            .setTextInfo(new TextInfo()
                                    .setFontColor(mContext.getResources().getColor(R.color.learn_colorPrimary))
                            )
                    )
                    .setCancelable(false)
                    .show();

        } else {
            startActivity(new Intent(splash.this, act_main.class));
            finish();
        }
    }





    private void EliminarDato(Context mContext){

        WaitDialog.show((AppCompatActivity) mContext, "Eliminando dato...").setCancelable(true);



        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("data").removeValue();



                WaitDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al eliminar dato", TipDialog.TYPE.ERROR);

            }
        });
    }

    private void AppVersion(){
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;

            TextView tvAppVersion = findViewById(R.id.tv_appversion);
            tvAppVersion.setText("Version: "+versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }






    private void SubirVideoFilmPel(Context mContext){

        List<modelAddVideoFilm> mlistAddFilm = new ArrayList<>();










        //region region


        modelAddVideoFilm  modelFilm729040 = new modelAddVideoFilm("729040","latino","T1-EP1","T1-EP1","");



        //endregion



        for (int i = 0; mlistAddFilm.size() > i; i++) {
            SubirVideo(mContext, mlistAddFilm.get(i).getId(), mlistAddFilm.get(i).getIdioma(), mlistAddFilm.get(i).getIdEpisodio(), mlistAddFilm.get(i).getNombreEpisodio(), mlistAddFilm.get(i).getLinkVideo());
    }


    }




    private void SubirVideo(Context mContext, String id, String idioma, String idEpisodio, String nombreEpisodio, String linkVideo){




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                WaitDialog.show((AppCompatActivity) mContext, "Enviando video...").setCancelable(true);


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(id).child("episodios").child(idioma).child(idEpisodio);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Random random = new Random();
                        int keyLink = random.nextInt(9999);



                        if(!dataSnapshot.exists()) {
                            dataSnapshot.getRef().child("id").setValue(id);
                            dataSnapshot.getRef().child("idEpisodio").setValue(idEpisodio);
                            dataSnapshot.getRef().child("nombre").setValue(nombreEpisodio);
                            dataSnapshot.getRef().child("links").child("op1").setValue(linkVideo);


                            WaitDialog.dismiss();
                            //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

                            System.out.println("FILM video SUBIDO == " + id + " - " + nombreEpisodio);
                         //   Toast.makeText(mContext, "FILM video SUBIDO == \"+id+\" - \"+nombreEpisodio", Toast.LENGTH_LONG).show();

                        } else {
                            System.out.println("FILM YA CARGADO == " + id + " - " + nombreEpisodio);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        WaitDialog.dismiss();
                        TipDialog.show((AppCompatActivity) mContext, "Error al enviar video", TipDialog.TYPE.ERROR);

                    }
                });


            }
        }, 20000);









    }

    private void SubirFilmSerie(Context mContext, String idFilm, String ano,String calidad, String categoria,String descrip, String imagen, String nombre, String tipo, String puntaje, String red, String fechaActualizado){

        WaitDialog.show((AppCompatActivity) mContext, "Enviando Serie...").setCancelable(true);



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
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar film", TipDialog.TYPE.ERROR);

            }
        });
    }

    //endregion



    private void SubirCategoria(Context mContext) {


        List<modelCategoria> mlistAddCategoria = new ArrayList<>();



        modelCategoria modelCategoria1 = new modelCategoria("https://i.pinimg.com/236x/a2/c0/35/a2c035e6bdafff043673f6c10f42790f.jpg","Accion");	mlistAddCategoria.add(modelCategoria1);
        modelCategoria modelCategoria2 = new modelCategoria("https://i.pinimg.com/236x/2a/3a/84/2a3a8443bcc4329c3ce6956e84b3ce82.jpg","Animacion");	mlistAddCategoria.add(modelCategoria2);
        modelCategoria modelCategoria3 = new modelCategoria("https://i.pinimg.com/236x/8e/4d/2d/8e4d2d22e7254063d13cf1fd81087c26.jpg","Aventura");	mlistAddCategoria.add(modelCategoria3);
        modelCategoria modelCategoria4 = new modelCategoria("https://i.pinimg.com/236x/c5/c8/76/c5c876abf1c13dcd9597d6b004f7df88.jpg","Ciencia ficcion");	mlistAddCategoria.add(modelCategoria4);
        modelCategoria modelCategoria5 = new modelCategoria("https://i.pinimg.com/236x/62/70/ef/6270ef4f25402183566d1bfb3b863885.jpg","Comedia");	mlistAddCategoria.add(modelCategoria5);
        modelCategoria modelCategoria6 = new modelCategoria("https://i.pinimg.com/236x/cd/59/7e/cd597e4548c30f49316a7966f6fc4395.jpg","Crimen");	mlistAddCategoria.add(modelCategoria6);
        modelCategoria modelCategoria7 = new modelCategoria("https://i.pinimg.com/236x/41/27/a1/4127a1e9359cbd81e2ada2ffd3bc0ed9.jpg","Documental");	mlistAddCategoria.add(modelCategoria7);
        modelCategoria modelCategoria8 = new modelCategoria("https://i.pinimg.com/236x/c3/7a/6d/c37a6d7d2496724516bf572a7f46b22c.jpg","Drama");	mlistAddCategoria.add(modelCategoria8);
        modelCategoria modelCategoria9 = new modelCategoria("https://i.pinimg.com/564x/26/ce/ec/26ceeca8f98a0124512a2730b7ea7e50.jpg","Familia");	mlistAddCategoria.add(modelCategoria9);
        modelCategoria modelCategoria10 = new modelCategoria("https://i.pinimg.com/236x/4d/f6/d8/4df6d8ba89f88a37b94b25aab374d61a.jpg","Fantasia");	mlistAddCategoria.add(modelCategoria10);
        modelCategoria modelCategoria11 = new modelCategoria("https://i.pinimg.com/236x/88/a4/17/88a4171abc3f1cf5291626a651151cb1.jpg","Guerra");	mlistAddCategoria.add(modelCategoria11);
        modelCategoria modelCategoria12 = new modelCategoria("https://i.pinimg.com/236x/9f/5d/b8/9f5db84dbfa958724923d5358433ba21.jpg","Historia");	mlistAddCategoria.add(modelCategoria12);
        modelCategoria modelCategoria13 = new modelCategoria("https://i.pinimg.com/236x/5a/9d/51/5a9d517ed5b045057cf83457f817c639.jpg","Misterio");	mlistAddCategoria.add(modelCategoria13);
        modelCategoria modelCategoria14 = new modelCategoria("https://i.pinimg.com/236x/a2/f8/7a/a2f87abad873a877cd133ea2e93ca02f.jpg","Musica");	mlistAddCategoria.add(modelCategoria14);
        modelCategoria modelCategoria15 = new modelCategoria("https://i.pinimg.com/236x/ce/80/ed/ce80ed181c48ac32a15d4cf415f27a70.jpg","Reality");	mlistAddCategoria.add(modelCategoria15);
        modelCategoria modelCategoria16 = new modelCategoria("https://i.pinimg.com/236x/5e/53/24/5e5324c3fcdbb4262b8454731625f862.jpg","Recomendadas");	mlistAddCategoria.add(modelCategoria16);
        modelCategoria modelCategoria17 = new modelCategoria("https://i.pinimg.com/236x/b5/27/7e/b5277e3357bad0dc59b7429a4a0f3cd8.jpg","Romance");	mlistAddCategoria.add(modelCategoria17);
        modelCategoria modelCategoria18 = new modelCategoria("https://i.pinimg.com/236x/5f/e2/ee/5fe2ee524751abbdfdae33eedc9e4512.jpg","Sincategoria");	mlistAddCategoria.add(modelCategoria18);
        modelCategoria modelCategoria19 = new modelCategoria("https://i.pinimg.com/236x/7e/c6/cb/7ec6cb3b30a9d373c13f2d0a1ade91a9.jpg","Suspenso");	mlistAddCategoria.add(modelCategoria19);
        modelCategoria modelCategoria20 = new modelCategoria("https://i.pinimg.com/236x/c5/c8/76/c5c876abf1c13dcd9597d6b004f7df88.jpg","Terror");	mlistAddCategoria.add(modelCategoria20);
        modelCategoria modelCategoria21 = new modelCategoria("https://i.pinimg.com/236x/62/70/ef/6270ef4f25402183566d1bfb3b863885.jpg","Thriller");	mlistAddCategoria.add(modelCategoria21);


        //region region


        //endregion


        for (int i = 0; mlistAddCategoria.size() > i; i++) {
            EnviarCategoria(mContext, mlistAddCategoria.get(i).getImagen(), mlistAddCategoria.get(i).getNombre());


        }


    }



    private void EnviarCategoria(Context mContext, String imagen, String nombre){
        WaitDialog.show((AppCompatActivity) mContext, "Enviando categoria...").setCancelable(true);


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("categorias");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Random random = new Random();
                int keyLink = random.nextInt(9999);


                dataSnapshot.getRef().child(String.valueOf(keyLink)).child("imagen").setValue(imagen);
                dataSnapshot.getRef().child(String.valueOf(keyLink)).child("nombre").setValue(nombre);



                WaitDialog.dismiss();
                //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

                System.out.println("CATEGORIA video SUBIDO == "+nombre);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar video", TipDialog.TYPE.ERROR);

            }
        });

    };




    private void AgregarFilm(Context mContext, String nombreFileCSV){



        List<String[]> listCSV = ListaCSV(mContext, nombreFileCSV);


        List<modelAddFilm> mlistAddFilm = new ArrayList<>();


        //region region


        //endregion


        if (listCSV != null && !listCSV.isEmpty())
            for (int i = 0; i < listCSV.size(); i++) {


                String id = listCSV.get(i)[0];
                String nombre= listCSV.get(i)[1];
                String sipnosis= listCSV.get(i)[2];
                String tipo= listCSV.get(i)[3];
                String calidad= listCSV.get(i)[4];
                String categoria= listCSV.get(i)[5];
                String ano= listCSV.get(i)[6];
                String puntaje= listCSV.get(i)[7];
                String imagen= listCSV.get(i)[8];
                String red= listCSV.get(i)[9];
                String estado = listCSV.get(i)[10];
                String fechaActualizado= listCSV.get(i)[11];


                modelAddFilm film = new modelAddFilm(id, nombre, sipnosis, tipo, calidad, categoria, ano, puntaje, imagen, red, estado, fechaActualizado);

                mlistAddFilm.add(film);


            }

        System.out.println("jajajajajaja"+mlistAddFilm.get(12).getSipnosis());



        for (int i = 0; i < mlistAddFilm.size(); i++) {
            System.out.println("LISTAAA " + mlistAddFilm.get(i).getId() + "-" + mlistAddFilm.get(i).getNombre() + "-" + mlistAddFilm.get(i).getCalidad() + "-" + mlistAddFilm.get(i).getFechaActualizado());
            SubirFilm(mContext,mlistAddFilm.get(i).getId(),mlistAddFilm.get(i).getNombre(),mlistAddFilm.get(i).getSipnosis(),mlistAddFilm.get(i).getTipo(),mlistAddFilm.get(i).getCalidad(),mlistAddFilm.get(i).getCategoria(),mlistAddFilm.get(i).getAno(),mlistAddFilm.get(i).getPuntaje(),mlistAddFilm.get(i).getImagen(),mlistAddFilm.get(i).getRed(),mlistAddFilm.get(i).getEstado(),mlistAddFilm.get(i).getFechaActualizado());
        }



    }



    private void AgregarVideo(Context mContext, String nombreFileCSV){



        List<String[]> listCSV = ListaCSV(mContext, nombreFileCSV);


        List<modelAddVideoFilm> mlistAddVideo = new ArrayList<>();



        if (listCSV != null && !listCSV.isEmpty())
            for (int i = 0; i < listCSV.size(); i++) {


                String id = listCSV.get(i)[0];
                String idioma= listCSV.get(i)[2];
                String idEpisodio= "EP1";
                String nombreEpisodio= "VER AHORA";
                String linkVideo= listCSV.get(i)[3];



                modelAddVideoFilm film = new modelAddVideoFilm(id, idioma, idEpisodio, nombreEpisodio, linkVideo);

                mlistAddVideo.add(film);


            }

       // System.out.println("jajajajajaja"+mlistAddVideo.get(12).getSipnosis());



        for (int i = 0; i < mlistAddVideo.size(); i++) {
         //   System.out.println("LISTAAA " + mlistAddFilm.get(i).getId() + "-" + mlistAddFilm.get(i).getNombre() + "-" + mlistAddFilm.get(i).getCalidad() + "-" + mlistAddFilm.get(i).getFechaActualizado());
            SubirVideo(mContext,mlistAddVideo.get(i).getId(),mlistAddVideo.get(i).getIdioma(),mlistAddVideo.get(i).getIdEpisodio(),mlistAddVideo.get(i).getNombreEpisodio(),mlistAddVideo.get(i).getLinkVideo());
        }



    }


    // subir item de lista a FB

    private void SubirFilm(Context mContext, String idFIlm, String nombre, String sipnosis, String tipo, String calidad, String categoria, String ano, String puntaje, String imagen, String red, String estado, String fechaActualizado) {

        WaitDialog.show((AppCompatActivity) mContext, "Enviando film...").setCancelable(true);



        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(String.valueOf(idFIlm));
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("info").child("ano").setValue(ano);
                dataSnapshot.getRef().child("info").child("calidad").setValue(calidad);
                dataSnapshot.getRef().child("info").child("categoria").setValue(categoria);
                dataSnapshot.getRef().child("info").child("descrip").setValue(sipnosis);
                dataSnapshot.getRef().child("info").child("id").setValue(idFIlm);
                dataSnapshot.getRef().child("info").child("imagen").setValue(imagen);
                dataSnapshot.getRef().child("info").child("nombre").setValue(nombre);
                dataSnapshot.getRef().child("info").child("tipo").setValue(tipo);
                dataSnapshot.getRef().child("info").child("puntaje").setValue(puntaje);
                dataSnapshot.getRef().child("info").child("red").setValue(red);
                dataSnapshot.getRef().child("info").child("estado").setValue(estado);
                dataSnapshot.getRef().child("info").child("fechaActualizado").setValue(fechaActualizado);


                WaitDialog.dismiss();
                //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);
                System.out.println("FILM SUBIDO == "+idFIlm+" - "+nombre);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar film", TipDialog.TYPE.ERROR);

            }
        });

    }


    private void AgregarVideoSerie(Context mContext, String nombreFileCSV){



        List<String[]> listCSV = ListaCSV(mContext, nombreFileCSV);


        List<modelAddVideoFilm> mlistAddVideo = new ArrayList<>();



        if (listCSV != null && !listCSV.isEmpty())
            for (int i = 0; i < listCSV.size(); i++) {


                String id = listCSV.get(i)[0];
                String idioma= listCSV.get(i)[7];
                String idEpisodio= listCSV.get(i)[4];
                String nombreEpisodio= listCSV.get(i)[4]+" "+listCSV.get(i)[5];
                String linkVideo= listCSV.get(i)[8];



                modelAddVideoFilm film = new modelAddVideoFilm(id, idioma, idEpisodio, nombreEpisodio, linkVideo);

                mlistAddVideo.add(film);


            }

        // System.out.println("jajajajajaja"+mlistAddVideo.get(12).getSipnosis());



        for (int i = 0; i < mlistAddVideo.size(); i++) {
            //   System.out.println("LISTAAA " + mlistAddFilm.get(i).getId() + "-" + mlistAddFilm.get(i).getNombre() + "-" + mlistAddFilm.get(i).getCalidad() + "-" + mlistAddFilm.get(i).getFechaActualizado());
            SubirVideo(mContext,mlistAddVideo.get(i).getId(),mlistAddVideo.get(i).getIdioma(),mlistAddVideo.get(i).getIdEpisodio(),mlistAddVideo.get(i).getNombreEpisodio(),mlistAddVideo.get(i).getLinkVideo());
        }



    }


}
