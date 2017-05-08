package igor.firefly;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class UpdateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTitle;
    private EditText mAddress;
    private EditText mDesc;
    private EditText mPrice;
    private Spinner mSpinner;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        Bundle getBundle = this.getIntent().getExtras();
        event = getBundle.getParcelable("event");

        if (event != null) {
            mTitle = (EditText) findViewById(R.id.editTextTitle);
            mTitle.setHint(event.getName());

            mAddress = (EditText) findViewById(R.id.editTextAddress);
            mAddress.setHint(event.getAddress());

            mDesc = (EditText) findViewById(R.id.editTextDesc);
            mDesc.setHint(event.getDescription());

            mPrice = (EditText) findViewById(R.id.editTextPrice);
            mPrice.setHint("" + event.getPrice());

            Button btnUpdate = (Button) findViewById(R.id.btn_Update);
            btnUpdate.setOnClickListener(this);

            Button btnReset = (Button) findViewById(R.id.btn_Reset);
            btnReset.setOnClickListener(this);

            mSpinner = (Spinner) findViewById(R.id.update_spinner);
            loadSpinnerData();
        }
        else
            showMessage("Event error!", "Event wasn't initialized properly!");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Update:
                if (updateEvent()) {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }
                break;
            case R.id.btn_Reset:
                resetActivity();
                break;
        }
    }

    private void loadSpinnerData() {
        EventsHelper db = new EventsHelper(getApplicationContext());
        List<Tag> tags = db.getAllTags();
        List<String> tagsList = new ArrayList<>();

        tagsList.add("Event tag");

        for (Tag t: tags){
            tagsList.add(t.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tagsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(dataAdapter);
        mSpinner.setSelection(event.getTag());
    }

    private boolean setValues(){
        ///////////////////////////////Setting input variable values to be updated////////////////////////////////////////
        float price = Float.parseFloat(mPrice.getText().toString().equals("")? "10.0":mPrice.getText().toString());

        event.setName(mTitle.getText().toString().trim());
        event.setAddress(mAddress.getText().toString().trim());
        event.setDescription(mDesc.getText().toString().trim());
        event.setPrice(price);
        event.setTag(mSpinner.getSelectedItemPosition());

        return true;
    }

    private boolean updateEvent(){
        if (setValues()){
            try {
                EventsHelper db = new EventsHelper(this);
                db.updateEvent(event.getId(),
                        event.getName(),
                        event.getAddress(),
                        event.getDescription(),
                        event.getPrice(),
                        event.getTag());
                return true;
            }catch (Exception e){
                Log.d("SQLiteException", "Could not update event!");
            }
        }
        return false;
    }

    private void resetActivity(){
        mTitle.setText("");
        mAddress.setText("");
        mDesc.setText("");
        mSpinner.setSelection(event.getTag(), true);
        mPrice.setText("");
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
