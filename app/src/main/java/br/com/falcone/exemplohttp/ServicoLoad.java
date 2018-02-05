package br.com.falcone.exemplohttp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import br.com.falcone.exemplohttp.model.Artigo;
import br.com.falcone.exemplohttp.util.Util;

import static br.com.falcone.exemplohttp.MainActivity.TIPO_COMPONENTE_OKHTTP;
import static br.com.falcone.exemplohttp.MainActivity.TIPO_COMPONENTE_RETROFIT;

public class ServicoLoad extends Service {
    private final IBinder mBinder = new ServicoBinder();

    public ServicoLoad() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        new AssyncLoad().execute(TIPO_COMPONENTE_RETROFIT);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ServicoBinder extends Binder {
        ServicoLoad getService() {
            return ServicoLoad.this;
        }
    }

    public List<Artigo> CarregarArtigos(){
        return Util.CarregarArtigosRetrofit();
    }

    private class AssyncLoad extends AsyncTask<Integer, Void, List<Artigo>> {

        @Override
        protected List<Artigo> doInBackground(Integer ... inteiros) {
            return CarregarArtigos();
        }


        @Override
        protected void onPostExecute(List<Artigo> artigos) {
            super.onPostExecute(artigos);
            for(Artigo artigo : artigos){
                Log.d("TAG", artigo.getTitulo());
            }

        }
    }



}
