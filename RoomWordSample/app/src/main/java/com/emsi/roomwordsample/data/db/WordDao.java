package com.emsi.roomwordsample.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.emsi.roomwordsample.model.Word;

import java.util.List;

@Dao
public interface WordDao {

    // Permettre l'insertion du même mot plusieurs fois en passant
    // une stratégie de résolution de conflits.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

 /*   @Query("SELECT * FROM word_table ORDER BY word ASC")
    List<Word> getAlphabetizedWords();*/

    @Query("SELECT * from word_table ORDER BY word ASC")
    LiveData<List<Word>> getAlphabetizedWords();


    // Ajoutez ces nouvelles méthodes
    @Query("DELETE FROM word_table WHERE word = :word")
    void deleteWord(String word);

    @Update
    void update(Word word);
}
