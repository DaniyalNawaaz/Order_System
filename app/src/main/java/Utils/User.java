package Utils;

/**
 * Created by user on 3/22/2016.
 */
public class User {
    private int id;
    private String name;
    private String address;
    private String contact;
    private String cnic;
    private int user_role;
    private String user_name;
    private String password;
    private double latitude;
    private double longitude;
    private String gcm_id;
    private int is_login;

    public User(){}

    public User(int id, String name, String address, String contact, String cnic, String user_name, String password){
        this.id = id;
        this.name=name;
        this.address=address;
        this.contact=contact;
        this.cnic =cnic;
        this.user_name=user_name;
        this.password=password;
    }

    public User(int id, String name, String address, String contact, String cnic, String user_name, String password, double latitude, double longitude, String gcm_id){
        this.id = id;
        this.name=name;
        this.address=address;
        this.contact=contact;
        this.cnic =cnic;
        this.user_name=user_name;
        this.password=password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gcm_id = gcm_id;
    }

    public User(int id, String name, String address, String contact, String cnic,int user_role, String user_name, String password, double latitude, double longitude, String gcm_id){
        this.id = id;
        this.name=name;
        this.address=address;
        this.contact=contact;
        this.cnic =cnic;
        this.user_role = user_role;
        this.user_name=user_name;
        this.password=password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gcm_id = gcm_id;
    }

    public User(int id, String name, String address, String contact, String cnic,int user_role, String user_name, String password, double latitude, double longitude, String gcm_id,int is_login){
        this.id = id;
        this.name=name;
        this.address=address;
        this.contact=contact;
        this.cnic =cnic;
        this.user_role = user_role;
        this.user_name=user_name;
        this.password=password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gcm_id = gcm_id;
        this.is_login = is_login;
    }

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public int getUser_role() {
        return user_role;
    }

    public void setUser_role(int user_role) {
        this.user_role = user_role;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
