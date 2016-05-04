package Utils;

/**
 * Created by user on 3/24/2016.
 */
public class Store {
    private int storeId;
    private String storeName;
    private String storeAddress;
    private String contact;
    private String contactPerson;
    private int zoneId;
    private String zoneName;
    private int categoryId;
    private String categoryName;
    private String carryName;

    public Store(){}

    public Store(int storeId, String storeName, String storeAddress, String contact, String contactPerson,
                 int zoneId, String zoneName, int categoryId, String categoryName){
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.contact = contact;
        this.contactPerson = contactPerson;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Store(int storeId, String storeName, String storeAddress, String contact, String contactPerson,
                 int zoneId, int categoryId){
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.contact = contact;
        this.contactPerson = contactPerson;
        this.zoneId = zoneId;
        this.categoryId = categoryId;

    }

    public String getCarryName() {
        return carryName;
    }

    public void setCarryName(String carryName) {
        this.carryName = carryName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
