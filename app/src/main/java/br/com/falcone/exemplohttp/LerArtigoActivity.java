package br.com.falcone.exemplohttp;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.falcone.exemplohttp.model.Artigo;
import br.com.falcone.exemplohttp.util.Util;

public class LerArtigoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_artigo);
        //int idArtigo = getIntent().getIntExtra("artigoId", 0);
        Artigo artigo = (Artigo)getIntent().getSerializableExtra("artigo");
        if(artigo != null) {
            //Artigo artigo = Util.getBanco(getApplicationContext()).getArtigoDao().getById(idArtigo);

            TextView lbTituloArtigo = (TextView) findViewById(R.id.lbTituloArtigo);
            TextView lbDescricaoArtigo = (TextView) findViewById(R.id.lbDescricaoArtigo);
            TextView lbAutorArtigo = (TextView) findViewById(R.id.lbAutorArtigo);

            lbTituloArtigo.setText(artigo.getTitulo());
            lbDescricaoArtigo.setText(artigo.getDescricao());
            lbAutorArtigo.setText(artigo.getAutor());
        }
    }
}
