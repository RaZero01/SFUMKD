package sample.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    public static final User user = new User();

    private String login;
    private String name;
    private String surname;
    private String patronymic;
    private String degree;

}
