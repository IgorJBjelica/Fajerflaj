package igor.firefly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Bundle getBundle = this.getIntent().getExtras();
        User user = getBundle.getParcelable("user");
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        EventsHelper db = new EventsHelper(this);
        List<Event> eventsList;
        
        if(user == null)
            showMessage("User error!", "No user info available!");
        else {
            eventsList = db.searchEventsByOrganizer(user.getId());

            if (eventsList != null) {
                for (final Event e : eventsList) {
                    Button btn_event = new Button(this);
                    btn_event.setId(e.getId());
                    btn_event.setText(e.getName());
                    ll.addView(btn_event);
                    btn_event.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("event", e);
                            startActivity(new Intent(EventsActivity.this, AddEvent.class).putExtras(bundle));
                        }
                    });
                }
            } else
                showMessage("Make a new event!", "There are no events made by user " + user.getName());
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
