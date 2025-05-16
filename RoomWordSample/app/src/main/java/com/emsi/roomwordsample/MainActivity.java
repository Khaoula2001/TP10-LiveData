package com.emsi.roomwordsample;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.emsi.roomwordsample.adapter.WordListAdapter;
import com.emsi.roomwordsample.model.Word;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;


import com.emsi.roomwordsample.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WordViewModel mWordViewModel;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WordListAdapter mAdapter = new WordListAdapter();
        binding.contentMain.recyclerview.setAdapter(mAdapter);
        binding.contentMain.recyclerview.setHasFixedSize(true);
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, words -> {
            // Mettre à jour la copie en cache des mots dans l'adaptateur.
            mAdapter.setWords(words);
        });


        mAdapter.setOnItemClickListener(new WordListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Word word) {
                // Ouvrir l'activité d'édition
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                intent.putExtra("EDIT_WORD", word.getWord());
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }

            @Override
            public void onDeleteClick(Word word) {
                // Afficher une boîte de dialogue de confirmation
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Supprimer le mot")
                        .setMessage("Voulez-vous vraiment supprimer \"" + word.getWord() + "\"?")
                        .setPositiveButton("Supprimer", (dialog, which) -> {
                            mWordViewModel.deleteWord(word);
                            Toast.makeText(MainActivity.this, "Mot supprimé", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            }
        });

        setSupportActionBar(binding.toolbar);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String wordText = data.getStringExtra(NewWordActivity.EXTRA_REPLY);
            String originalWord = data.getStringExtra("ORIGINAL_WORD");

            if (originalWord != null) {
                // C'est une mise à jour
                Word word = new Word(wordText);
                mWordViewModel.updateWord(word);
                Toast.makeText(this, "Mot mis à jour", Toast.LENGTH_SHORT).show();
            } else {
                // C'est une nouvelle insertion
                Word word = new Word(wordText);
                mWordViewModel.insert(word);
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}