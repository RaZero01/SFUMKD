package sample.client.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {

    private String login;

    private String password;

    private String name;

    private String surname;

    private String patronymic;
    
    private String degree;

}
