package igor.firefly;

/**
 * Created by Igor on 4/5/2017.
 */

public class Event {
    private int id;
    private String name;
    private String description;
    private String address;
    private float price;
    private float popularity;
    private int organ_id;
    private int tag_id;

    public Event(){

    }

    public Event(int id, String name, String description, String address, float price, float popularity, int organ_id, int tag_id) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setAddress(address);
        this.setPrice(price);
        this.setPopularity(popularity);
        this.setOrgan(organ_id);
        this.setTag(tag_id);
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getOrgan() {
        return organ_id;
    }

    public void setOrgan(int organ_id) {
        this.organ_id = organ_id;
    }

    public int getTag() {
        return tag_id;
    }

    public void setTag(int tag_id) {
        this.tag_id = tag_id;
    }
}
