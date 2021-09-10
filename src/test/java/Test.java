import java.util.UUID;

public class Test {
    public String id;

    public Test(){
        this.id = UUID.randomUUID().toString();
    }


    public static void main(String[] args){
        System.out.println(new Test().id);
        System.out.println(new Test().id);
    }
}
