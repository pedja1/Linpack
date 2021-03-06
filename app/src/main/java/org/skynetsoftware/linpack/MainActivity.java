package org.skynetsoftware.linpack;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements Runnable
{
    private TextView mflopsTextView;
    private TextView nresTextView;
    private TextView timeTextView;
    private TextView precisionTextView;
    private ResultsListAdapter adapter;
    private DatabaseHandler db;
    private Button start_single;
    private Handler linpackHandler;
    private Handler uiHandler;

    private long startTime;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        HandlerThread thread = new HandlerThread("linpack");
        thread.start();
        linpackHandler = new Handler(thread.getLooper());
        uiHandler = new Handler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mflopsTextView = findViewById(R.id.mflops);
        nresTextView = findViewById(R.id.nres);
        timeTextView = findViewById(R.id.time);
        precisionTextView = findViewById(R.id.precision);

        start_single = findViewById(R.id.start_single);
        start_single.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View p1)
            {
                mflopsTextView.setText(R.string.running_benchmark);
                nresTextView.setText("0");
                timeTextView.setText("0");
                precisionTextView.setText("0");
                start_single.setEnabled(false);
                startLinpack();
            }
        });
        ListView resultsList = findViewById(R.id.list);
        adapter = new ResultsListAdapter(this);
        resultsList.setAdapter(adapter);
        db = new DatabaseHandler(this);
        populateList();
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.result_dialog_layout, null);
                TextView mflopsTextView = view.findViewById(R.id.mflops);
                TextView nresTextView = view.findViewById(R.id.nres);
                TextView timeTextView = view.findViewById(R.id.time);
                TextView precisionTextView = view.findViewById(R.id.precision);
                Result e = db.getResultByDate(adapter.getItem(position).date);

                mflopsTextView.setText(String.valueOf(e.mflops));
                if (e.mflops < 30)
                {
                    mflopsTextView.setTextColor(Color.RED);
                }
                else
                {
                    mflopsTextView.setTextColor(Color.GREEN);
                }
                nresTextView.setText(String.valueOf(e.nres));
                if (e.nres > 5)
                {
                    nresTextView.setTextColor(Color.YELLOW);
                }
                else if (e.nres > 10)
                {
                    nresTextView.setTextColor(Color.RED);
                }
                else
                {
                    nresTextView.setTextColor(Color.GREEN);
                }
                timeTextView.setText(String.valueOf(e.time));
                precisionTextView.setText(String.valueOf(e.precision));

                builder.setNegativeButton(getResources().getString(R.string.close), null);

                builder.setView(view);
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    private void startLinpack()
    {
        linpackHandler.post(this);
    }

    private void populateList()
    {
        adapter.clear();
        List<Result> results = db.getAllResults();
        Collections.reverse(results);
        for (Result r : results)
        {
            adapter.add(r);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void run()
    {
        startTime = System.currentTimeMillis();
        final Result result = runLinpack(Result.class);
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                result.time = System.currentTimeMillis() - (double)startTime;
                startTime = 0;
                result.date = new Date();

                db.addResult(result);

                mflopsTextView.setText(String.format(Locale.US, "%.3f", result.mflops));
                mflopsTextView.setTextColor(result.mflops < 200 ? Color.RED : Color.GREEN);
                nresTextView.setText(String.format(Locale.US, "%.2f", result.nres));
                if (result.nres > 5)
                {
                    nresTextView.setTextColor(Color.YELLOW);
                }
                else if (result.nres > 10)
                {
                    nresTextView.setTextColor(Color.RED);
                }
                else
                {
                    nresTextView.setTextColor(Color.GREEN);
                }
                timeTextView.setText(result.time / 1000 + "s");
                precisionTextView.setText(String.valueOf(result.precision));
                populateList();
                start_single.setEnabled(true);
            }
        });
    }

    public native Result runLinpack(Class mClass);

    static
    {
        System.loadLibrary("linpack-jni");
    }
}
