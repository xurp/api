package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.Offer;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.OfferDTO;

import java.util.List;

public interface OfferService {

    //ResumeDTO find(String id);
    List<OfferDTO> list(String companyId);

    void update(EmailDTO emailDTO);

    void offer(String offerId, String result);
}
