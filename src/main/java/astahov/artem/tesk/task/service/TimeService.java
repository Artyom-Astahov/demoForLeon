package astahov.artem.tesk.task.service;

import astahov.artem.tesk.task.persistence.entity.TimeEntryEntity;
import astahov.artem.tesk.task.persistence.repository.TimeEntryRepository;
import astahov.artem.tesk.task.service.dto.TimeEntry;
import astahov.artem.tesk.task.service.mappers.TimeEntryMapper;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
@Transactional
public class TimeService {

    private final TimeEntryRepository repository;
    private final TimeEntryMapper mapper;
    public static final BlockingQueue<TimeEntry> queue = new LinkedBlockingQueue<>();

    public TimeService(TimeEntryRepository repository, TimeEntryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<TimeEntry> getAll() {
        List<TimeEntryEntity> entities = repository.findAll();
        return mapper.toDtoList(entities);
    }


    @Scheduled(fixedRate = 1000)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    @SneakyThrows
    protected void saveFromQueue() {
        if (!queue.isEmpty()) {
            queue.stream().map(mapper::toEntity).forEach(repository::save);
            queue.clear();
            log.info("Сохранил очередь, поток {}", Thread.currentThread().getName());
        }
    }
}