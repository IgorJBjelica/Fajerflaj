package igor.firefly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class UpdateEventActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mAddress;
    private EditText mDesc;
    private Spinner mSpinner;
    private EditText mPrice;
    private Event event = new Event();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        Bundle getBundle = this.getIntent().getExtras();
        final Event event = getBundle.getParcelable("event");

        mTitle = (EditText) findViewById(R.id.editTextTitle);
        mTitle.setText(event.getName());
    }
}
