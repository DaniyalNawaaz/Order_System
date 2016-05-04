package Utils;

import java.util.Comparator;

/**
 * Created by user on 3/18/2016.
 */
public class Order {
    private String orderId;
    private String storeName;
    private String orderDate;
    private String deliveryDate;
    private double orderAmount;
    private double orderDiscount;
    private double orderPerCartonDiscount;
    private String instruction;
    private String instructionID;
    private String status;

    public Order(){}

    public Order(String orderId, String storeName, String orderDate, String deliveryDate, double orderAmount){
        this.orderId = orderId;
        this.storeName = storeName;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderAmount = orderAmount;
    }

    public Order(String orderId, String storeName, String orderDate, double orderAmount){
        this.orderId = orderId;
        this.storeName = storeName;
        this.orderDate = orderDate;
        this.orderAmount = orderAmount;
    }

    public static Comparator<Order> StuNameComparator = new Comparator<Order>() {

        public int compare(Order s1, Order s2) {
            String StudentName1 = s1.getOrderDate();
            String StudentName2 = s2.getOrderDate();

            //ascending order
            return StudentName2.compareTo(StudentName1);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

    public static Comparator<Order> OrderIdComparator = new Comparator<Order>() {

        public int compare(Order s1, Order s2) {
            String StudentName1 = s1.getOrderId();
            String StudentName2 = s2.getOrderId();

            //ascending order
            //return StudentName2.compareTo(StudentName1);

            //descending order
            return StudentName2.compareTo(StudentName1);
        }
    };

    public static Comparator<Order> OrderIdComparator1 = new Comparator<Order>() {

        public int compare(Order s1, Order s2) {
            String StudentName1 = s1.getOrderId();
            String StudentName2 = s2.getOrderId();

            //ascending order
            return StudentName2.compareTo(StudentName1);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

    public double getOrderPerCartonDiscount() {
        return orderPerCartonDiscount;
    }

    public void setOrderPerCartonDiscount(double orderPerCartonDiscount) {
        this.orderPerCartonDiscount = orderPerCartonDiscount;
    }

    public double getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(double orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public String getInstructionID() {
        return instructionID;
    }

    public void setInstructionID(String instructionID) {
        this.instructionID = instructionID;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }
}
