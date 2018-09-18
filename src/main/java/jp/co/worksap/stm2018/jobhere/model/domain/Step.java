package jp.co.worksap.stm2018.jobhere.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by xu_xi-pc on 2018/9/4.
 */
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "step")
public class Step {
    @Id
    private String id;

    @Column(nullable = false)
    private String jobId;

    @Column(nullable = false)
    private double index;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;
    @OneToMany(mappedBy = "step",cascade= CascadeType.MERGE,fetch= FetchType.LAZY)
    private List<Item> items;
    public void addItem(Item item) {
        if(this.items==null)
            this.items=new ArrayList<>();
        this.items.add(item);
    }
    public void removeItem(String userId) {
        for (int index=0; index < this.items.size(); index ++ ) {
            if (items.get(index).getId() .equals(userId)) {
                this.items.remove(index);
                break;
            }
        }
    }

    public Step(){
        this.id=UUID.randomUUID().toString().replace("-", "");
    }
}
