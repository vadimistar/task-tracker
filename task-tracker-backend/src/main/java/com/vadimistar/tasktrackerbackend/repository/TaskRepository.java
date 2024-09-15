package com.vadimistar.tasktrackerbackend.repository;

import com.vadimistar.tasktrackerbackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndOwnerId(long id, long ownerId);
    boolean existsByIdAndOwnerId(long id, long ownerId);
}
