package jp.co.worksap.stm2018.jobhere.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/5.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    private String applicationId;
    private String cooperatorId;
    private String offerId;
    private String subject;
    private String content;
    private String assessId;
    private String receiver;
    private String operationId;
    private String link;

    private String startDate;
    private String endDate;
    private String periods;
    private List<String> cooperatorIds;
    private List<String> applications;

    private String result;
    private String remark;
}
