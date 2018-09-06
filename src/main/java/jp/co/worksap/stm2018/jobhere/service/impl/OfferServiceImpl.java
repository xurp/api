package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.ApplicationRepository;
import jp.co.worksap.stm2018.jobhere.dao.OfferRepository;
import jp.co.worksap.stm2018.jobhere.dao.ResumeRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.Offer;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.OfferDTO;
import jp.co.worksap.stm2018.jobhere.service.OfferService;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import jp.co.worksap.stm2018.jobhere.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private ApplicationRepository applicationRepository;


    @Override
    public List<OfferDTO> list(String companyId) {

        List<Offer> offerList = offerRepository.findByCompanyId(companyId);
        List<OfferDTO> offerDTOList = new ArrayList<>();
        for (Offer offer : offerList) {
            Optional<Application> applicationOptional = applicationRepository.findById(offer.getApplicationId());
            if (applicationOptional.isPresent()) {
                Application application = applicationOptional.get();
                ApplicationDTO applicationDTO = ApplicationDTO.builder()
                        .id(application.getId())
                        .resume(application.getResume())
                        .job(application.getJob())
                        .step(application.getStep())
                        .user(application.getUser())
                        .createTime(application.getCreateTime())
                        .updateTime(application.getUpdateTime()).build();

                offerDTOList.add(OfferDTO.builder()
                        .id(offer.getId())
                        .result(offer.getResult())
                        .sendStatus(offer.getSendStatus())
                        .remark(offer.getRemark())
                        .applicationDTO(applicationDTO)
                        .companyId(offer.getCompanyId())
                        .offerTime(offer.getOfferTime())
                        .respondTime(offer.getRespondTime()).build());
            }
        }
        return offerDTOList;
    }
    @Transactional
    @Override
    public void update(EmailDTO emailDTO) {
        String offerId=emailDTO.getOfferId();
        Optional<Offer> offerOptional=offerRepository.findById(offerId);
        if(offerOptional.isPresent()) {
            Offer offer=offerOptional.get();
            offer.setSendStatus("1");
            offerRepository.save(offer);
            Mail.send("chorespore@163.com", emailDTO.getReceiver(), emailDTO.getSubject(), emailDTO.getContent());
        }
        else{
            throw new ValidationException("Wrong offer ID.");
        }
    }
}
