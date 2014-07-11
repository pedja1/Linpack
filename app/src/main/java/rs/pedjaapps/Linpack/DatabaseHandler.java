package rs.pedjaapps.Linpack;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;


public class DatabaseHandler extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 8;

    // Database Name
    private static final String DATABASE_NAME = "LinpackResults.db";

    // table names
	private static final String TABLE_RESULTS = "favorites";
	
    // Table Columns names
	
	
	private static final String[] filds = {"_id", "mflops", "nres", "time", "precision",
			"date"};
	
	
	
    public DatabaseHandler(Context context)
	{
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
	{
       
		
			
		String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_RESULTS + "("
			+ filds[0] + " INTEGER PRIMARY KEY,"
			+ filds[1] + " DOUBLE,"
			+ filds[2] + " DOUBLE,"
			+ filds[3] + " DOUBLE,"
			+ filds[4] + " DOUBLE,"
			+ filds[5] + " STRING"
			+
			")";

		db.execSQL(CREATE_LIST_TABLE);
	 }

	
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
	
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addResult(ResultsDatabaseEntry result)
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(filds[1], result.getMflops());
        values.put(filds[2], result.getNres());
        values.put(filds[3], result.getTime());
        values.put(filds[4], result.getPrecision());
        values.put(filds[5], result.getDate());

        // Inserting Row
        db.insert(TABLE_RESULTS, null, values);
        db.close(); // Closing database connection
    }
    
    public ResultsDatabaseEntry getResult(int id)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RESULTS, new String[] {filds[0],
        		filds[1],
        		filds[2],
        		filds[3],
        		filds[4],
        		filds[5]
									 
									 
									}, filds[0] + "=?",
								 new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ResultsDatabaseEntry items = new ResultsDatabaseEntry(Integer.parseInt(cursor.getString(0)),
									  cursor.getDouble(1),
									  cursor.getDouble(2),
									  cursor.getDouble(3),
									  cursor.getDouble(4),
									  cursor.getString(5)
									  );
        // return list
        db.close();
        cursor.close();
        return items;
    }
	
	public ResultsDatabaseEntry getResultByDate(String date)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RESULTS, new String[] {filds[0],
									 filds[1],
									 filds[2],
									 filds[3],
									 filds[4],
									 filds[5]


								 }, filds[5] + "=?",
								 new String[] { date }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ResultsDatabaseEntry items = new ResultsDatabaseEntry(Integer.parseInt(cursor.getString(0)),
															cursor.getDouble(1),
															cursor.getDouble(2),
															cursor.getDouble(3),
															cursor.getDouble(4),
															cursor.getString(5)
															);
        // return list
        db.close();
        cursor.close();
        return items;
    }
	
	/*public MoviesDatabaseEntry getMovieByIndex(String table, int id)
	{
       // Select All Query
        String selectQuery = "SELECT * FROM " +table+" LIMIT 1 OFFSET "+id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
		MoviesDatabaseEntry list = new MoviesDatabaseEntry();
        if (cursor!=null)
		{
            cursor.moveToFirst();
            
                list.set_id(Integer.parseInt(cursor.getString(0)));
                list.set_title(cursor.getString(1));
                list.set_runtime(cursor.getString(2));
                list.set_rating(cursor.getDouble(3));
				list.set_genres(cursor.getString(4));
				list.set_type(cursor.getString(5));
				list.set_lang(cursor.getString(6));
				list.set_poster(cursor.getString(7));
				list.set_url(cursor.getString(8));
				list.set_director(cursor.getString(9));
				list.set_actors(cursor.getString(10));
				list.set_plot(cursor.getString(11));
				list.set_year(cursor.getInt(12));
				list.set_country(cursor.getString(13));
				list.set_date(cursor.getInt(14));
			    list.set_ur(cursor.getDouble(15));
				list.set_imdb_id(cursor.getString(16));
			    


                
            
        }

        // return list
        db.close();
        cursor.close();
        return list;
    }*/
	
	public List<ResultsDatabaseEntry> getAllResults()
	{
        List<ResultsDatabaseEntry> lists = new ArrayList<ResultsDatabaseEntry>();
        // Select All Query
       
        String selectQuery = "SELECT  * FROM " + TABLE_RESULTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
		{
            do {
            	ResultsDatabaseEntry list = new ResultsDatabaseEntry();
                list.setId(Integer.parseInt(cursor.getString(0)));
                list.setMflops(cursor.getDouble(1));
                list.setNres(cursor.getDouble(2));
                list.setTime(cursor.getDouble(3));
				list.setPrecision(cursor.getDouble(4));
				list.setDate(cursor.getString(5));
				
                // Adding  to list
                lists.add(list);
            } while (cursor.moveToNext());
        }

        // return list
        db.close();
        cursor.close();
        return lists;
    }
   
	/*public void deleteMovie(MoviesDatabaseEntry movies, String table)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, filds[0] + " = ?",
				  new String[] { String.valueOf(movies.get_id()) });
        
        db.close();
    }*/
	
	
	
	/*public boolean movieExists(String table, String movieName) {
    	SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table, new String[] { filds[0],
        		filds[1],
        		filds[2],
        		filds[3],
        		filds[4],
        		filds[5],
        		filds[6],
        		filds[7],
        		filds[8],
        		filds[9],
        		filds[10],
        		filds[11],
        		filds[12],
        		filds[13],
        		filds[14],
				filds[15],
				filds[16]
									}, filds[1] + "=?",
								 new String[] { movieName }, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
	}*/
	
	/*public int updateMovie(MoviesDatabaseEntry movie, String title, String table)
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(filds[1], movie.get_title());
        values.put(filds[2], movie.get_runtime());
        values.put(filds[3], movie.get_rating());
        values.put(filds[4], movie.get_genres()); 
        values.put(filds[5], movie.get_type());
        values.put(filds[6], movie.get_lang()); 
        values.put(filds[7], movie.get_poster());
        values.put(filds[8], movie.get_url()); 
        values.put(filds[9], movie.get_director()); 
        values.put(filds[10], movie.get_actors());
        values.put(filds[11], movie.get_plot()); 
        values.put(filds[12], movie.get_year());
        values.put(filds[13], movie.get_country()); 
        values.put(filds[14], movie.get_date());
		values.put(filds[15], movie.get_ur());
		values.put(filds[16], movie.get_imdb_id());
        
        return db.update(table, values, filds[1] + " = ?",
						 new String[] { title });
        }*/
	
	public int getResultsCount()
	{
        String countQuery = "SELECT  * FROM " + TABLE_RESULTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
       int count = cursor.getCount();
	  // cursor.close();
      // db.close();
        // return count
        return count;
    }

}

