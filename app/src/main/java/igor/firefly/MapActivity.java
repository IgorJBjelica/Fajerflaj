package igor.firefly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle getBundle = this.getIntent().getExtras();
        Event event = getBundle.getParcelable("event");

        TextView tv = (TextView) findViewById(R.id.tvMapa);
        tv.setText(event.getName());
    }
}
