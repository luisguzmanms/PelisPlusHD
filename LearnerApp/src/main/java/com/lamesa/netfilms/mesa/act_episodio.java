package com.lamesa.netfilms.mesa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.InputDialog;
import com.lamesa.netfilms.R;

import static com.lamesa.netfilms.activity.act_main.mlistUrls;
import static com.lamesa.netfilms.mesa.fire.EliminarLinkExistente;
import static com.lamesa.netfilms.mesa.fire.EnviarCambioEpisodio;
import static com.lamesa.netfilms.otros.metodos.getUrlEpisodioEditar;

public class act_episodio extends AppCompatActivity {


    public static String tbIdioma;
    private String idFilm;
    private String idioma;
    private String idEpisodio;
    private String nombreEpisodio;
    public static ListView lvUrls;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_epi);

        Bundle extras = getIntent().getExtras();
        idFilm = extras.getString("idFilm");
        idioma = extras.getString("idioma");
        idEpisodio = extras.getString("idEpisodio");
        nombreEpisodio = extras.getString("nombreEpisodio");

        getUrlEpisodioEditar(act_episodio.this, idFilm, idioma, idEpisodio);

        vistas(act_episodio.this);


    }


    private void vistas(Context mContext) {


        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;



        EditText etNombreEpisodio = findViewById(R.id.etNombreEpisodio);
        EditText etAddLink = findViewById(R.id.etAddLink);
        etNombreEpisodio.setText(nombreEpisodio);
        etNombreEpisodio.setHint(nombreEpisodio);

        MaterialButton btnEnviarCambios = findViewById(R.id.btnEnviarCambios);
        btnEnviarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarCambioEpisodio(mContext, idFilm, idioma, idEpisodio, etAddLink.getText().toString(), etNombreEpisodio.getText().toString());
                etAddLink.setText("");
            }
        });




        lvUrls = findViewById(R.id.lvUrls);
        lvUrls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionn, long id) {

                if (mlistUrls != null && !mlistUrls.isEmpty()) {

                    InputDialog.show((AppCompatActivity) mContext, "Link", "Link de " + nombreEpisodio, "ABRIR", "CERRAR").setOtherButton("ELIMINAR").setInputText(mlistUrls.get(positionn))
                            .setCancelable(false)
                            .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                    Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();


                                    String url = inputStr;
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    mContext.startActivity(i);

                                    return false;
                                }
                            }).setOnOtherButtonClickListener(new OnInputDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                           // Toast.makeText(mContext, "asdasdasd", Toast.LENGTH_SHORT).show();
                            EliminarLinkExistente(mContext, idFilm, idioma, idEpisodio, mlistUrls.get(positionn));
                            return false;
                        }
                    });
                }
            }
        });

    }
}
