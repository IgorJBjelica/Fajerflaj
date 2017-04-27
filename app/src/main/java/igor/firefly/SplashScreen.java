package igor.firefly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import static igor.firefly.R.layout.activity_splash_screen;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash_screen);

        FillBase.initDB(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
