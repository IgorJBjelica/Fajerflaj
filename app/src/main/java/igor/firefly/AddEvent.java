package igor.firefly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddEvent extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_add_event);

        Bundle getBundle = this.getIntent().getExtras();
        user = getBundle.getParcelable("user");

        mTitle = (EditText) findViewById(R.id.editTextTitle);
        mAddress = (EditText) findViewById(R.id.editTextAddress);
        mDesc = (EditText) findViewById(R.id.editTextDesc);
        mPrice = (EditText) findViewById(R.id.editTextPrice);

        Button btnAdd = (Button) findViewById(R.id.btn_Add);
        btnAdd.setOnClickListener(this);

        Button btnReset = (Button) findViewById(R.id.btn_Reset);
        btnReset.setOnClickListener(this);

        mSpinner = (Spinner) findViewById(R.id.simple_spinner);
        loadSpinnerData();
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Add:
                if (addEvent()) {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }
                break;
            case R.id.btn_Reset:
                resetActivity();
                break;
        }
    }

    private boolean testValues(){
        ///////////////////////////////////Testing input variable values/////////////////////////////////////////////
        String title = mTitle.getText().toString();
        title = title.trim();
        String address = mAddress.getText().toString();
        address = address.trim();
        String desc = mDesc.getText().toString();
        desc = desc.trim();
        float price = Float.parseFloat(mPrice.getText().toString().equals("")? "10.0":mPrice.getText().toString());
        String tag = mSpinner.getSelectedItem().toString();

        if (title.isEmpty()) {
            mTitle.setError("Title cannot be empty!");
            mTitle.requestFocus();
            return false;
        }
        event.setName(mTitle.getText().toString());

        if (address.isEmpty()) {
            mAddress.setError("Address cannot be empty!");
            mAddress.requestFocus();
            return false;
        }
        event.setAddress(mAddress.getText().toString());

        if (desc.isEmpty()) {
            mDesc.setError("Description cannot be empty!");
            mDesc.requestFocus();
            return false;
        }
        event.setDescription(mDesc.getText().toString());

        if (price >= 0.0f && price <= 5.0f){
            event.setPrice(Float.parseFloat(mPrice.getText().toString()));
        }
        else{
            mPrice.setError("Price cannot be less than 0.0 or more than 5.0!");
            mPrice.requestFocus();
            return false;
        }

        switch (tag){
            case "Provod":
            case "Karijera":
            case "Razonoda":
                event.setTag(mSpinner.getSelectedItemPosition());
                break;
            case "Event tag":
                TextView errorText = (TextView)mSpinner.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Invalid selection for Tag!");
                mSpinner.requestFocus();
                return false;
        }

        if (user != null)
            event.setOrgan(user.getId());
        else{
            showMessage("User error!", "User data isn't viable!");
            return false;
        }

        return true;
    }

    private boolean addEvent(){
        if (testValues()){
            try {
                EventsHelper db = new EventsHelper(this);
                db.insertEvent(event);
                return true;
            }catch (Exception e){}
        }
        return false;
    }

    private void resetActivity(){
        mTitle.setText("");
        mAddress.setText("");
        mDesc.setText("");
        mSpinner.setSelection(0, true);
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
