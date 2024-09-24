package com.vadimistar.tasktrackerscheduler.repository;

import com.vadimistar.tasktrackerscheduler.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
