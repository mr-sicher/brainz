package de.sicher.brainz;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_DATA_KEY = "EXTRA_DATA_KEY";

    private ListView list;
    private Button add;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = (Button) findViewById(R.id.addNew);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EXTRA_DATA_KEY, 0);
                startActivity(intent);
            }
        });

        helper = new DBHelper(this);
        final Cursor cursor = helper.getAllData();
        String[] columns = new String[] {
                DBHelper.TRACKING_COLUMN_TIME,
                DBHelper.TRACKING_COLUMN_STRENGTH
        };
        int[] widgets = new int[] {
                R.id.dataTime,
                R.id.dataStength
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.info,
                cursor, columns, widgets, 0);
        list = (ListView) findViewById(R.id.dataList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor itemCursor = (Cursor) MainActivity.this.list.getItemAtPosition(position);
                int dataId = itemCursor.getInt(itemCursor.getColumnIndex(DBHelper.TRACKING_COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra(EXTRA_DATA_KEY, dataId);
                startActivity(intent);
            }
        });
    }
}
