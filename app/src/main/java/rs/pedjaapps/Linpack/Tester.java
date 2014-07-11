package rs.pedjaapps.Linpack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Tester extends Activity {

    TextView mflopsTextView;
    TextView nresTextView;
    TextView timeTextView;
    TextView precisionTextView;
    ListView resultsList;
    ResultsListAdapter adapter;
    DatabaseHandler db;
	Button start_single;
	Button start_multi;
    Bundle mInfo[];
    public final static String MFLOPS = "MFLOPS";
    public final static String RESIDN = "RESIDN";
    public final static String TIME   = "TIME";
    public final static String EPS    = "EPS";

	private static String TAG;
	int mRound;
    int mNow;
    int mIndex;

    protected long mTesterStart = 0;
    protected long mTesterEnd   = 0;

    private boolean mNextRound = true;

    protected boolean mDropTouchEvent     = true;
    protected boolean mDropTrackballEvent = true;
	
    protected String getTag() {
        return "Linpack";
    }

    protected int sleepBeforeStart() {
        return 1000;
    }

    protected int sleepBetweenRound() {
        return 200;
    }

    protected void oneRound() {
        Linpack.main(mInfo[mNow - 1]);
        decreaseCounter();
    }

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TAG = getTag();

		mRound = 80;
		mIndex = -1;

        mNow   = mRound;
        int length = mRound;
        mInfo = new Bundle[length];
        for (int i = 0; i < length; i++) {
            mInfo[i] = new Bundle();
        }

        mflopsTextView = (TextView)findViewById(R.id.mflops);
        nresTextView = (TextView)findViewById(R.id.nres);
        timeTextView = (TextView)findViewById(R.id.time);
        precisionTextView = (TextView)findViewById(R.id.precision);
        
        start_single = (Button)findViewById(R.id.start_single);
		start_single.setOnClickListener(new View.OnClickListener(){

				public void onClick(View p1)
				{
					mflopsTextView.setText(R.string.running_benchmark);
					nresTextView.setText("0");
					timeTextView.setText("0");
					precisionTextView.setText("0");
					start_single.setEnabled(false);
					start_multi.setEnabled(false);
					startTester(0);
				}
			});
		start_multi = (Button)findViewById(R.id.start_multi);
		start_multi.setOnClickListener(new View.OnClickListener(){

				public void onClick(View p1)
				{
					mflopsTextView.setText(R.string.running_benchmark);
					nresTextView.setText("0");
					timeTextView.setText("0");
					precisionTextView.setText("0");
					start_single.setEnabled(false);
					start_multi.setEnabled(false);
					startTester(1);
				}
			});
		resultsList = (ListView)findViewById(R.id.list);
		adapter = new ResultsListAdapter(this, R.layout.results_row);
		resultsList.setAdapter(adapter);
		db = new DatabaseHandler(this);
		populateList();
		resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						Tester.this);

				LayoutInflater inflater = (LayoutInflater) Tester.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.result_dialog_layout, null);
				TextView mflopsTextView = (TextView)view.findViewById(R.id.mflops);
		        TextView nresTextView = (TextView)view.findViewById(R.id.nres);
		        TextView timeTextView = (TextView)view.findViewById(R.id.time);
		        TextView precisionTextView = (TextView)view.findViewById(R.id.precision);
		        ResultsDatabaseEntry e = db.getResultByDate(adapter.getItem(position).getDate());
				mflopsTextView.setText(e.getMflops()+"");
				if(e.getMflops()<30){
					mflopsTextView.setTextColor(Color.RED);
				}
				else{
					mflopsTextView.setTextColor(Color.GREEN);
				}
				nresTextView.setText(e.getNres()+"");
				if(e.getNres()>5){
					nresTextView.setTextColor(Color.YELLOW);
				}
				else if(e.getNres()>10){
					nresTextView.setTextColor(Color.RED);
				}
				else{
					nresTextView.setTextColor(Color.GREEN);
				}
				timeTextView.setText(e.getTime()+"s");
				precisionTextView.setText(""+e.getPrecision());
			    	
					builder.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
							
							}

						});
					
					builder.setView(view);
					AlertDialog alert = builder.create();
					alert.show();
				
			}
		});
        
    }

    private void populateList(){
    	adapter.clear();
    	List<ResultsDatabaseEntry> results = db.getAllResults();
		Collections.sort(results, new ReverseList());
		for(ResultsDatabaseEntry e : results){
			adapter.add(new ResultsEntry(e.getMflops(), e.getDate()));
		}
		adapter.notifyDataSetChanged();
    }
    
    static class ReverseList implements Comparator<ResultsDatabaseEntry>
    {
            @Override
            public int compare(ResultsDatabaseEntry p1, ResultsDatabaseEntry p2)
            {
            if (p1.getId() < p2.getId()) return 1;
            if (p1.getId() > p2.getId()) return -1;
            return 0;
        }   

    }
    
    public static void average(Bundle result, Bundle[] list) {

        if (result == null) {
            result = new Bundle();
        }

        if (list == null) {
            Log.i(TAG, "Array is null");
            return;
        }

        int length = list.length;
        double mflops_total  = 0.0;
        double residn_total  = 0.0;
        double time_total    = 0.0;
        double eps_total     = 0.0;

        for (int i = 0; i < length; i ++) {
            Bundle info = list[i];

            if (info == null) {
                Log.i(TAG, "one item of array is null!");
                return;
            }

            mflops_total  += info.getDouble(MFLOPS);
            residn_total  += info.getDouble(RESIDN);
            time_total    += info.getDouble(TIME);
            eps_total     += info.getDouble(EPS);
        }

        result.putDouble(MFLOPS, mflops_total / length);
        result.putDouble(RESIDN, residn_total / length);
        result.putDouble(TIME, time_total / length);
        result.putDouble(EPS, eps_total  / length);
    }
	
	@Override
    protected void onPause() {
        super.onPause();
		if(isTesterFinished()==false){
           interruptTester();
		}
    }

    protected void startTester(int code) {
    	switch(code){
    	case 0:
    		TesterThread thread = new TesterThread(sleepBeforeStart(), sleepBetweenRound());
            thread.start();
            break;
    	case 1:
    	     new TesterAsyncTask(sleepBeforeStart(), sleepBetweenRound()).execute();
    	    break;
    	}
        
   }

    public void interruptTester() {
        mNow = 0;
        finish();
    }

    /**
     * Call this method if you finish your testing.
     *
     * @param start The starting time of testing round
     * @param end The ending time of testing round
     */
    public void finishTester(final long start, final long end) {
		final Bundle result = new Bundle();
        average(result, mInfo);
        final DecimalFormat mflopsFormat = new DecimalFormat("0.000");
		final DecimalFormat nResFormat = new DecimalFormat("0.00");
		SimpleDateFormat f = new SimpleDateFormat("dd MMM yy HH:mm:ss");
        db.addResult(new ResultsDatabaseEntry(Double.parseDouble(mflopsFormat.format(result.getDouble(MFLOPS, 0.0))),
        		Double.parseDouble(nResFormat.format(result.getDouble(RESIDN, 0.0))),
        		(double)(end-start)/1000,
        		result.getDouble(EPS, 0.0),
        		f.format(System.currentTimeMillis())
        		));
        runOnUiThread(new Runnable(){
				public void run() {
					
					mflopsTextView.setText(mflopsFormat.format(result.getDouble(MFLOPS, 0.0)));
					if(result.getDouble(MFLOPS, 0.0)<30){
						mflopsTextView.setTextColor(Color.RED);
					}
					else{
						mflopsTextView.setTextColor(Color.GREEN);
					}
					nresTextView.setText(nResFormat.format(result.getDouble(RESIDN, 0.0)));
					if(result.getDouble(RESIDN, 0.0)>5){
						nresTextView.setTextColor(Color.YELLOW);
					}
					else if(result.getDouble(RESIDN, 0.0)>10){
						nresTextView.setTextColor(Color.RED);
					}
					else{
						nresTextView.setTextColor(Color.GREEN);
					}
					timeTextView.setText((double)(end-start)/1000 +"s");
					precisionTextView.setText(""+result.getDouble(EPS, 0.0));
					populateList();
					Tester.this.start_single.setEnabled(true);
					Tester.this.start_multi.setEnabled(true);
				}});
		mNow = mRound;
    }

   

    public void resetCounter() {
        mNow = mRound;
    }

    public void decreaseCounter() {
       
        mNow = mNow - 1;
        mNextRound = true;
    }

    public boolean isTesterFinished() {
        return (mNow <= 0);
    }

    class TesterThread extends Thread {
        int mSleepingStart;
        int mSleepingTime;
        TesterThread(int sleepStart, int sleepPeriod) {
            mSleepingStart = sleepStart;
            mSleepingTime  = sleepPeriod;
        }

        private void lazyLoop() throws Exception {
            while (!isTesterFinished()) {
                if (mNextRound) {
                    mNextRound = false;
                    oneRound();
                } else {
                    sleep(mSleepingTime);
                }
            }
        }


        public void run() {
            try {
                sleep(mSleepingStart);

                long start = SystemClock.uptimeMillis();

                lazyLoop();

                long end = SystemClock.uptimeMillis();
                finishTester(start, end);
            } catch (Exception e) {
				e.printStackTrace();
            }
        }
    }
    
    private class TesterAsyncTask extends AsyncTask<String, Void, Long[]> {

    	int mSleepingStart;
        int mSleepingTime;
        TesterAsyncTask(int sleepStart, int sleepPeriod) {
            mSleepingStart = sleepStart;
            mSleepingTime  = sleepPeriod;
        }
        private void lazyLoop() throws Exception {
            while (!isTesterFinished()) {
                if (mNextRound) {
                    mNextRound = false;
                    oneRound();
                } else {
                    Thread.sleep(mSleepingTime);
                }
            }
        }
        
		@Override
		protected Long[] doInBackground(String... args) {
			long start = 0;
			long end = 0 ;
			try {
                Thread.sleep(mSleepingStart);

                start = SystemClock.uptimeMillis();

                lazyLoop();

                end = SystemClock.uptimeMillis();
                //finishTester(start, end);
            } catch (Exception e) {
				e.printStackTrace();
            }
			return new Long[]{start, end};
		}

		
		@Override
		protected void onPostExecute(Long[] result) {
			finishTester(result[0], result[1]);
		}
	}

}
