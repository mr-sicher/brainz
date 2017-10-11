package de.sicher.brainz;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class EditActivity extends AppCompatActivity implements View.OnClickListener {


    private DBHelper dbHelper ;
    EditText nameEditText, dateEditText,timeEditText, strengthEditText;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    int dataID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataID = getIntent().getIntExtra(MainActivity.EXTRA_DATA_KEY, 0);

        setContentView(R.layout.activity_edit);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        dateEditText = (EditText) findViewById(R.id.editTextDate);
        timeEditText = (EditText) findViewById(R.id.editTextTime);
        strengthEditText = (EditText) findViewById(R.id.editTextStrength);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        if(dataID > 0) {
            saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            Cursor rs = dbHelper.getData(dataID);
            rs.moveToFirst();
            String name = rs.getString(rs.getColumnIndex(DBHelper.TRACKING_COLUMN_NAME));
            String date = rs.getString(rs.getColumnIndex(DBHelper.TRACKING_COLUMN_DATE));
            String time = rs.getString(rs.getColumnIndex(DBHelper.TRACKING_COLUMN_TIME));
            int strength = rs.getInt(rs.getColumnIndex(DBHelper.TRACKING_COLUMN_STRENGTH));
            if (!rs.isClosed()) {
                rs.close();
            }

            nameEditText.setText(name);
            nameEditText.setFocusable(false);
            nameEditText.setClickable(false);

//TODO Clicklistener
            dateEditText.setText(date);
            dateEditText.setFocusable(false);
            dateEditText.setClickable(false);

            timeEditText.setText(time);
            timeEditText.setFocusable(false);
            timeEditText.setClickable(false);

//TODO Rating picker
            strengthEditText.setText(Integer.toString(strength));
            strengthEditText.setFocusable(false);
            strengthEditText.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistPerson();
                return;
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                nameEditText.setEnabled(true);
                nameEditText.setFocusableInTouchMode(true);
                nameEditText.setClickable(true);

                dateEditText.setEnabled(true);
                dateEditText.setFocusableInTouchMode(true);
                dateEditText.setClickable(true);
                dateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDatePicker();
                    }
                });

                timeEditText.setEnabled(true);
                timeEditText.setFocusableInTouchMode(true);
                timeEditText.setClickable(true);
                timeEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openTimePicker();
                    }
                });

                strengthEditText.setEnabled(true);
                strengthEditText.setFocusableInTouchMode(true);
                strengthEditText.setClickable(true);
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteData)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteData(dataID);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle(getString(R.string.deleteDataDialog));
                d.show();
        }
    }

    public void persistPerson() {
        if(dataID > 0) {
            if(dbHelper.updateData(dataID, nameEditText.getText().toString(),
                    dateEditText.getText().toString(),
                    timeEditText.getText().toString(),
                    Integer.parseInt(strengthEditText.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Data Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Data Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertData(nameEditText.getText().toString(),
                    dateEditText.getText().toString(),
                    timeEditText.getText().toString(),
                    Integer.parseInt(strengthEditText.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert data", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void openDatePicker(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getApplicationContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        StringBuilder builder = new StringBuilder();
                        builder.append(dayOfMonth)
                                .append(".")
                                .append(monthOfYear+1)
                                .append(".")
                                .append(year);
                        dateEditText.setText(builder);
                    }
                }, mYear, mMonth, mDay);
        dialog.show();
    }
    private void openTimePicker(){
        final Calendar c = Calendar.getInstance();

        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getApplicationContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        StringBuilder builder = new StringBuilder();
                        if(hourOfDay < 10){
                            builder.append("0");
                        }
                        builder.append(hourOfDay)
                                .append(":");
                        if(minute < 10){
                            builder.append("0");
                        }
                        builder.append(minute);
                        timeEditText.setText(builder);
                    }
                }, mHour, mMinute, true);
        dialog.show();

    }
}
