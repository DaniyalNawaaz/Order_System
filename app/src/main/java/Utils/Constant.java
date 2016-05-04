package Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/17/2016.
 */
public class Constant {
    public static List<Item> itemList = new ArrayList<>();
    public static List<Item> itemDiscountList = new ArrayList<>();
    public static List<Order> filterList = new ArrayList<>();
    public static String OrderDate = "";
    public static String DeliverDate = "";
    public static String StoreName = "";
    public static String StoreCode = "";
    public static String StoreNumber = "";
    public static String SelectedInstruction = "";
    public static String OrderId = "";
    public static String TOTALPRICE = "";
    public static String GRANDTOTAL = "";
    public static int SelectedInstructionID = -1;
    public static boolean IS_FROM_BOOK_ORDER = false;
    public static boolean IS_FROM_ORDER_STATISTIC = false;

    public static double DiscountPrice = 0.00;

    public static User loginUser = new User();


//    public static String POST_LOGIN = "http://dev.technology-minds.com/metro/web_services/login.php";
//    public static String POST_REGISTER_STORE = "http://dev.technology-minds.com/metro/web_services/register_store.php";
//    public static String POST_MY_ACCOUNT = "http://dev.technology-minds.com/metro/web_services/my_account.php";
//    public static String POST_INSERT_GCM = "http://dev.technology-minds.com/metro/web_services/insert_gcm.php";
//    public static String POST_GET_ALL_CATEGORY = "http://dev.technology-minds.com/metro/web_services/get_all_category.php";
//    public static String POST_GET_ALL_ZONE = "http://dev.technology-minds.com/metro/web_services/get_all_zone.php";
//    public static String POST_GET_ALL_NOTIFICATION = "http://dev.technology-minds.com/metro/web_services/get_all_notification.php";
//    public static String POST_GET_ALL_PRODUCT_WITH_FLAVOUR = "http://dev.technology-minds.com/metro/web_services/get_all_products.php";
//    public static String POST_GET_ALL_STORES = "http://dev.technology-minds.com/metro/web_services/get_all_stores.php";
//    public static String POST_GET_LATEST_STORES = "http://dev.technology-minds.com/metro/web_services/get_latest_stores.php";
//    public static String POST_GET_ALL_INSTRUCTIONS = "http://dev.technology-minds.com/metro/web_services/get_all_instructions.php";
//    public static String POST_PLACE_ORDERS = "http://dev.technology-minds.com/metro/web_services/place_order.php";
//    public static String POST_GET_ALL_PAST_ORDERS = "http://dev.technology-minds.com/metro/web_services/get_all_past_order.php";
//    public static String POST_GET_ALL_PAST_ORDER_DETAILS = "http://dev.technology-minds.com/metro/web_services/get_all_past_order_detail.php";
//    public static String POST_GET_ALL_ORDERS = "http://dev.technology-minds.com/metro/web_services/get_all_orders.php";
//    public static String POST_UPDATE_LOCATION = "http://dev.technology-minds.com/metro/web_services/update_location.php";
//    public static String POST_MY_TARGET = "http://dev.technology-minds.com/metro/web_services/my_target.php?user_id=";

    public static String POST_LOGIN = "http://dev.technology-minds.com/metro_dev/web_services/login.php";
    public static String POST_REGISTER_STORE = "http://dev.technology-minds.com/metro_dev/web_services/register_store.php";
    public static String POST_MY_ACCOUNT = "http://dev.technology-minds.com/metro_dev/web_services/my_account.php";
    public static String POST_INSERT_GCM = "http://dev.technology-minds.com/metro_dev/web_services/insert_gcm.php";
    public static String POST_GET_ALL_CATEGORY = "http://dev.technology-minds.com/metro_dev/web_services/get_all_category.php";
    public static String POST_GET_ALL_ZONE = "http://dev.technology-minds.com/metro_dev/web_services/get_all_zone.php";
    public static String POST_GET_ALL_NOTIFICATION = "http://dev.technology-minds.com/metro_dev/web_services/get_all_notification.php";
    public static String POST_GET_ALL_USER_NOTIFICATION = "http://dev.technology-minds.com/metro_dev/web_services/get_all_user_notification.php";
    public static String POST_GET_ALL_PRODUCT_WITH_FLAVOUR = "http://dev.technology-minds.com/metro_dev/web_services/get_all_products.php";
    public static String POST_GET_ALL_STORES = "http://dev.technology-minds.com/metro_dev/web_services/get_all_stores.php";
    public static String POST_GET_LATEST_STORES = "http://dev.technology-minds.com/metro_dev/web_services/get_latest_stores.php";
    public static String POST_GET_ALL_INSTRUCTIONS = "http://dev.technology-minds.com/metro_dev/web_services/get_all_instructions.php";
    public static String POST_PLACE_ORDERS = "http://dev.technology-minds.com/metro_dev/web_services/place_order.php";
    //public static String POST_GET_ALL_PAST_ORDERS = "http://dev.technology-minds.com/metro_dev/web_services/get_all_past_order.php";
    public static String POST_GET_ALL_PAST_ORDER_DETAILS = "http://dev.technology-minds.com/metro_dev/web_services/get_all_past_order_detail.php";
    public static String POST_GET_ALL_ORDERS = "http://dev.technology-minds.com/metro_dev/web_services/get_all_orders.php";
    public static String POST_UPDATE_LOCATION = "http://dev.technology-minds.com/metro_dev/web_services/update_location.php";
    public static String POST_MY_TARGET = "http://dev.technology-minds.com/metro_dev/web_services/my_target.php?user_id=";

    public static final String GOOGLE_PROJ_ID = "171086232926";
    public static final String MSG_KEY = "m";
    //public static String POST_UPDATE_LOCATION = "update_location.php?";
    //public static String WEB_URL = "https://192.168.0.101/";

}
