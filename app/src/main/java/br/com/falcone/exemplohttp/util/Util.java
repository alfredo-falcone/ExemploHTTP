package br.com.falcone.exemplohttp.util;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.falcone.exemplohttp.communication.NewsRetrofitService;
import br.com.falcone.exemplohttp.model.AppDatabase;
import br.com.falcone.exemplohttp.model.Artigo;
import br.com.falcone.exemplohttp.model.ResultadoRetrofit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alfredo on 12/08/2017.
 */

public class Util {

    public static final String URL_SERVICO = "https://newsapi.org/v1/articles?source=national-geographic&sortBy=top&apiKey=c213b48804674275b759eb25f82c2a3c";
    public static final String URL_BASE_SERVICO = "https://newsapi.org/v1/";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static AppDatabase banco = null;

    public static AppDatabase getBanco(Context contexto){
        if(banco == null){
            banco = Room.databaseBuilder(contexto,
                    AppDatabase.class, "roomDBExemplo").build();
        }
        return banco;
    }
    public static boolean IsConnected(Context contexto){
        ConnectivityManager manager = (ConnectivityManager)contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected())
            return true;

        return false;
    }

    public static InputStream GetStream(){

        InputStream retorno = null;
        try{
            URL endereco = new URL(URL_SERVICO);
            HttpURLConnection connection = (HttpURLConnection) endereco.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setDoInput(true);
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                retorno = connection.getInputStream();
            }
        }
        catch(MalformedURLException m){
            m.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    public static String StreamToString(InputStream stream) {
        String retorno = null;
        if(stream != null){
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int temDados = 0;
            try {
                while((temDados = stream.read(bytes)) > 0) {
                out.write(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            retorno = new String(out.toByteArray());
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return retorno;
    }

    public static List<Artigo> ParseJSON(String texto){
        List<Artigo>  retorno = new ArrayList<>();
        try {

            JSONArray array = new JSONObject(texto).getJSONArray("articles");
            for(int i = 0; i < array.length(); i++ ){
                JSONObject objeto = (JSONObject) array.get(i);
                String autor = objeto.getString("author");
                String descricao =  objeto.getString("description");
                String titulo = objeto.getString("title");
                String url = objeto.getString("urlToImage");


                Artigo artigo = new Artigo(autor, descricao, titulo, url);
                retorno.add(artigo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return retorno;
    }

    public static List<Artigo> CarregarArtigos(){
        return ParseJSON(StreamToString(GetStream()));
    }

    public static List<Artigo> CarregarArtigosOkHTTP() {
        OkHttpClient cliente = new OkHttpClient();
        List<Artigo>  retorno = new ArrayList<>();
        RequestBody body = RequestBody.create(JSON, "");
        try {
            Response resposta = cliente.newCall(new Request.Builder().url(URL_SERVICO).build()).execute();
            retorno = ParseJSON(resposta.body().string());
            resposta.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }


    public static List<Artigo> CarregarArtigosRetrofit() {
        List<Artigo>  retorno = new ArrayList<>();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE_SERVICO)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        NewsRetrofitService servico = retrofit.create(NewsRetrofitService.class);
        // "https://newsapi.org/v1/articles?source=national-geographic&sortBy=top&apiKey=c213b48804674275b759eb25f82c2a3c";
        Call<ResultadoRetrofit> resultado = servico.listArtigos("national-geographic", "top", "c213b48804674275b759eb25f82c2a3c");

        try {
            retrofit2.Response<ResultadoRetrofit> resposta = resultado.execute();
            retorno = resposta.body().getArticles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
