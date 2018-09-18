package jp.co.worksap.stm2018.jobhere.model.dto.response;

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
public class AppointedTimeAndApplicationDTO {
    private String id;
    private String operationId;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp startTime;
    private Timestamp endTime;
    private String periods;
    private String cooperatorId;
    private ApplicationDTO applicationDTO;
}
