package sample.client.dto;

import lombok.Data;

@Data
public class AuthUserRequest {

    private String login;
    private String password;
}
