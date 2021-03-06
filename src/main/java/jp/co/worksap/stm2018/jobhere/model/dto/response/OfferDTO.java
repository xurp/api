package jp.co.worksap.stm2018.jobhere.model.dto.response;

import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {
    private String id;
    private String result;
    private String sendStatus;
    private String remark;
    private ApplicationDTO applicationDTO;
    private String companyId;
    private Timestamp offerTime;
    private Timestamp respondTime;
}
