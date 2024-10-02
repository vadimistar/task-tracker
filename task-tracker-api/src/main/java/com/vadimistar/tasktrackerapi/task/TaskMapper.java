package com.vadimistar.tasktrackerapi.task;

import com.vadimistar.tasktrackerapi.task.dto.CreateTaskDto;
import com.vadimistar.tasktrackerapi.task.dto.TaskDto;
import com.vadimistar.tasktrackerapi.task.dto.UpdateTaskDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskDto mapTaskToTaskDto(Task task);
    Task mapCreateTaskDtoToTask(CreateTaskDto createTaskDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateTaskDtoToTask(UpdateTaskDto updateTaskDto, @MappingTarget Task task);
}
