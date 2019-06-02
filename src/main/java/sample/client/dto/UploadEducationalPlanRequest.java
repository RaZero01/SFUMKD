package sample.client.dto;

import lombok.Data;
import sample.dto.Discipline;
import sample.dto.EducationalPlan;

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

    public UploadEducationalPlanRequest(EducationalPlan eplan){
        trDirection = eplan.getTrDirection();
        profile = eplan.getProfile();
        form = eplan.getForm();
        plan = eplan.getPlan();
        department = eplan.getDepartment();
        disciplines = eplan.getDisciplines();
    }
}
