package com.apiwiz.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor



public class TaskDto {

    private String title;
    private String description;
    private String dueDate;
    private String status;
    private String email;
    private String password;

    public TaskDto(String title, String description, String dueDate, String status, String email, String password){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.email = email;
        this.password = password;
    }

}
