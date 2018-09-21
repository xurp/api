package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.CooperatorDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.UserDTO;

import java.util.List;

public interface ReviewService {
    List<UserDTO> list(User user);

    List<CooperatorDTO> listCooperator(String id);

    void inspect(User inspector,String id, String pass);
}
