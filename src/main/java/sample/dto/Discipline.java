package sample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discipline {
    private String code;
    private String name;
    private Set<Competition> competitions;
    private Set<ControlForm> controlForms;
    private String exam_units;
    private String lec_hours;
    private String lab_hours;
    private String prc_hours;
    private String self_hours;
}
