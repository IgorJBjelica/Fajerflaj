package igor.firefly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
    private SeekBar mSeekBar;
    private SeekBar mSeekBarMin;
    private float minPrice;
    private SeekBar mSeekBarMax;
    private float maxPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSeekBarMin = (SeekBar) findViewById(R.id.seekBarMinCena);
        mSeekBarMin.setOnSeekBarChangeListener(this);

        mSeekBarMax = (SeekBar) findViewById(R.id.seekBarMaxCena);
        mSeekBarMax.setOnSeekBarChangeListener(this);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText2 = (EditText) findViewById(R.id.editTextMinCena);
        mEditText3 = (EditText) findViewById(R.id.editTextMaxCena);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);

        mTextView = (TextView) findViewById(R.id.textView3);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setOnClickListener(this);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        Button btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        mSpinner = (Spinner) findViewById(R.id.simple_spinner);
        loadSpinnerData();
    }

    private void loadSpinnerData() {
        EventsHelper db = new EventsHelper(getApplicationContext());
        List<Tag> tags = db.getAllTags();
        List<String> tagsList = new ArrayList<>();

        tagsList.add("Tip desavanja");

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
                break;
            case R.id.btnReset:
                resetActivity();
                break;
        }
    }

    private void resetActivity(){
        mSeekBar.setProgress(0);
        mSeekBarMin.setProgress(0);
        mSeekBarMax.setProgress(0);
        mEditText.setText("");
        mEditText2.setText("");
        mEditText3.setText("");
        mRatingBar.setProgress(0);
        mSpinner.setSelection(0, true);
        mTextView.setText("");
    }

    private boolean testValues(){
        ///////////////////////////////Put in test values of all variables/////////////////////////////////////////////
        minPrice = Float.parseFloat(mEditText2.getText().toString().equals("") ? "0": mEditText2.getText().toString());
        maxPrice = Float.parseFloat(mEditText3.getText().toString().equals("") ? "0": mEditText3.getText().toString());
        if(minPrice > maxPrice) {
            showMessage("Greska!", "Minimalna cena ne sme biti veca od maksimalne!");
            return false;
        }
        return true;
    }

    private void searchEvents(){
        if(testValues()) {
            EventsHelper db = new EventsHelper(this);
            List<Event> eventsList = db.getAllEvents();

            String freeText = mEditText.getText().toString();
            if (!freeText.trim().isEmpty() || !freeText.trim().equals("")) {
                List<Event> list = db.searchEventsByName(eventsList, freeText);

                for( Event e : db.searchEventsByAddress(eventsList, freeText))
                    if(!list.contains(e))
                        list.add(e);

                for( Event e : db.searchEventsByDescription(eventsList, freeText))
                    if(!list.contains(e))
                        list.add(e);

                eventsList = list;
            }

            if (minPrice != 0 || maxPrice != 0)
                eventsList = db.searchEventsByPrice(eventsList, minPrice, maxPrice);

            float popularity = mRatingBar.getRating();
            if (popularity != 0)
                eventsList = db.searchEventsByPopularity(eventsList, popularity);

            String tag = mSpinner.getSelectedItem().toString();
            if (!tag.equals(mSpinner.getItemAtPosition(0).toString()))
                eventsList = db.searchEventsByTag(eventsList, tag);

            float distance = mSeekBar.getProgress();

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("list", (ArrayList<Event>) eventsList);
            startActivity(new Intent(SearchActivity.this, ResultsActivity.class).putExtras(bundle));
        }
    }

    @Override
    public void onProgressChanged(SeekBar mSB, int progress, boolean fromUser){
        switch (mSB.getId()){
            case R.id.seekBar:{
                mTextView.setText(String.valueOf(Integer.valueOf(progress)).concat(" km"));
                break;
            }
            case R.id.seekBarMinCena:{
                mEditText2.setText(String.valueOf(Integer.valueOf(progress)));
                break;
            }
            case R.id.seekBarMaxCena:{
                mEditText3.setText(String.valueOf(Integer.valueOf(progress)));
                break;
            }
        }
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