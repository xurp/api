package jp.co.worksap.stm2018.jobhere.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xu_xi-pc on 2018/9/18.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {
    private String name;
    private int value;
}
