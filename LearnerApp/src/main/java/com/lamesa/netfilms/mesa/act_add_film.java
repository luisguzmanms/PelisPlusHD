package com.lamesa.netfilms.mesa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.activity.act_film;
import com.lamesa.netfilms.otros.TinyDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.lamesa.netfilms.mesa.fire.DestacarFilm;
import static com.lamesa.netfilms.mesa.fire.EnviarFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBidFilm;
import static com.lamesa.netfilms.otros.metodos.EnviarCambioFB;

public class act_add_film extends AppCompatActivity {


    public static String tbIdioma;
    private String idFilm;
    private String idioma;
    private String idEpisodio;
    private String nombreEpisodio;
    public static ListView lvUrls;
    private boolean nuevoFilm;
    private String nombreFilm;
    private String categoriaFilm
            ;
    private String calidadFilm;
    private String tipoFilm;
    private String redFilm;
    private String imagenFilm;
    private String anoFilm;
    private String sipnosisFilm;
    private String puntajeFilm;
    private String estadoFilm;
    private TinyDB tinyDB;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_film);
        tinyDB = new TinyDB(this);
        Bundle extras = getIntent().getExtras();
        nuevoFilm = extras.getBoolean("nuevoFilm");

        if(nuevoFilm==false){
            idFilm = extras.getString("idFilm");
            idioma = extras.getString("idioma");
            nombreFilm = extras.getString("nombreFilm");
            categoriaFilm = extras.getString("categoriaFilm");
            tipoFilm = extras.getString("tipoFilm");
            imagenFilm = extras.getString("imagenFilm");
            calidadFilm = extras.getString("calidadFilm");
            redFilm = extras.getString("redFilm");
            anoFilm = extras.getString("anoFilm");
            sipnosisFilm = extras.getString("sipnosisFilm");
            puntajeFilm = extras.getString("puntajeFilm");
            estadoFilm = extras.getString("estadoFilm");
        }


        vistas(act_add_film.this);


    }


    private void vistas(Context mContext) {


        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;



        EditText etIdFilm = findViewById(R.id.etIdFilm);
        EditText etNombreFilm = findViewById(R.id.etNombreFilm);
        EditText etSipnosisFilm = findViewById(R.id.etSipnosisFilm);
        EditText etTipoFilm = findViewById(R.id.etTipoFilm);
        EditText etCalidadFilm = findViewById(R.id.etCalidadFilm);
        EditText etCategoriaFilm = findViewById(R.id.etCategoriaFilm);
        EditText etAnoFilm = findViewById(R.id.etAnoFilm);
        EditText etPuntajeFilm = findViewById(R.id.etPuntajeFilm);
        EditText etImagenFilm = findViewById(R.id.etImagenFilm);
        EditText etEstadoFilm = findViewById(R.id.etEstadoFilm);
        EditText etRedFilm = findViewById(R.id.etRedFilm);
        ImageView ivIdFilmBuscar = findViewById(R.id.ivIdFilmBuscar);




        if(nuevoFilm==false){
            etIdFilm.setText(idFilm);
             etNombreFilm.setText(nombreFilm);
             etSipnosisFilm.setText(sipnosisFilm);
             etTipoFilm.setText(tipoFilm);
             etCalidadFilm.setText(calidadFilm);
             etCategoriaFilm.setText(categoriaFilm);
             etAnoFilm.setText(anoFilm);
             etPuntajeFilm.setText(puntajeFilm);
             etImagenFilm.setText(imagenFilm);
             etEstadoFilm.setText(estadoFilm);
             etRedFilm.setText(redFilm);
        }





        MaterialButton btnEnviarFilm = findViewById(R.id.btnEnviarFilm);
        MaterialButton btnDestacarFilm = findViewById(R.id.btnDestacarFilm);
        btnDestacarFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogSettings.theme = DialogSettings.THEME.DARK;
                DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


                MessageDialog.show((AppCompatActivity) mContext, "AGREGAR O ELIMINAR DE FAVORITOS", "Automaticamente detectara si el Film esta en destacados, sino lo agrega, de lo contrario lo elimina", "SI", "CANCELAR")
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                DestacarFilm(act_add_film.this, etIdFilm.getText().toString());
                                return false;
                            }
                        })
                        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return true;                    //位于“取消”位置的按钮点击后无法关闭对话框
                            }
                        });



            }
        });


        btnEnviarFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogSettings.theme = DialogSettings.THEME.DARK;
                DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


                MessageDialog.show((AppCompatActivity) mContext, "ENVIAR ESTE FILM", "Los cambios no pueden ser restaurados.", "SI", "CANCELAR")
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                // para la fecha
                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String fechaActualizado = df.format(Calendar.getInstance().getTime());
                                EnviarFilm(act_add_film.this, etIdFilm.getText().toString(), etAnoFilm.getText().toString(), etCalidadFilm.getText().toString(), etCategoriaFilm.getText().toString(), etSipnosisFilm.getText().toString(), etImagenFilm.getText().toString(), etNombreFilm.getText().toString(), etTipoFilm.getText().toString(), etPuntajeFilm.getText().toString(), etRedFilm.getText().toString(), fechaActualizado);
                                EnviarCambioFB(mContext);
                                return false;
                            }
                        })

                        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                Toast.makeText(act_add_film.this, "Cancelado.", Toast.LENGTH_SHORT).show();
                                return true;                    //位于“取消”位置的按钮点击后无法关闭对话框
                            }
                        });
            }
        });


        ivIdFilmBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.putString(TBidFilm, etIdFilm.getText().toString());
                mContext.startActivity(new Intent(mContext, act_film.class));
                finish();
            }
        });


    }
}
