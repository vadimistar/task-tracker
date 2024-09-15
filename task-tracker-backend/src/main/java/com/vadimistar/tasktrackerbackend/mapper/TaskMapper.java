package com.vadimistar.tasktrackerbackend.mapper;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerbackend.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskDto mapTaskToTaskDto(Task task);
    Task mapTaskDtoToTask(TaskDto taskDto);
    Task mapCreateTaskDtoToTask(CreateTaskDto createTaskDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateTaskDtoToTask(UpdateTaskDto updateTaskDto, @MappingTarget Task task);
}
