package jp.co.worksap.stm2018.jobhere.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xu_xi-pc on 2018/9/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {
    private String name;
    private String content;
}
