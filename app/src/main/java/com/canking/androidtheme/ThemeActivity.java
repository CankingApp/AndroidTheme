package com.canking.androidtheme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by changxing on 16-3-30.
 */
public class ThemeActivity extends AppCompatActivity {
//    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_activity);
    }

    public void onClick(View v) {
        Log.e("changxing", "click");
        SharedPreferences sharedPreferences = getSharedPreferences(
                Consts.SHARE_NAME, MODE_PRIVATE);
        int typeBefor = sharedPreferences.getInt("theme_type", 0);
        int typeSetted = typeBefor;
        int themeId;
        int id = v.getId();
        switch (id) {
            case R.id.btn0:
                typeSetted = 0;
                break;
            case R.id.btn1:
                typeSetted = 1;
                break;
            case R.id.btn2:
                typeSetted = 2;
                break;
            case R.id.btn3:
                typeSetted = 3;
                break;
            case R.id.btn4:
                typeSetted = 4;
                break;
            case R.id.btn5:
                typeSetted = 5;
                break;
            case R.id.btn6:
                typeSetted = 6;
                break;
            case R.id.btn7:
                typeSetted = 7;
                break;
        }
        if (typeBefor != typeSetted) {
            sharedPreferences.edit().putInt("theme_type", typeSetted).commit();
            setResult(Activity.RESULT_OK);
        }
        finish();
    }
}
