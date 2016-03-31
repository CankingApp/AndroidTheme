package com.canking.androidtheme;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


/**
 * Created by changxing on 15-11-20.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final int THEME_GREEN = 1;
    public static final int ThEME_BLUE = 2;
    public static final int THEME_PURPLE = 3;
    public static final int THEME_GREY = 4;
    public static final int THEME_TEAL = 5;
    public static final int THEME_BROWN = 6;
    public static final int THEME_ORANGE = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setBaseTheme();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags =
                    (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        super.onCreate(savedInstanceState);
    }

    private void setBaseTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                Consts.SHARE_NAME, MODE_PRIVATE);
        int themeType = sharedPreferences.getInt("theme_type", 0);
        int themeId;
        switch (themeType) {
            case THEME_GREEN:
                themeId = R.style.AppTheme_Base_Green;
                break;
            case ThEME_BLUE:
                themeId = R.style.AppTheme_Base_Blue;
                break;
            case THEME_ORANGE:
                themeId = R.style.AppTheme_Base_Orange;
                break;
            case THEME_TEAL:
                themeId = R.style.AppTheme_Base_Teal;
                break;
            case THEME_BROWN:
                themeId = R.style.AppTheme_Base_Brown;
                break;
            case THEME_GREY:
                themeId = R.style.AppTheme_Base_Grey;
                break;
            case THEME_PURPLE:
                themeId = R.style.AppTheme_Base_Purple;
                break;
            default:
                themeId = R.style.AppTheme_Base_Default;
        }
        setTheme(themeId);
    }

}
