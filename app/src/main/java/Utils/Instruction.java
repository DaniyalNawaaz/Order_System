package Utils;

/**
 * Created by user on 4/2/2016.
 */
public class Instruction {
    private int id;
    private String name;

    public Instruction(){}

    public Instruction(int id, String name){
        this.id = id;
        this.name = name;
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
}
