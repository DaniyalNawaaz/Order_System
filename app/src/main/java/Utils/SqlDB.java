package Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/22/2016.
 */
public class SqlDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "metrodb";

    // Contacts table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_ZONE = "zone";
    private static final String TABLE_STORE = "store";
    private static final String TABLE_OFFLINE_STORE = "offline_store";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_PRODUCT = "product";
    private static final String TABLE_PRODUCT_FLAVOUR = "product_flavour";
    private static final String TABLE_ORDER = "orders";
    private static final String TABLE_OFFLINE_ORDER = "offline_orders";
    private static final String TABLE_OFFLINE_ORDER_PRODUCT = "offline_order_product";
    private static final String TABLE_ORDER_PRODUCT = "order_product";
    private static final String TABLE_INSTRUCTION = "instruction";
    private static final String TABLE_NEWS_NOTIFICATION = "news_notification";
    private static final String TABLE_USER_TARGET = "user_target";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CONTACT = "contact_num";
    private static final String KEY_CNIC = "cnic";
    private static final String KEY_USERROLE = "user_role";
    private static final String KEY_ZONE_ID = "zone_id";
    private static final String KEY_ZONE_NAME = "zone_name";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_GCM_ID = "gcm_id";
    private static final String KEY_IS_LOGIN = "is_local";

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ORDER_ID = "order_id";


    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_CONTACT_PERSON = "contact_person";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_CARRY_NAME = "carry_name";
    private static final String KEY_INSTRUCTION_NAME = "instruction_name";
    private static final String KEY_INSTRUCTION_ID = "instruction_id";

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    private static final String KEY_STATUS = "status";
    private static final String KEY_ORDER_DATE = "order_date";
    private static final String KEY_DELIVERY_DATE = "delivery_date";

    private static final String KEY_PRICE = "price";
    private static final String KEY_PER_CARTON = "discount_per_carton";
    private static final String KEY_DISCOUNT = "discount_price";
    private static final String KEY_DISCOUNT_PER_PRODUCT = "discount_per_product";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_FLAVOUR_ID = "flavour_id";
    private static final String KEY_FLAVOUR_NAME = "flavour_name";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_SUB_PRICE = "sub_price";
    private static final String KEY_QTY = "qty";

    private static final String KEY_TOTAL_TARGET = "total_target";
    private static final String KEY_TOTAL_ACHIEVE = "total_achieve";

    public SqlDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_CONTACT + " TEXT,"
                + KEY_CNIC + " TEXT,"
                + KEY_USERROLE + " INTEGER,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL,"
                + KEY_GCM_ID + " TEXT,"
                + KEY_IS_LOGIN + " INTEGER"+ ")";

        String CREATE_STORE_TABLE = "CREATE TABLE " + TABLE_STORE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_STORE_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_CONTACT + " TEXT,"
                + KEY_CONTACT_PERSON + " TEXT,"
                + KEY_ZONE_ID + " INTEGER,"
                + KEY_CATEGORY_ID + " INTEGER,"
                + KEY_CARRY_NAME + " TEXT" + ")";

        String CREATE_OFFLINE_STORE_TABLE = "CREATE TABLE " + TABLE_OFFLINE_STORE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_STORE_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_CONTACT + " TEXT,"
                + KEY_CONTACT_PERSON + " TEXT,"
                + KEY_ZONE_ID + " INTEGER,"
                + KEY_CATEGORY_ID + " INTEGER,"
                + KEY_CARRY_NAME + " TEXT" + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_NAME + " TEXT"+ ")";

        String CREATE_INSTRUCTION_TABLE = "CREATE TABLE " + TABLE_INSTRUCTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_INSTRUCTION_NAME + " TEXT"+ ")";

        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NEWS_NOTIFICATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT" + ")";

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PRICE + " TEXT"+ ")";

        String CREATE_PRODUCT_FLAVOUR_TABLE = "CREATE TABLE " + TABLE_PRODUCT_FLAVOUR + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_PRODUCT_ID + " INTEGER,"
                + KEY_FLAVOUR_ID + " INTEGER,"
                + KEY_FLAVOUR_NAME + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_SUB_PRICE + " REAL"+ ")";

        String CREATE_ZONE_TABLE = "CREATE TABLE " + TABLE_ZONE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_USER_ID + " INTEGER"+ ")";

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRICE + " TEXT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_STORE_NAME + " TEXT,"
                + KEY_ORDER_DATE + " TEXT,"
                + KEY_PER_CARTON + " REAL,"
                + KEY_DISCOUNT + " REAL,"
                + KEY_DELIVERY_DATE + " TEXT"+ ")";

        String CREATE_ORDER_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_ORDER_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_ORDER_ID + " INTEGER,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_QTY + " INTEGER,"
                + KEY_DISCOUNT_PER_PRODUCT + " REAL,"
                + KEY_FLAVOUR_NAME + " TEXT" + ")";

        String CREATE_OFFLINE_ORDER_TABLE = "CREATE TABLE " + TABLE_OFFLINE_ORDER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_PRICE + " TEXT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_STORE_NAME + " TEXT,"
                + KEY_ORDER_DATE + " TEXT,"
                + KEY_PER_CARTON + " REAL,"
                + KEY_DISCOUNT + " REAL,"
                + KEY_DELIVERY_DATE + " TEXT,"
                + KEY_INSTRUCTION_ID + " TEXT"+")";

        String CREATE_OFFLINE_ORDER_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_OFFLINE_ORDER_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_ORDER_ID + " INTEGER,"
                + KEY_PRODUCT_ID + " INTEGER,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_QTY + " INTEGER,"
                + KEY_DISCOUNT_PER_PRODUCT + " REAL,"
                + KEY_FLAVOUR_ID + " INTEGER,"
                + KEY_FLAVOUR_NAME + " TEXT" + ")";

        String CREATE_USER_TARGET_TABLE = "CREATE TABLE " + TABLE_USER_TARGET + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TOTAL_TARGET + " INTEGER,"
                + KEY_TOTAL_ACHIEVE +" INTEGER" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_STORE_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_NOTIFICATION_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_PRODUCT_FLAVOUR_TABLE);
        db.execSQL(CREATE_INSTRUCTION_TABLE);
        db.execSQL(CREATE_ZONE_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_ORDER_PRODUCTS_TABLE);
        db.execSQL(CREATE_OFFLINE_STORE_TABLE);
        db.execSQL(CREATE_OFFLINE_ORDER_TABLE);
        db.execSQL(CREATE_OFFLINE_ORDER_PRODUCTS_TABLE);
        db.execSQL(CREATE_USER_TARGET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_CONTACT, user.getContact());
        values.put(KEY_CNIC, user.getCnic());
        values.put(KEY_USERROLE, user.getUser_role());
        values.put(KEY_USER_NAME, user.getUser_name());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_LATITUDE, user.getLatitude());
        values.put(KEY_LONGITUDE, user.getLongitude());
        values.put(KEY_GCM_ID, user.getGcm_id());
        values.put(KEY_IS_LOGIN, user.getIs_login());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void addTarget(Target target) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, target.getUserId());
        values.put(KEY_TOTAL_TARGET, target.getTotalTarget());
        values.put(KEY_TOTAL_ACHIEVE, target.getTotalAchieved());
        // Inserting Row
        db.insert(TABLE_USER_TARGET, null, values);
        db.close();
    }

    public void addProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, product.getProductId());
        values.put(KEY_NAME, product.getProductName());
        values.put(KEY_PRICE, product.getPrice());

        // Inserting Row
        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public void addZone(Zone zone){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, zone.getId());
        values.put(KEY_NAME, zone.getZoneName());
        values.put(KEY_USER_ID, zone.getUserId());

        // Inserting Row
        db.insert(TABLE_ZONE, null, values);
        db.close();
    }

    public void addProductFlavour(Product_Flavour product_flavour){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, product_flavour.getProductId());
        values.put(KEY_FLAVOUR_ID, product_flavour.getFlavourId());
        values.put(KEY_FLAVOUR_NAME, product_flavour.getFlavourName());
        values.put(KEY_IMAGE_PATH, product_flavour.getImagePath());
        values.put(KEY_SUB_PRICE, product_flavour.getPrice());

        // Inserting Row
        db.insert(TABLE_PRODUCT_FLAVOUR, null, values);
        db.close();
    }

    public void addStore(Store store) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, store.getStoreId());
        values.put(KEY_STORE_NAME, store.getStoreName());
        values.put(KEY_ADDRESS, store.getStoreAddress());
        values.put(KEY_CONTACT, store.getContact());
        values.put(KEY_CONTACT_PERSON, store.getContactPerson());
        values.put(KEY_ZONE_ID, store.getZoneId());
        values.put(KEY_CATEGORY_ID, store.getCategoryId());
        values.put(KEY_CARRY_NAME, store.getCarryName());

        // Inserting Row
        db.insert(TABLE_STORE, null, values);
        db.close();
    }

    public void addOfflineStore(Store store) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, store.getStoreId());
        values.put(KEY_STORE_NAME, store.getStoreName());
        values.put(KEY_ADDRESS, store.getStoreAddress());
        values.put(KEY_CONTACT, store.getContact());
        values.put(KEY_CONTACT_PERSON, store.getContactPerson());
        values.put(KEY_ZONE_ID, store.getZoneId());
        values.put(KEY_CATEGORY_ID, store.getCategoryId());
        values.put(KEY_CARRY_NAME, store.getCarryName());

        // Inserting Row
        db.insert(TABLE_OFFLINE_STORE, null, values);
        db.close();
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, category.getId());
        values.put(KEY_CATEGORY_NAME, category.getName());

        // Inserting Row
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    public void addInstruction(Instruction instruction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, instruction.getId());
        values.put(KEY_INSTRUCTION_NAME, instruction.getName());

        // Inserting Row
        db.insert(TABLE_INSTRUCTION, null, values);
        db.close();
    }

    public void addNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, notification.getMessage());
        values.put(KEY_DATE, notification.getDate());
        values.put(KEY_TIME, notification.getTime());

        // Inserting Row
        db.insert(TABLE_NEWS_NOTIFICATION, null, values);
        db.close();
    }

    public void addOrder(Order order,int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, order.getOrderId());
        values.put(KEY_PRICE, order.getOrderAmount());
        values.put(KEY_PER_CARTON, order.getOrderPerCartonDiscount());
        values.put(KEY_DISCOUNT, order.getOrderDiscount());
        values.put(KEY_USER_ID, userId);
        values.put(KEY_STORE_NAME, order.getStoreName());
        values.put(KEY_ORDER_DATE, order.getOrderDate());
        values.put(KEY_DELIVERY_DATE, order.getDeliveryDate());

        // Inserting Row
        db.insert(TABLE_ORDER, null, values);
        db.close();
    }

    public long addOfflineOrder(Order order,int userId, String instructionID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, order.getOrderId());
        values.put(KEY_PRICE, order.getOrderAmount());
        values.put(KEY_PER_CARTON, order.getOrderPerCartonDiscount());
        values.put(KEY_DISCOUNT, order.getOrderDiscount());
        values.put(KEY_USER_ID, userId);
        values.put(KEY_STORE_NAME, order.getStoreName());
        values.put(KEY_ORDER_DATE, order.getOrderDate());
        values.put(KEY_DELIVERY_DATE, order.getDeliveryDate());
        values.put(KEY_INSTRUCTION_ID, instructionID);

        // Inserting Row
        long id = db.insert(TABLE_OFFLINE_ORDER, null, values);
        db.close();
        return id;
    }

    public void addOrderProduct(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, item.getOrderId());
        values.put(KEY_PRODUCT_NAME, item.getItemName());
        values.put(KEY_PRICE, item.getPrice());
        values.put(KEY_QTY, item.getQuantity());
        values.put(KEY_DISCOUNT_PER_PRODUCT, item.getDiscount_per_product());
        values.put(KEY_FLAVOUR_NAME, item.getFlavour());

        // Inserting Row
        db.insert(TABLE_ORDER_PRODUCT, null, values);
        db.close();
    }

    public void addOfflineOrderProduct(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, item.getOrderId());
        values.put(KEY_PRODUCT_ID, item.getItemId());
        values.put(KEY_PRODUCT_NAME, item.getItemName());
        values.put(KEY_PRICE, item.getPrice());
        values.put(KEY_QTY, item.getQuantity());
        Log.i("Insert Discount Product",item.getDiscount_per_product()+"--");
        values.put(KEY_DISCOUNT_PER_PRODUCT, item.getDiscount_per_product());
        values.put(KEY_FLAVOUR_ID, item.getFlavourId());
        values.put(KEY_FLAVOUR_NAME, item.getFlavour());

        // Inserting Row
        db.insert(TABLE_OFFLINE_ORDER_PRODUCT, null, values);
        db.close();
    }

    public Product_Flavour getFlavour(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCT_FLAVOUR, new String[] { KEY_PRODUCT_ID,
                        KEY_FLAVOUR_ID, KEY_FLAVOUR_NAME}, KEY_FLAVOUR_NAME + "=?",
                new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Product_Flavour product_flavour = new Product_Flavour();
        product_flavour.setProductId(Integer.parseInt(cursor.getString(0)));
        product_flavour.setFlavourId(Integer.parseInt(cursor.getString(1)));
        product_flavour.setFlavourName(cursor.getString(2));

        return product_flavour;
    }



    public List<Item> getOrderItems(String orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> itemList = new ArrayList<Item>();
        Cursor cursor = db.query(TABLE_ORDER_PRODUCT, new String[] { KEY_ORDER_ID,
                        KEY_PRODUCT_NAME, KEY_PRICE, KEY_QTY, KEY_DISCOUNT_PER_PRODUCT, KEY_FLAVOUR_NAME}, KEY_ORDER_ID + "=?",
                new String[] { orderId }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemName(cursor.getString(1)+"\n"+cursor.getString(5));
                item.setPrice(Double.parseDouble(cursor.getString(2)));
                item.setQuantity(Integer.parseInt(cursor.getString(3)));
                item.setDiscount_per_product(Double.parseDouble(cursor.getString(4)));
                Log.i("Product Name", item.getItemName());
                Log.i("Product Price", item.getPrice()+"");
                Log.i("Product Qty",item.getQuantity()+"");
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    public List<Item> getOfflineOrderItems(String orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> itemList = new ArrayList<Item>();
        Cursor cursor = db.query(TABLE_OFFLINE_ORDER_PRODUCT, new String[] { KEY_ORDER_ID,
                        KEY_PRODUCT_NAME, KEY_PRICE, KEY_QTY, KEY_DISCOUNT_PER_PRODUCT, KEY_FLAVOUR_NAME}, KEY_ORDER_ID + "=?",
                new String[] { orderId }, null, null, null, null);
        Log.i("Cursor Count",cursor.getCount()+"");
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemName(cursor.getString(1)+"\n"+cursor.getString(5));
                item.setPrice(Double.parseDouble(cursor.getString(2)));
                item.setQuantity(Integer.parseInt(cursor.getString(3)));
                item.setDiscount_per_product(Double.parseDouble(cursor.getString(4)));
                Log.i("Product Name", item.getItemName());
                Log.i("Product Price", item.getPrice()+"");
                Log.i("Product Qty",item.getQuantity()+"");
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }


    public List<Item> getOfflineOrderItemList(String orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> itemList = new ArrayList<Item>();
        Cursor cursor = db.query(TABLE_OFFLINE_ORDER_PRODUCT, new String[] { KEY_ORDER_ID,
                        KEY_PRODUCT_ID, KEY_PRICE, KEY_QTY, KEY_DISCOUNT_PER_PRODUCT, KEY_FLAVOUR_ID}, KEY_ORDER_ID + "=?",
                new String[] { orderId }, null, null, null, null);
        Log.i("Cursor Count",cursor.getCount()+"");
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setItemId(Integer.parseInt(cursor.getString(1)));
                item.setPrice(Double.parseDouble(cursor.getString(2)));
                item.setQuantity(Integer.parseInt(cursor.getString(3)));
                item.setDiscount_per_product(Double.parseDouble(cursor.getString(4)));
                item.setFlavourId(Integer.parseInt(cursor.getString(5)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    public Store getStore(String storeName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STORE, new String[] { KEY_ID,
                        KEY_STORE_NAME, KEY_ADDRESS,KEY_CONTACT,KEY_CONTACT_PERSON,KEY_ZONE_ID,KEY_CATEGORY_ID,KEY_CARRY_NAME}, KEY_STORE_NAME + "=?",
                new String[] { storeName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Store store = new Store(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4),Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)));

        return store;
    }


    public List<Category> getAllCategory() {
        List<Category> categoryList = new ArrayList<Category>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));

                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        return categoryList;
    }

    public List<Zone> getAllZone() {
        List<Zone> zoneList = new ArrayList<Zone>();

        String selectQuery = "SELECT  * FROM " + TABLE_ZONE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Zone zone = new Zone();
                zone.setId(Integer.parseInt(cursor.getString(0)));
                zone.setZoneName(cursor.getString(1));
                zone.setUserId(Integer.parseInt(cursor.getString(2)));
                zoneList.add(zone);
            } while (cursor.moveToNext());
        }

        return zoneList;
    }

    public List<Instruction> getAllInstructions() {
        List<Instruction> instructionList = new ArrayList<Instruction>();

        String selectQuery = "SELECT  * FROM " + TABLE_INSTRUCTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Instruction instruction = new Instruction();
                instruction.setId(Integer.parseInt(cursor.getString(0)));
                instruction.setName(cursor.getString(1));

                instructionList.add(instruction);
            } while (cursor.moveToNext());
        }

        return instructionList;
    }

    public List<Store> getAllStore() {
        List<Store> storeList = new ArrayList<Store>();

        String selectQuery = "SELECT  * FROM " + TABLE_STORE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setStoreId(Integer.parseInt(cursor.getString(0)));
                //Log.i("Store ID", store.getStoreId() + "");
                store.setStoreName(cursor.getString(1));
                //Log.i("Store Name", store.getStoreName() + "");
                store.setStoreAddress(cursor.getString(2));
                //Log.i("Store Address", store.getStoreAddress());
                store.setContact(cursor.getString(3));
                //Log.i("Store Contact", store.getContact() + "");
                store.setContactPerson(cursor.getString(4));
                //Log.i("Store Contact Person", store.getContactPerson() + "");
                store.setZoneId(Integer.parseInt(cursor.getString(5)));
                //Log.i("Store Zone ID", store.getZoneId() + "");
                store.setCategoryId(Integer.parseInt(cursor.getString(6)));
                //Log.i("Store Category ID", cursor.getString(6) + "");
                store.setCarryName((cursor.getString(7)));
                //Log.i("Store Carry Name", store.getCarryName() + "");

                storeList.add(store);
            } while (cursor.moveToNext());
        }

        return storeList;
    }

    public List<Store> getLastStore() {
        List<Store> storeList = new ArrayList<Store>();

        String selectQuery = "SELECT  * FROM " + TABLE_STORE +" ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setStoreId(Integer.parseInt(cursor.getString(0)));
                //Log.i("Store ID", store.getStoreId() + "");
                store.setStoreName(cursor.getString(1));
                //Log.i("Store Name", store.getStoreName() + "");
                store.setStoreAddress(cursor.getString(2));
                //Log.i("Store Address", store.getStoreAddress());
                store.setContact(cursor.getString(3));
                //Log.i("Store Contact", store.getContact() + "");
                store.setContactPerson(cursor.getString(4));
                //Log.i("Store Contact Person", store.getContactPerson() + "");
                store.setZoneId(Integer.parseInt(cursor.getString(5)));
                //Log.i("Store Zone ID", store.getZoneId() + "");
                store.setCategoryId(Integer.parseInt(cursor.getString(6)));
                //Log.i("Store Category ID", cursor.getString(6) + "");
                store.setCarryName((cursor.getString(7)));
                //Log.i("Store Carry Name", store.getCarryName() + "");

                storeList.add(store);
            } while (cursor.moveToNext());
        }
        return storeList;
    }

    public List<Store> getAllOfflineStore() {
        List<Store> storeList = new ArrayList<Store>();

        String selectQuery = "SELECT  * FROM " + TABLE_OFFLINE_STORE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setStoreId(Integer.parseInt(cursor.getString(0)));
                Log.i("Store ID", store.getStoreId() + "");
                store.setStoreName(cursor.getString(1));
                Log.i("Store Name", store.getStoreName() + "");
                store.setStoreAddress(cursor.getString(2));
                Log.i("Store Address", store.getStoreAddress());
                store.setContact(cursor.getString(3));
                Log.i("Store Contact", store.getContact() + "");
                store.setContactPerson(cursor.getString(4));
                Log.i("Store Contact Person", store.getContactPerson() + "");
                store.setZoneId(Integer.parseInt(cursor.getString(5)));
                Log.i("Store Zone ID", store.getZoneId() + "");
                store.setCategoryId(Integer.parseInt(cursor.getString(6)));
                Log.i("Store Category ID", cursor.getString(6) + "");
                store.setCarryName((cursor.getString(7)));
                Log.i("Store Carry Name", store.getCarryName() + "");

                storeList.add(store);
            } while (cursor.moveToNext());
        }

        return storeList;
    }

    public List<Product> getAllProduct() {
        List<Product> productList = new ArrayList<Product>();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setProductId(Integer.parseInt(cursor.getString(0)));
                product.setProductName(cursor.getString(1));
                product.setPrice(Double.parseDouble(cursor.getString(2)));

                productList.add(product);
            } while (cursor.moveToNext());
        }

        return productList;
    }

    public List<Product_Flavour> getAllFlavours() {
        List<Product_Flavour> flavourList = new ArrayList<Product_Flavour>();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_FLAVOUR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Product_Flavour product_flavour = new Product_Flavour();
                product_flavour.setProductId(Integer.parseInt(cursor.getString(1)));
                product_flavour.setFlavourId(Integer.parseInt(cursor.getString(2)));
                product_flavour.setFlavourName(cursor.getString(3));
                product_flavour.setImagePath(cursor.getString(4));
                product_flavour.setImagePath(cursor.getString(4));
                product_flavour.setPrice(Double.parseDouble(cursor.getString(5)));

                flavourList.add(product_flavour);
            } while (cursor.moveToNext());
        }

        return flavourList;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<Order>();

        String selectQuery = "SELECT  * FROM " + TABLE_ORDER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(cursor.getString(0));
                order.setOrderAmount(Double.parseDouble(cursor.getString(1)));
                order.setStoreName(cursor.getString(3));
                order.setOrderDate(cursor.getString(4));
                order.setOrderPerCartonDiscount(Double.parseDouble(cursor.getString(5)));
                order.setOrderDiscount(Double.parseDouble(cursor.getString(6)));
                order.setDeliveryDate(cursor.getString(7));
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        return orderList;
    }

    public List<Order> getAllOfflineOrders() {
        List<Order> orderList = new ArrayList<Order>();

        String selectQuery = "SELECT  * FROM " + TABLE_OFFLINE_ORDER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(cursor.getString(0));
                order.setOrderAmount(Double.parseDouble(cursor.getString(1)));
                order.setStoreName(cursor.getString(3));
                order.setOrderDate(cursor.getString(4));
                order.setOrderPerCartonDiscount(Double.parseDouble(cursor.getString(5)));
                order.setOrderDiscount(Double.parseDouble(cursor.getString(6)));
                order.setDeliveryDate(cursor.getString(7));
                order.setInstructionID(cursor.getString(8));
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        return orderList;
    }

    public List<Notification> getAllNotification() {
        List<Notification> notificationList = new ArrayList<Notification>();

        String selectQuery = "SELECT  * FROM " + TABLE_NEWS_NOTIFICATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification();
                notification.setMessage(cursor.getString(1));
                notification.setDate(cursor.getString(2));
                notification.setTime(cursor.getString(3));

                notificationList.add(notification);
            } while (cursor.moveToNext());
        }

        return notificationList;
    }



    public User getLoginedUser() {
        User user;
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User us = new User();
                us.setId(Integer.parseInt(cursor.getString(0)));
                us.setName(cursor.getString(1));
                us.setAddress(cursor.getString(2));
                us.setContact(cursor.getString(3));
                us.setCnic(cursor.getString(4));
                us.setUser_role(Integer.parseInt(cursor.getString(5)));
                us.setUser_name(cursor.getString(6));
                us.setPassword(cursor.getString(7));
                us.setLatitude(Double.parseDouble(cursor.getString(8)));
                us.setLongitude(Double.parseDouble(cursor.getString(9)));
                us.setGcm_id(cursor.getString(10));
                us.setIs_login(Integer.parseInt(cursor.getString(11)));
                user = us;
            } while (cursor.moveToNext());
        }
        else {
            user = new User();
        }

        // return contact list
        return user;
    }

    public Target getUserTarget() {
        Target target;
        String selectQuery = "SELECT  * FROM " + TABLE_USER_TARGET;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Target t = new Target();
                t.setUserId(Integer.parseInt(cursor.getString(0)));
                t.setTotalTarget(Integer.parseInt(cursor.getString(1)));
                t.setTotalAchieved(Integer.parseInt(cursor.getString(2)));
                target = t;
            } while (cursor.moveToNext());
        }
        else {
            target = new Target();
        }

        return target;
    }

    // Getting contacts Count
    public int getUserCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    // Getting contacts Count
    public int getUserTargetCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_USER_TARGET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    // Getting contacts Count
    public int getStoreCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_STORE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    // Getting contacts Count
    public int getNotificationCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_NEWS_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateUserPassword(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_CONTACT, user.getContact());
        values.put(KEY_CNIC, user.getCnic());
        values.put(KEY_USER_NAME, user.getUser_name());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_LATITUDE, user.getLatitude());
        values.put(KEY_LONGITUDE, user.getLongitude());
        values.put(KEY_GCM_ID, user.getGcm_id());

        Log.i("Update User", "updateUserPassword");
        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });

    }

    public void deleteAllRecords(String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tableName);
        db.close();
    }
}
