package jp.co.worksap.stm2018.jobhere.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDTO {
    private String invitationId;
    private String userId;
    private String jobId;
    private Timestamp inviteTime;
}
