package jp.co.worksap.stm2018.jobhere.model.dto.request;

import jp.co.worksap.stm2018.jobhere.model.domain.Item;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by xu_xi-pc on 2018/9/18.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    /*private String id;
    private String name;
    private int score;
    private Step step;*/
    private List<Item> itemList;
    private String stepId;
    private String jobId;
}
