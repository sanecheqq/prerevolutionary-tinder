package com.liga.semin.server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "mate_gender")
    @Enumerated(EnumType.STRING)
    private GenderType mateGender;

    @ManyToMany
    @JoinTable(
            name = "users_likes",
            joinColumns = @JoinColumn(name = "from_user"),
            inverseJoinColumns = @JoinColumn(name = "to_user"))
    private Set<User> favorites = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "users_likes",
            joinColumns = @JoinColumn(name = "to_user"),
            inverseJoinColumns = @JoinColumn(name = "from_user"))
    private Set<User> followers = new HashSet<>();

    @Column(name = "search_offset")
    private int searchOffset;
}
