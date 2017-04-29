package igor.firefly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;


public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private EventsHelper db = new EventsHelper(this);
    private Bundle bundle;

    private String getOrgan (int id){
        List<User> organ = db.searchUsers("", id);

        for (User u : organ) {
            if (u.getId() == id) return u.getName();
        }
        return "Greska pri ucitavanju organizatora.";
    }

    private String getTag (int id){
        List<Tag> tag = db.searchTags("", id);

        for (Tag t : tag) {
            if (t.getId() == id) return t.getName();
        }
        return "Greska pri ucitavanju tag-a.";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        bundle = this.getIntent().getExtras();
        Event event = bundle.getParcelable("event");

        if (event != null) {
            TextView tvName = (TextView) findViewById(R.id.textViewName);
            tvName.setText(event.getName());

            TextView tvAdd = (TextView) findViewById(R.id.textViewAddress);
            tvAdd.setText(event.getAddress());

            TextView tvDesc = (TextView) findViewById(R.id.textViewDescription);
            tvDesc.setText(event.getDescription());

            TextView tvPrice = (TextView) findViewById(R.id.textViewPrice);
            String p = "" + event.getPrice();
            tvPrice.setText(p);

            TextView tvOrgan = (TextView) findViewById(R.id.textViewOrgan);
            tvOrgan.setText(getOrgan(event.getOrgan()));

            TextView tvType = (TextView) findViewById(R.id.textViewType);
            tvType.setText(getTag(event.getTag()));

            RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
            rb.setRating(event.getPopularity());

            TextView tvMap = (TextView) findViewById(R.id.tv_Map);
            tvMap.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_Map:
                startActivity(new Intent(EventActivity.this, MapActivity.class).putExtras(bundle));
                break;
        }
    }
}
