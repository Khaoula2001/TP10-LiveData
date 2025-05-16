package com.emsi.roomwordsample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "unique.key.for.REPLY";
    private EditText mEditWordView;
    private String mOriginalWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditWordView = findViewById(R.id.edit_word);
        final Button button = findViewById(R.id.button_save);

        // Vérifier si nous sommes en mode édition
        mOriginalWord = getIntent().getStringExtra("EDIT_WORD");
        if (mOriginalWord != null) {
            mEditWordView.setText(mOriginalWord);
            button.setText("Mettre à jour");
        }

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = mEditWordView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, word);
                if (mOriginalWord != null) {
                    replyIntent.putExtra("ORIGINAL_WORD", mOriginalWord);
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}