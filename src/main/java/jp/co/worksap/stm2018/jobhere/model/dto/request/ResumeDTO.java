package jp.co.worksap.stm2018.jobhere.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDTO {

    private String id;

    private String name;

    private String userId;

    private String gender;

    private int age;

    private String email;

    private String phone;

    private String degree;

    private String school;

    private String major;

    private String intro;

    private boolean open;
}
