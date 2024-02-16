package com.project.entity.concretes.business;

import com.project.entity.concretes.user.User;

import javax.persistence.*;
import java.util.List;

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departmentName;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<User> doctors;


}
