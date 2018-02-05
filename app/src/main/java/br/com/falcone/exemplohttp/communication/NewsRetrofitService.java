package br.com.falcone.exemplohttp.communication;

import java.util.List;

import br.com.falcone.exemplohttp.model.Artigo;
import br.com.falcone.exemplohttp.model.ResultadoRetrofit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Alfredo on 12/08/2017.
 */

public interface NewsRetrofitService {
    @GET("articles")
    Call<ResultadoRetrofit> listArtigos(@Query("source") String source, @Query("sortBy") String sortBy, @Query("apiKey") String chave);
}
