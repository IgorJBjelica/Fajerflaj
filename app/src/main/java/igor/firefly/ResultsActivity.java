package igor.firefly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Event> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Bundle getBundle = this.getIntent().getExtras();
        eventsList = getBundle.getParcelableArrayList("list");
        LinearLayout ll = (LinearLayout) findViewById(R.id.coord);

        if(eventsList != null) {
            for (Event e : eventsList) {
                Button btn_event = new Button(this);
                btn_event.setId(e.getId());
                btn_event.setText(e.getName());
                ll.addView(btn_event);
                btn_event.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view){
        for (Event e : eventsList) {
            if(view.getId() == e.getId()) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("event", e);
                startActivity(new Intent(ResultsActivity.this, MapActivity.class).putExtras(bundle));
            }
        }
    }
}