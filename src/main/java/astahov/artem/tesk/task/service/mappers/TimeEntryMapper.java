package astahov.artem.tesk.task.service.mappers;

import astahov.artem.tesk.task.persistence.entity.TimeEntryEntity;
import astahov.artem.tesk.task.service.dto.TimeEntry;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface TimeEntryMapper {


    TimeEntry toDto(TimeEntryEntity timeEntryEntity);

    TimeEntryEntity toEntity(TimeEntry timeEntry);

    List<TimeEntry> toDtoList(List<TimeEntryEntity> timeEntryEntity);
}
