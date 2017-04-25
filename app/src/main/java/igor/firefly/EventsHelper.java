package igor.firefly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 4/4/2017.
 */

public class EventsHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fajerflaj.db";
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
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_TAGS);
        db.execSQL(CREATE_TABLE_EVENTS);

        Log.d("Create users: ", "Creating complete");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_EVENTS_1);
        }
        if (oldVersion < 3) {
            db.execSQL(DATABASE_ALTER_EVENTS_2);
        }
    }

    public void insertUser(User user){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASS, user.getPass());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_ORGAN, user.isOrgan());

        db.insert(TABLE_NAME, null, values);
    }

    public void insertEvent(Event event){
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
    }

    public void insertTag(Tag tag){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, tag.getName());

        db.insert(TABLE_NAME3, null, values);
    }

    public void dropUsers() {
        String dropUsers = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropUsers);
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null)) {

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
        }
        return userList;
    }

    public List<Event> getAllEvents() {
        List<Event> labels = new ArrayList<>();

        try (Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2, null)) {

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
        }
        return labels;
    }

    public List<Tag> getAllTags() {
        List<Tag> tagList = new ArrayList<>();

        try (Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME3, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Tag tag = new Tag();
                    tag.setId(cursor.getInt(0));
                    tag.setName(cursor.getString(1));

                    tagList.add(tag);
                } while (cursor.moveToNext());
            }
        }
        return tagList;
    }

    public List<Event> searchEventsByName(String name){
        List<Event> eventsList = new ArrayList<>();

        try (Cursor cursor = db.rawQuery("Select * From " + TABLE_NAME2 + "Where Name = " + name, null)){

            if(cursor.moveToFirst()){
                do{
                    Event e = new Event();
                    e.setId(cursor.getInt(0));
                    e.setName(cursor.getString(1));
                    e.setDescription(cursor.getString(2));
                    e.setAddress(cursor.getString(3));
                    e.setPrice(cursor.getFloat(4));
                    e.setPopularity(cursor.getFloat(5));
                    e.setOrgan(cursor.getInt(6));
                    e.setTag(cursor.getInt(7));

                    eventsList.add(e);
                }while (cursor.moveToNext());
            }
        }
        return eventsList;
    }
}
