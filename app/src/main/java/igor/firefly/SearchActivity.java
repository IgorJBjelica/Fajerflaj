package igor.firefly;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private EditText mEditText;
    private EditText mEditText2;
    private EditText mEditText3;
    private RatingBar mRatingBar;
    private Spinner mSpinner;
    private TextView mTextView;
    private List<Event> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText2 = (EditText) findViewById(R.id.editText2);
        mEditText3 = (EditText) findViewById(R.id.editText3);

        SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mTextView = (TextView) findViewById(R.id.textView3);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setOnClickListener(this);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        mSpinner = (Spinner) findViewById(R.id.simple_spinner);
        loadSpinnerData();

        try {
            EventsHelper db = new EventsHelper(getApplicationContext());
            Log.d("Reading: ", "Reading all events..");
            eventsList = db.getAllEvents();

            for (Event e : eventsList) {
                String log = "Id: " + e.getId() + " ,Name: " + e.getName() + " ,Description: " + e.getDescription();
                // Writing Contacts to log
                Log.d("Name: ", log);
            }

            db.close();
        }
        catch(Exception ex){
            showMessage("FUCK", ex.getMessage());
        }
    }

    private void loadSpinnerData() {
        EventsHelper db = new EventsHelper(getApplicationContext());
        List<Tag> tags = db.getAllTags();
        List<String> tagsList = new ArrayList<>();

        for (Tag t: tags){
            tagsList.add(t.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tagsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnSearch:
                searchEvents();
                startActivity(new Intent(SearchActivity.this, EventsActivity.class));
                break;
        }
    }

    private void searchEvents(){
        String freeText = mEditText.getText().toString();
        int minPrice = Integer.parseInt(mEditText2.getText().toString());
        int maxPrice = Integer.parseInt(mEditText3.getText().toString());
        float popularity = mRatingBar.getRating();
        String tag = mSpinner.getSelectedItem().toString();
        EventsHelper db = new EventsHelper(getApplicationContext());

        List<Event> eventsList = db.searchEventsByName(freeText);

        mEditText.setText(eventsList.get(0).getId() + eventsList.get(0).getName() + eventsList.get(0).getPrice());
    }

    @Override
    public void onProgressChanged(SeekBar mSeekBar, int progress, boolean fromUser){
        mTextView.setText(String.valueOf(Integer.valueOf(progress)).concat(" km"));
    }

    @Override
    public void onStartTrackingTouch(SeekBar mSeekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar mSeekBar) {

    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}