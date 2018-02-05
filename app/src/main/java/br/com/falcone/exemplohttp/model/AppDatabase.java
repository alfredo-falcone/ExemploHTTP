package br.com.falcone.exemplohttp.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Alfredo on 04/02/2018.
 */

@Database(entities = {Artigo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ArtigoDao getArtigoDao();
}
