package jp.co.worksap.stm2018.jobhere.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CooperatorDTO {
    private String id;
    private String name;
    private String companyId;
    private String department;
    private String email;
    private String phone;
}
