package sample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationalPlan {

    private String customName;
    private String trDirection;
    private String profile;
    private String form;
    private String plan;
    private String department;
    private Set<Discipline> disciplines;
}
