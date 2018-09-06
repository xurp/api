package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.OfferRepository;
import jp.co.worksap.stm2018.jobhere.dao.ResumeRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Offer;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.OfferDTO;
import jp.co.worksap.stm2018.jobhere.service.OfferService;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;


    @Override
    public List<OfferDTO> list(String companyId) {
        List<Offer> offerList = offerRepository.findByCompanyId(companyId);
        List<OfferDTO> offerDTOList = new ArrayList<>();
        for (Offer offer : offerList) {
            offerDTOList.add(OfferDTO.builder()
                    .id(offer.getId())
                    .result(offer.getResult())
                    .sendStatus(offer.getSendStatus())
                    .remark(offer.getRemark())
                    .applicationId(offer.getApplicationId())
                    .companyId(offer.getCompanyId())
                    .offerTime(offer.getOfferTime())
                    .respondTime(offer.getRespondTime()).build());
        }
        return offerDTOList;
    }
}
