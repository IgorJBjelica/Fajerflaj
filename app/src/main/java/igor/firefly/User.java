package igor.firefly;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Igor on 4/5/2017.
 */

public class User implements Parcelable {
    private int id;
    private String name;
    private String email;
    private String pass;
    private String phone;
    private boolean organ;

    public User(){

    }

    public User(int id, String name, String email, String pass, String phone, boolean organ) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setPass(pass);
        this.setPhone(phone);
        this.setOrgan(organ);
    }

    public User(Parcel in) {
        this.setId(in.readInt());
        this.setName(in.readString());
        this.setEmail(in.readString());
        this.setPass(in.readString());
        this.setPhone(in.readString());
        this.setOrgan(in.readByte() != 0);
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

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.pass);
        dest.writeString(this.phone);
        dest.writeByte((byte) (this.organ ? 1:0));
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>()
    {
        public User createFromParcel(Parcel in)
        {
            return new User(in);
        }
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };
}
