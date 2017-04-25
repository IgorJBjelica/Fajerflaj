package igor.firefly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class EventsActivity extends AppCompatActivity {

    private RatingBar mRatingBar;
    private NumberPicker mNumPick;
    private SeekBar mSeekBar;
    private TextView mTextView;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

    }
}
