package astahov.artem.tesk.task.service;

import astahov.artem.tesk.task.persistence.entity.TimeEntryEntity;
import astahov.artem.tesk.task.persistence.repository.TimeEntryRepository;
import astahov.artem.tesk.task.service.dto.TimeEntry;
import astahov.artem.tesk.task.service.mappers.TimeEntryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTest {

    @InjectMocks
    private TimeService timeService;

    @Mock
    private TimeEntryRepository timeEntryRepository;

    @Mock
    private TimeEntryMapper timeEntryMapper;


    @Test
    public void getAllTest() {
        TimeEntryEntity entity = new TimeEntryEntity();
        TimeEntry dto = new TimeEntry(LocalDateTime.now());
        when(timeEntryRepository.findAll()).thenReturn(List.of(entity));
        when(timeEntryMapper.toDtoList(List.of(entity))).thenReturn(List.of(dto));

        List<TimeEntry> result = timeService.getAll();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}
