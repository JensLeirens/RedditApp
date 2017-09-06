package mafken.redditapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelperSubreddits extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AndroidReddit.db";
    public static final String SUBREDDITS_TABLE_NAME = "Subreddits";
    public static final String SUBREDDITS_COLUMN_ID = "id";
    public static final String SUBREDDITS_COLUMN_NAME = "name";
    public static final String SUBREDDITS_COLUMN_TITLE = "title";
    public static final String SUBREDDITS_COLUMN_AUTHOR = "author";
    public static final String SUBREDDITS_COLUMN_CREATED = "created";
    public static final String SUBREDDITS_COLUMN_SELFTEXT = "selftext";
    public static final String SUBREDDITS_COLUMN_THUMBNAIL = "thumbnail";
    public static final String SUBREDDITS_COLUMN_NUMCOMMENT = "numcomment";
    public static final String SUBREDDITS_COLUMN_URL = "url";
    public static final String SUBREDDITS_COLUMN_UPS = "ups";

    public DBHelperSubreddits(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table SUBREDDITS " +
                        "(id integer primary key, name text,title text,author text, " +
                        "created text,selftext text, thumbnail text, numcomment text," +
                        " url text, ups text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SUBREDDITS");
        onCreate(db);
    }

    public void clearDatabase(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS SUBREDDITS");
        onCreate(db);
    }

    public boolean insertPerson (String name, String title, String author, long created,
                                 String selftext, String thumbnail, int num_comments, String url, int ups) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("title", title);
        contentValues.put("author", author);
        contentValues.put("created", created);
        contentValues.put("selftext", selftext);
        contentValues.put("thumbnail", thumbnail);
        contentValues.put("numcomment", num_comments);
        contentValues.put("url", url);
        contentValues.put("ups", ups);
        db.insert("SUBREDDITS", null, contentValues);
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SUBREDDITS_TABLE_NAME);
        return numRows;
    }


    public Integer deleteSubreddits (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("SUBREDDITS",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Subreddit> getSubreddits() {
        ArrayList<Subreddit> subreddits = new ArrayList<>();

        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SUBREDDITS", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Subreddit sr = new Subreddit();

            sr.setName(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_NAME)));
            sr.setTitle(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_TITLE)));
            sr.setAuthor(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_AUTHOR)));
            sr.setCreated(Integer.parseInt(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_CREATED))));
            sr.setSelftext(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_SELFTEXT)));
            sr.setThumbnail(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_THUMBNAIL)));
            sr.setNum_comments(Integer.parseInt(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_NUMCOMMENT))));
            sr.setUrl(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_URL)));
            sr.setUps(Integer.parseInt(res.getString(res.getColumnIndex(SUBREDDITS_COLUMN_UPS))));

            subreddits.add(sr);
            res.moveToNext();
        }

        res.close();
        return subreddits;
    }
}