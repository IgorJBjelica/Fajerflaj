package igor.firefly;

import java.util.List;

/**
 * Created by Igor on 4/5/2017.
 */

public class Tag {
    private int id;
    private String name;
    private List<Event> events;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
