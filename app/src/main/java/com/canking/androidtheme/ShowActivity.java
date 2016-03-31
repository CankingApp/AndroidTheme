package com.canking.androidtheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 精确秒表
 *
 * @author cxcx
 */
public class ShowActivity extends BaseActivity {


    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView timeView = (TextView) findViewById(R.id.timeView);
        timeView.setTextScaleX(0.7f);
    }


    /**
     * 菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    private void showSale() {
        Toast.makeText(this, "If you like, give me a star!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_theme:
                Intent i = new Intent(this, ThemeActivity.class);
                startActivityForResult(i, 0);
                break;
            case R.id.menu_sale:
                showSale();
                break;
            case R.id.menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.about));
                builder.setIcon(getResources().getDrawable(R.mipmap.ic_action_good));
                builder.setMessage("Touch me in:\n http://weibo.com/canking666/ \n\n" + getString(R.string.right));
                builder.setNegativeButton(getString(R.string.ok), null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.menu_quit:
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                finish();

                Intent i = new Intent(this, ShowActivity.class);
                startActivity(i);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, getString(R.string.press_again),
                    Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            finish();
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
        }
    }

}
