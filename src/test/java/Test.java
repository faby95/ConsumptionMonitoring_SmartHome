import java.util.UUID;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Thread;

public class Test {
    public String id;
    public long timeStart;
    public int value = 0;

    public Test(){
        this.id = UUID.randomUUID().toString();
        this.timeStart = System.currentTimeMillis();
    }



    public static void main(String[] args) throws InterruptedException {

        Timer timer = new Timer();
        Test t = new Test();
        System.out.println(t.id);
        System.out.println(t.timeStart);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                t.value += 1;
                System.out.println(t.value);
            }
        }, 200, 200);

        //Thread principale

        for(int i=0;i<10;i++){
            System.out.printf("ciao %d%n", i);
            Thread.sleep(5000);
        }

    }
}
