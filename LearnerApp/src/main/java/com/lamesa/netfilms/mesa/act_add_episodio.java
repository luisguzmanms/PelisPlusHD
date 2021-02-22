package com.lamesa.netfilms.mesa;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.lamesa.netfilms.R;

import static com.lamesa.netfilms.activity.act_film.mlistEpisodios;
import static com.lamesa.netfilms.mesa.fire.EliminarEpisodio;
import static com.lamesa.netfilms.mesa.fire.EnviarEpisodio;
import static com.lamesa.netfilms.otros.metodos.getListaEpisodios;

public class act_add_episodio extends AppCompatActivity {


    public static String tbIdioma;
    private String idFilm;
    private String idioma;
    private String idEpisodio;
    private String nombreEpisodio;
    public static ListView lvEpisodios;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_episodio);

        Bundle extras = getIntent().getExtras();
        idFilm = extras.getString("idFilm");
        idioma = extras.getString("idioma");

       getListaEpisodios(idFilm, idioma, act_add_episodio.this);

        vistas(act_add_episodio.this);


    }


    private void vistas(Context mContext) {




        EditText etIdEpisodio = findViewById(R.id.etIdEpisodio);
        EditText etNombreEpisodio = findViewById(R.id.etNombreEpisodio);
        EditText etAddLink = findViewById(R.id.etAddLink);
        EditText etIdioma = findViewById(R.id.etIdioma);
        etIdioma.setText(idioma);
        lvEpisodios = findViewById(R.id.lvEpisodios);

        MaterialButton btnEnviarCambios = findViewById(R.id.btnEnviarCambios);
        btnEnviarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarEpisodio(mContext, idFilm, etIdioma.getText().toString().toLowerCase().replace(" ",""), etIdEpisodio.getText().toString().toUpperCase().replace(" ",""), etAddLink.getText().toString().toLowerCase().replace(" ",""), etNombreEpisodio.getText().toString());
                etAddLink.setText("");
            }
        });



        etNombreEpisodio.setText(nombreEpisodio);
        etNombreEpisodio.setHint(nombreEpisodio);



        lvEpisodios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DialogSettings.theme = DialogSettings.THEME.DARK;
                DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


                MessageDialog.show((AppCompatActivity) mContext, "¿ELIMINAR ESTE EPISODIO?", "Se eliminará este episodio de FB", "SI", "CANCELAR")
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                if(mlistEpisodios!=null && !mlistEpisodios.isEmpty()) {
                                    EliminarEpisodio(mContext, idFilm, idioma, mlistEpisodios.get(position).getIdEpisodio());
                                } else {
                                    Toast.makeText(mContext, "mlistEpisodios nulo o vacio", Toast.LENGTH_SHORT).show();
                                }
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


    }
}
