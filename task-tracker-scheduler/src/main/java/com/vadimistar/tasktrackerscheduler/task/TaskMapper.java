package com.vadimistar.tasktrackerscheduler.task;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskDto mapTaskToTaskDto(Task task);
}
