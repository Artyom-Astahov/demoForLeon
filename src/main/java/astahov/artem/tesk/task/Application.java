package astahov.artem.tesk.task;

import astahov.artem.tesk.task.common.MyThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    static MyThread myThread;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        myThread = new MyThread();
        Thread thread = new Thread(myThread);
        thread.start();
    }

}