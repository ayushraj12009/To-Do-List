package com.apiwiz.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String fristName;

    private String lastName;

    private String userName;

    private String email;

    private String password;


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> taskletDetails = new ArrayList<>();


    public User(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public User(String firstName, String last, String userName, String eamil, String password) {
        this.fristName = firstName;
        this.lastName = last;
        this.userName = userName;
        this.email = eamil;
        this.password = password;
    }


}
