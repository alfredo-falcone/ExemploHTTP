package br.com.falcone.exemplohttp.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Alfredo on 04/02/2018.
 */
@Dao
public interface ArtigoDao {

    @Query("SELECT * FROM artigo")
    List<Artigo> getAll();

    @Query("SELECT * FROM artigo WHERE id IN (:artigoIds)")
    List<Artigo> loadAllByIds(int[] artigoIds);

    @Query("SELECT * FROM artigo WHERE titulo LIKE :first AND "
            + "titulo LIKE :last LIMIT 1")
    Artigo findByTitle(String first, String last);

    @Insert
    void insertAll(Artigo... artigos);

    @Insert
    void insertAll(List<Artigo> artigos);

    @Delete
    void delete(Artigo artigo);
    @Delete
    void deleteAll(List<Artigo> artigo);

    @Query("DELETE FROM artigo")
    void deleteAll();
}
