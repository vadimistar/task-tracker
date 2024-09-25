package com.vadimistar.tasktrackerscheduler.task;

import com.vadimistar.tasktrackerscheduler.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
public class Task {

    @Id
    private Long id;

    private String title;
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private Boolean isCompleted;
    private LocalDateTime completedAt;
}
