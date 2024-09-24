package com.vadimistar.tasktrackerscheduler.mapper;

import com.vadimistar.tasktrackerscheduler.dto.TaskDto;
import com.vadimistar.tasktrackerscheduler.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskDto mapTaskToTaskDto(Task task);
}
