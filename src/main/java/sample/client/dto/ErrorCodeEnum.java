package sample.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {
    NOT_FOUND(404, "service.not_found"),
    BAD_REQUEST(400, "service.bad_request"),
    UNEXPECTED_ERROR(500,"service.unexpected");

    private int code;
    private String codeMessage;

}
