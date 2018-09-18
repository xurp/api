package jp.co.worksap.stm2018.jobhere.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Created by xu_xi-pc on 2018/9/11.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChooseDateDTO {
    private Timestamp startDate;
    private Timestamp endDate;
    private String periods;
    private int number;//1(batch) or 3(single)
    private String operationId;
    private String cooperatorId;
    private Timestamp startTime;
}
