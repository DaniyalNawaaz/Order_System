package Utils;

/**
 * Created by user on 3/18/2016.
 */
public class Notification {
    private String message;
    private String Date;
    private String Time;

    public Notification(){}

    public Notification(String message, String Date, String Time){
        this.message = message;
        this.Date = Date;
        this.Time = Time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
