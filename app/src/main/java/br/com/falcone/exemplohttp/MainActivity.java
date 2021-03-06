package br.com.falcone.exemplohttp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.falcone.exemplohttp.model.Artigo;
import br.com.falcone.exemplohttp.model.ArtigoDao;
import br.com.falcone.exemplohttp.util.Util;

public class MainActivity extends AppCompatActivity {

    public static final int TIPO_COMPONENTE_NATIVO = 0;
    public static final int TIPO_COMPONENTE_OKHTTP = 1;
    public static final int TIPO_COMPONENTE_RETROFIT = 2;
    private static final String PREFS_NAME = "preferencias";

    ServicoLoad mService;
    boolean mBound = false;
    ArrayAdapter mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //if(Util.IsConnected(getApplicationContext()))
        //startService(new Intent(this, ServicoLoad.class));


        CarregarDataUltimaAtualizacao();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AssyncLoad().execute(TIPO_COMPONENTE_NATIVO);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabOKHTTP = (FloatingActionButton) findViewById(R.id.fabOKHTTP);
        fabOKHTTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AssyncLoad().execute(TIPO_COMPONENTE_OKHTTP);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabRetrofit = (FloatingActionButton) findViewById(R.id.fabRetrofit);
        fabRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AssyncLoad().execute(TIPO_COMPONENTE_RETROFIT);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ListView lstArtigos = (ListView)findViewById(R.id.lstArtigos);
        lstArtigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Artigo artigo = (Artigo)MainActivity.this.mAdapter.getItem(posicao);
                LerArtigo(artigo);
            }
        });

        Button btCarregar = (Button)findViewById(R.id.btCarregar);
        btCarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AssyncLoad().execute(TIPO_COMPONENTE_RETROFIT);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    private void LerArtigo(Artigo artigo) {
        Intent i = new Intent(getApplicationContext(), LerArtigoActivity.class);
        i.putExtra("artigo", artigo);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, ServicoLoad.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void CarregarDataUltimaAtualizacao(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Date myDate = new Date(settings.getLong("dataUltimaAtualizacao", 0));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //android.text.format.DateFormat.getLongDateFormat(getApplicationContext());

        TextView lbDataUltimaAtualizacao = (TextView) findViewById(R.id.lbDataUltimaAtualizacao);

        lbDataUltimaAtualizacao.setText("Última Atualização: " + dateFormat.format(myDate.getTime()));
    }
    public class AssyncLoad extends AsyncTask<Integer, Void, List<Artigo>> {

        @Override
        protected List<Artigo> doInBackground(Integer ... inteiros) {
            List<Artigo> artigos = null;
            if(mBound && Util.IsConnected(getApplicationContext())) {
                artigos = mService.CarregarArtigos();
                if(artigos != null) {
                    ArtigoDao dao = Util.getBanco(getApplicationContext()).getArtigoDao();
                    dao.deleteAll();
                    dao.insertAll(artigos);
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    Date dataUltimaAtualizacao = new Date(System.currentTimeMillis());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putLong("dataUltimaAtualizacao", dataUltimaAtualizacao.getTime());
                    editor.commit();
                }

            }
            else{
                artigos = Util.getBanco(getApplicationContext()).getArtigoDao().getAll();
            }
            return artigos;



        }





        @Override
        protected void onPostExecute(List<Artigo> artigos) {
            super.onPostExecute(artigos);


            CarregarDataUltimaAtualizacao();

            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, artigos) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    if(view instanceof TwoLineListItem){
                        ((TwoLineListItem)view).setBackgroundColor(Color.LTGRAY);
                    }
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(((Artigo)this.getItem(position)).getAutor());
                    text2.setText(((Artigo)this.getItem(position)).getTitulo());
                    //text2.setText(artigos.get(position).getAge());
                    return view;
                }
            };

            mAdapter = adapter;
            ListView lstDados = (ListView) findViewById(R.id.lstArtigos);
            lstDados.setAdapter(adapter);

            /*TextView lbDados = (TextView) findViewById(R.id.lbDados);
            String texto = "";
            for(Artigo artigo : artigos){
                texto += artigo.getDescricao() + "\n";
            }

            lbDados.setText(texto);*/
            //Snackbar.make(MainActivity.this, "Replace with your own action", Snackbar.LENGTH_LONG)
      //              .setAction("Action", null).show();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServicoLoad.ServicoBinder binder = (ServicoLoad.ServicoBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}

