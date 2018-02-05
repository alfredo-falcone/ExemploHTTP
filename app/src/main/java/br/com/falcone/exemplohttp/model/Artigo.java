package br.com.falcone.exemplohttp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by Alfredo on 12/08/2017.
 */
@Entity
public class Artigo {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @SerializedName("author")
    private String autor;

    @SerializedName("description")
    private String descricao;

    @SerializedName("title")
    private String titulo;
    @SerializedName("urlToImage")
    private String urlImagem;


    public Artigo(String autor, String descricao, String titulo, String urlImagem) {
        this.autor = autor;
        this.descricao = descricao;
        this.titulo = titulo;
        this.urlImagem = urlImagem;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
