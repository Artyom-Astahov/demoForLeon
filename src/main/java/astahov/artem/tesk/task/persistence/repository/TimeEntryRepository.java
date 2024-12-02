package astahov.artem.tesk.task.persistence.repository;

import astahov.artem.tesk.task.persistence.entity.TimeEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntryEntity, Long> {
}
