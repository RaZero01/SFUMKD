package sample.client.dto;

import lombok.Data;
import sample.dto.User;

@Data
public class AuthUserResponse {

    private StatusEnum status;
    private User user;
}
