package igor.firefly;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Bundle getBundle = this.getIntent().getExtras();
        List<Event> eventsList = getBundle.getParcelableArrayList("list");
        LinearLayout ll = (LinearLayout) findViewById(R.id.coord);

        if(eventsList != null) {
            for (Event e : eventsList) {
                Button btn_event = new Button(getApplicationContext());
                btn_event.setId(e.getId());
                btn_event.setText(e.getName());
                ll.addView(btn_event);
            }
        }
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}