package igor.firefly;

import android.app.Activity;

/**
 * Created by Igor on 4/5/2017.
 */

public class FillBase {
    public static void initDB(Activity activity) {
        EventsHelper dbHelper = new EventsHelper(activity);

//      Dodavanje korisnika Igor
        User user = new User();
        user.setName("Igor");
        user.setEmail("igorjbjelica@gmail.com");
        user.setPass("igorj");
        user.setOrgan(false);

        dbHelper.insertUser(user);

//      Dodavanje organizatora Vukasin
        user.setName("Vukasin");
        user.setEmail("duduk@gmail.com");
        user.setPass("vuled");
        user.setPhone("064/8884650");
        user.setOrgan(true);

        dbHelper.insertUser(user);

//      Dodavanje Tipova Desavanja u Tags
        Tag tag = new Tag();
        tag.setName("Provod");
        dbHelper.insertTag(tag);

        tag.setName("Karijera");
        dbHelper.insertTag(tag);

        tag.setName("Razonoda");
        dbHelper.insertTag(tag);

//      Dodavanje jednog desavanja
        Event event = new Event();
        event.setName("Vukasin Brajic");
        event.setDescription("Vukasin Brajic pravi rok spektakl u Wurst Platz bar-u.");
        event.setAddress("Cumicevo Sokace bb");
        event.setPopularity(4.2f);
        event.setPrice(3.5f);
        event.setOrgan(2);
        event.setTag(1);

        dbHelper.insertEvent(event);
    }
}
