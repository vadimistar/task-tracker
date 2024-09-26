package com.vadimistar.tasktrackerbackend.task;

import com.vadimistar.tasktrackerbackend.task.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.task.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.task.dto.UpdateTaskDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskDto mapTaskToTaskDto(Task task);
    Task mapCreateTaskDtoToTask(CreateTaskDto createTaskDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateTaskDtoToTask(UpdateTaskDto updateTaskDto, @MappingTarget Task task);
}
