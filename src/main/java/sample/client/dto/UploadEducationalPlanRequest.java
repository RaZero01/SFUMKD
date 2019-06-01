package sample.client.dto;

import lombok.Data;
import sample.dto.Discipline;

import java.util.Set;

@Data
public class UploadEducationalPlanRequest {

    private String customName;
    private String trDirection;
    private String profile;
    private String form;
    private String plan;
    private String department;
    private Set<Discipline> disciplines;
}
