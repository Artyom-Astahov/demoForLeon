package astahov.artem.tesk.task.service;

import astahov.artem.tesk.task.persistence.entity.TimeEntryEntity;
import astahov.artem.tesk.task.persistence.repository.TimeEntryRepository;
import astahov.artem.tesk.task.service.dto.TimeEntry;
import astahov.artem.tesk.task.service.mappers.TimeEntryMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class TimeServiceIntegrationTest {

    @Container
    static PostgreSQLContainer database = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @AfterEach
    public void tearDown() {
        database.close();
    }

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.datasource.hikari.minimumIdle", () -> 1);
        registry.add("spring.datasource.hikari.maximumPoolSize", () -> 5);
        registry.add("spring.datasource.hikari.connectionTimeout", () -> 5000);
        registry.add("spring.datasource.hikari.maxLifetime", () -> 2000000);
    }

    @Autowired
    private TimeService timeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Spy
    private TimeEntryMapperImpl timeEntryMapper;

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Test
    public void testDatabaseDisconnectionScenario() throws Exception {
        String str = "2024-12-02 17:35:27.665051";
        String str2 = "2024-12-02 17:35:28.665051";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        TimeEntry timeEntry1 = new TimeEntry(dateTime);
        TimeEntry timeEntry2 = new TimeEntry(dateTime2);
        database.start();
        TimeService.queue.add(timeEntry1);
        TimeService.queue.add(timeEntry2);

        try {
            timeService.saveFromQueue();
        } catch (Exception e) {
            // do nothing, we expect an error
        }

        database.start();
        TestPropertyValues values = TestPropertyValues.of(
                "spring.datasource.url=" + database.getJdbcUrl()
        );
        values.applyTo(configurableApplicationContext);
        assertTrue(TimeService.queue.isEmpty());
        List<TimeEntryEntity> savedEntities = timeEntryRepository.findAll();
        assertEquals(2, savedEntities.size());
        assertEquals(timeEntry1, timeEntryMapper.toDto(savedEntities.get(0)));
        assertEquals(timeEntry2, timeEntryMapper.toDto(savedEntities.get(1)));
    }
}

