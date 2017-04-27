package igor.firefly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 4/4/2017.
 */

public class EventsHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ff.db";
    private static final String DATABASE_PATH = "/data/data/igor.firefly/databases/" + DATABASE_NAME;
    private static final String TABLE_NAME = "users";
    private static final String TABLE_NAME2 = "events";
    private static final String TABLE_NAME3 = "tags";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_POPULARITY = "popularity";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ORGAN = "organ";
    private static final String COLUMN_TAG = "tag";

    public static SQLiteDatabase db;
    public Context context;
    private static final String CREATE_TABLE_USERS = "create table users (_id integer primary key autoincrement, " +
            "name text not null, email text not null, pass text not null, phone text, organ numeric not null)";

    private static final String CREATE_TABLE_TAGS = "create table tags (_id integer primary key autoincrement, " +
            "name text not null)";

    private static final String CREATE_TABLE_EVENTS = "create table events (_id integer primary key autoincrement, " +
            "name text not null, description text not null, address text not null, price integer, popularity integer, " +
            "organ integer not null, tag integer not null, " +
            "FOREIGN KEY (organ) REFERENCES users(_id), " +
            "FOREIGN KEY (tag) REFERENCES tags(Eve_id))";

    private static final String DATABASE_ALTER_EVENTS_1 = "ALTER TABLE "
            + TABLE_NAME2 + " ADD COLUMN " + COLUMN_ORGAN + " string;";

    private static final String DATABASE_ALTER_EVENTS_2 = "ALTER TABLE "
            + TABLE_NAME2 + " ADD COLUMN " + COLUMN_TAG + " string;";

    EventsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_TAGS);
            db.execSQL(CREATE_TABLE_EVENTS);

            db.close();
        }catch (SQLiteException e){}

        Log.d("Create users: ", "Creating complete");
    }

    public boolean checkDataBase(){
        try{
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){}

        if(db != null)
            db.close();

        return db == null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            if (oldVersion < 2) {
                db.execSQL(DATABASE_ALTER_EVENTS_1);
            }
            if (oldVersion < 3) {
                db.execSQL(DATABASE_ALTER_EVENTS_2);
            }
        }catch (SQLiteException e){}
    }

    public void insertUser(User user){
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, user.getName());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PASS, user.getPass());
            values.put(COLUMN_PHONE, user.getPhone());
            values.put(COLUMN_ORGAN, user.isOrgan());

            db.insert(TABLE_NAME, null, values);
        }catch (SQLiteException e){}
    }

    public void insertEvent(Event event){
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, event.getName());
            values.put(COLUMN_DESC, event.getDescription());
            values.put(COLUMN_ADDRESS, event.getAddress());
            values.put(COLUMN_PRICE, event.getPrice());
            values.put(COLUMN_POPULARITY, event.getPopularity());
            values.put(COLUMN_TAG, event.getTag());
            values.put(COLUMN_ORGAN, event.getOrgan());

            db.insert(TABLE_NAME2, null, values);
        }catch (SQLiteException e){}
    }

    public void insertTag(Tag tag){
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, tag.getName());

            db.insert(TABLE_NAME3, null, values);
        }catch (SQLiteException e){}
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(0));
                    user.setName(cursor.getString(1));
                    user.setEmail(cursor.getString(2));
                    user.setPass(cursor.getString(3));
                    user.setPhone(cursor.getString(4));
                    switch (cursor.getInt(5)) {
                        case 0:
                            user.setOrgan(false);
                            break;
                        case 1:
                            user.setOrgan(true);
                            break;
                        default:
                            user.setOrgan(false);
                            Log.d("User error: ", "Setting organiser value failed!");
                            break;
                    }
                    userList.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLiteException e){}
        return userList;
    }

    public List<Event> getAllEvents() {
        List<Event> labels = new ArrayList<>();

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2, null);

            if (cursor.moveToFirst()) {
                do {
                    Event e = new Event();
                    e.setId(cursor.getInt(0));
                    e.setName(cursor.getString(1));
                    e.setDescription(cursor.getString(2));
                    e.setAddress(cursor.getString(3));
                    e.setPrice(cursor.getFloat(4));
                    e.setPopularity(cursor.getFloat(5));
                    e.setOrgan(cursor.getInt(6));
                    e.setTag(cursor.getInt(7));

                    labels.add(e);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (SQLiteException e){}
        return labels;
    }

    public List<Tag> getAllTags() {
        List<Tag> tagList = new ArrayList<>();

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME3, null);

            if (cursor.moveToFirst()) {
                do {
                    Tag tag = new Tag();
                    tag.setId(cursor.getInt(0));
                    tag.setName(cursor.getString(1));

                    tagList.add(tag);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (SQLiteException e){}
        return tagList;
    }

    public List<Event> searchEventsByName(String name) {
        List<Event> eventsList1 = getAllEvents();
        List<Event> eventsList2 = new ArrayList<>();
        name = name.trim();
        name = name.toLowerCase();

        for (Event e: eventsList1){
            if (e.getName().equalsIgnoreCase(name) || e.getName().toLowerCase().contains(name))
                eventsList2.add(e);
        }
        return eventsList2;
    }
}
