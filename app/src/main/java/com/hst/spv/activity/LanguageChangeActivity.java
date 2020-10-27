package com.hst.spv.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hst.spv.R;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.LocaleHelper;
import com.hst.spv.utils.PreferenceStorage;

import org.json.JSONObject;

public class LanguageChangeActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener {

    private RelativeLayout englishLayout, tamilLayout;
    private ImageView imgEnglishCheck, imgTamilCheck;
    private Button languageConfirm;
    private Boolean englishCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_change);

        englishLayout = findViewById(R.id.english);
        tamilLayout = findViewById(R.id.tamil);
        imgEnglishCheck = findViewById(R.id.img_eng_check);
        imgTamilCheck = findViewById(R.id.img_tamil_check);
        languageConfirm = findViewById(R.id.confirm_language);

        englishLayout.setOnClickListener(this);
        tamilLayout.setOnClickListener(this);
        languageConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == englishLayout) {
            imgEnglishCheck.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_language_check));
            imgTamilCheck.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_language_uncheck));
            englishCheck = true;
        }
        if (v == tamilLayout) {
            imgTamilCheck.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_language_check));
            imgEnglishCheck.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_language_uncheck));
            englishCheck = false;
        }
        if (v == languageConfirm) {
            if (englishCheck) {
                Toast.makeText(getApplicationContext(), "App language is set to English", Toast.LENGTH_SHORT).show();
                LocaleHelper.setLocale(LanguageChangeActivity.this, "en");
                PreferenceStorage.saveLang(this, "english");
            } else {
                Toast.makeText(getApplicationContext(), "மொழி தமிழுக்கு அமைக்கப்பட்டுள்ளது", Toast.LENGTH_SHORT).show();
                LocaleHelper.setLocale(LanguageChangeActivity.this, "ta");
                PreferenceStorage.saveLang(this, "tamil");
            }
            recreate();
        }
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
