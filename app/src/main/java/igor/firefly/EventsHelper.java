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
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_LONG = "longitude";
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
            "organ integer not null, tag integer not null, latitude integer, longitude integer, " +
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
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH, null);
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_TAGS);
            db.execSQL(CREATE_TABLE_EVENTS);

            db.close();
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}

        Log.d("Create users: ", "Creating complete");
    }

    public boolean checkDataBase(){
        try{
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);

            if(db != null)
                db.close();
        }catch(SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}

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
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
    }

    public void insertUser(User user){
        if(checkDataBase())
            onCreate(db);

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
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
    }

    public void insertEvent(Event event){
        if(checkDataBase())
            onCreate(db);

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
            values.put(COLUMN_LAT, event.getLatitude());
            values.put(COLUMN_LONG, event.getLongitude());

            db.insert(TABLE_NAME2, null, values);
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
    }

    public void insertTag(Tag tag){
        if(checkDataBase())
            onCreate(db);

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, tag.getName());

            db.insert(TABLE_NAME3, null, values);
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
    }

    public void updateEvent(int id, String name, String address, String desc, float price, int tag){
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            if (!name.equals("")){
                cv.put("name", name);
            }
            if (!address.equals("")){
                cv.put("address", address);
                cv.put("latitude", 0);
                cv.put("longitude", 0);
            }
            if (!desc.equals("")){
                cv.put("description", desc);
            }
            if (price >= 0.0f && price <= 5.0f){
                cv.put("price", price);
            }
            if (tag > 0){
                cv.put("tag", tag);
            }

            db.update("events", cv, "_id=" + id, null);
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
    }

    public  void updateEventLatLng(int id, double latitude, double longitude){
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            if (latitude > 0.0){
                cv.put("latitude", latitude);
            }
            if (longitude > 0.0){
                cv.put("longitude", longitude);
            }

            db.update("events", cv, "_id=" + id, null);
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                    user.setPass(cursor.getString(cursor.getColumnIndex(COLUMN_PASS)));
                    user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                    switch (cursor.getInt(cursor.getColumnIndex(COLUMN_ORGAN))) {
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
        }catch(SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
        return userList;
    }

    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2, null);

            if (cursor.moveToFirst()) {
                do {
                    Event e = new Event();
                    e.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    e.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    e.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
                    e.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)));
                    e.setLatitude(cursor.getFloat(cursor.getColumnIndex(COLUMN_LAT)));
                    e.setLongitude(cursor.getFloat(cursor.getColumnIndex(COLUMN_LONG)));
                    e.setPrice(cursor.getFloat(cursor.getColumnIndex(COLUMN_PRICE)));
                    e.setPopularity(cursor.getFloat(cursor.getColumnIndex(COLUMN_POPULARITY)));
                    e.setOrgan(cursor.getInt(cursor.getColumnIndex(COLUMN_ORGAN)));
                    e.setTag(cursor.getInt(cursor.getColumnIndex(COLUMN_TAG)));

                    list.add(e);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
        return list;
    }

    public List<Tag> getAllTags() {
        List<Tag> tagList = new ArrayList<>();

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME3, null);

            if (cursor.moveToFirst()) {
                do {
                    Tag tag = new Tag();
                    tag.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    tag.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));

                    tagList.add(tag);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (SQLiteException e){Log.d("SQLiteException", "Sqlite error!");}
        return tagList;
    }

    public List<User> searchUsers(String name, int id) {
        List<User> usersList1 = getAllUsers();
        List<User> usersList2 = new ArrayList<>();

        for (User u: usersList1){
            if (u.getName().equalsIgnoreCase(name) || u.getId() == id)
                usersList2.add(u);
        }
        return usersList2;
    }

    public List<Tag> searchTags(String name, int id) {
        List<Tag> tagsList1 = getAllTags();
        List<Tag> tagsList2 = new ArrayList<>();

        for (Tag t: tagsList1){
            if (t.getName().equalsIgnoreCase(name) || t.getId() == id)
                tagsList2.add(t);
        }
        return tagsList2;
    }

    public List<Event> searchEventsByName(List<Event> list, String name) {
        List<Event> eventsList = new ArrayList<>();
        name = name.trim();
        name = name.toLowerCase();

        for (Event e: list){
            if (e.getName().equalsIgnoreCase(name) || e.getName().toLowerCase().contains(name))
                eventsList.add(e);
        }
        return eventsList;
    }

    public List<Event> searchEventsByAddress(List<Event> list, String address) {
        List<Event> eventsList = new ArrayList<>();
        address = address.trim();
        address = address.toLowerCase();

        for (Event e: list){
            if (e.getAddress().equalsIgnoreCase(address) || e.getAddress().toLowerCase().contains(address))
                eventsList.add(e);
        }
        return eventsList;
    }

    public List<Event> searchEventsByDescription(List<Event> list, String desc) {
        List<Event> eventsList = new ArrayList<>();
        desc = desc.trim();
        desc = desc.toLowerCase();

        for (Event e: list){
            if (e.getDescription().equalsIgnoreCase(desc) || e.getDescription().toLowerCase().contains(desc))
                eventsList.add(e);
        }
        return eventsList;
    }

    public List<Event> searchEventsByPrice(List<Event> list, float min, float max) {
        List<Event> eventsList = new ArrayList<>();

        if((min >= 0) && (max >= 0)) {
            for (Event e : list) {
                if ((e.getPrice() >= min) && (e.getPrice() <= max))
                    eventsList.add(e);
            }
        }
        else if((min >= 0) && (max <= 0)) {
            for (Event e : list) {
                if (e.getPrice() >= min)
                    eventsList.add(e);
            }
        }
        else if((min <= 0) && (max >= 0)) {
            for (Event e : list) {
                if (e.getPrice() <= max)
                    eventsList.add(e);
            }
        }
        else eventsList = list;

        return eventsList;
    }

    public List<Event> searchEventsByPopularity(List<Event> list, float pop) {
        List<Event> eventList = new ArrayList<>();

        if(pop > 0) {
            for (Event e : list) {
                if (e.getPrice() >= pop)
                    eventList.add(e);
            }
        }

        return eventList;
    }

    public List<Event> searchEventsByTag(List<Event> list, String tagName) {
        List<Event> eventList = new ArrayList<>();
        Tag tag = searchTags(tagName, 0).get(0);

        if(tag != null) {
            for (Event e : list) {
                if (e.getTag() == tag.getId())
                    eventList.add(e);
            }
        }

        return eventList;
    }

    public List<Event> searchEventsByOrganizer(int userID) {
        List<Event> list = getAllEvents();
        List<Event> eventList = new ArrayList<>();
        User user = searchUsers("", userID).get(0);

        if(user != null) {
            for (Event e : list) {
                if (e.getOrgan() == user.getId())
                    eventList.add(e);
            }
        }

        return eventList;
    }
}
