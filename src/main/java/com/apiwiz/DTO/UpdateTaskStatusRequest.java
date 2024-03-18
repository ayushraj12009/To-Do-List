package com.apiwiz.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateTaskStatusRequest {
    private String email;
    private String password;
    private Long taskId;
    private String newStatus;

  private String taskUpdate;

    public UpdateTaskStatusRequest() {
    }

    public UpdateTaskStatusRequest(String email, String password, Long taskId, String newStatus, String taskUpdate) {
        this.email = email;
        this.password = password;
        this.taskId = taskId;
        this.newStatus = newStatus;
      this.taskUpdate = taskUpdate;
    }


}
