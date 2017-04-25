package igor.firefly;
///////////////////////////////////////////////
/**
 * Created by Igor on 4/5/2017.
 */

public class User {
    private int id;
    private String name;
    private String email;
    private String pass;
    private String phone;
    private boolean organ;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOrgan() {
        return organ;
    }

    public void setOrgan(boolean organ) {
        this.organ = organ;
    }
}
