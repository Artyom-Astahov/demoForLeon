package astahov.artem.tesk.task.common;

import astahov.artem.tesk.task.service.TimeService;
import astahov.artem.tesk.task.service.dto.TimeEntry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class MyThread implements Runnable {

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            TimeEntry entity = new TimeEntry(LocalDateTime.now());
            TimeService.queue.add(entity);
            log.info("Добавил в очередь {}, {}", entity, Thread.currentThread().getName());
            long delayMillis = 1000 - (System.currentTimeMillis() % 1000);
            Thread.sleep(delayMillis);
        }

    }
}
