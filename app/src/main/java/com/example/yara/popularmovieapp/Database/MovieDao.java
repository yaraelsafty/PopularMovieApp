package com.example.yara.popularmovieapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Yara on 20-Dec-18.
 */
@Dao
public interface MovieDao {
    @Query(" SELECT * FROM Movies")
    LiveData<List<MovieEntry>> loadAllMovies();

    @Insert
    void  insertMovie (MovieEntry movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void  updateMovie (MovieEntry movieEntry);

    @Query("delete from Movies where movie_id=:id")
    void  deleteMovie (int id);

}
