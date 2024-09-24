package com.vadimistar.tasktrackerscheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id
    private Long id;

    private String email;
    private String password;

    @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Task> tasks;
}
