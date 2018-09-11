package jp.co.worksap.stm2018.jobhere.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointedTimeDTO {
    private String operationId;
    private String cooperatorId;
    private List<Timestamp> startTimes;
}

