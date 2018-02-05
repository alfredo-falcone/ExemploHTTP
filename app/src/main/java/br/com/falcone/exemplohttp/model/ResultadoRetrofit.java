package br.com.falcone.exemplohttp.model;

import java.util.List;

/**
 * Created by Alfredo on 12/08/2017.
 */

public class ResultadoRetrofit {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Artigo> getArticles() {
        return articles;
    }

    public void setArticles(List<Artigo> articles) {
        this.articles = articles;
    }

    private String status;
    private List<Artigo> articles;
}
