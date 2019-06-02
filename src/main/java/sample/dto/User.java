package sample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String login;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private String degree;

}
