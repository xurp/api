package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mail mail;
    @Autowired
    private CompanyRepository companyRepository;

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

    @Override
    public List<OfferDTO> offersOfCandidate(String userId) {
        List<OfferDTO> offerDTOList = new ArrayList<>();

        User user = userRepository.getOne(userId);
        List<Application> applicationList = user.getApplications();

        for (Application application : applicationList) {
            List<Offer> offerList = offerRepository.findByApplicationId(application.getId());
            if (offerList.size() == 1) {
                Offer offer = offerList.get(0);

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

    @Override
    public OfferDTO find(String offerId) {
        Offer offer = offerRepository.getOne(offerId);
        Application application = applicationRepository.getOne(offer.getApplicationId());
        ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .id(application.getId())
                .resume(application.getResume())
                .job(application.getJob())
                .step(application.getStep())
                .user(application.getUser())
                .createTime(application.getCreateTime())
                .updateTime(application.getUpdateTime()).build();

        return OfferDTO.builder()
                .id(offer.getId())
                .result(offer.getResult())
                .sendStatus(offer.getSendStatus())
                .remark(offer.getRemark())
                .applicationDTO(applicationDTO)
                .companyId(offer.getCompanyId())
                .offerTime(offer.getOfferTime())
                .respondTime(offer.getRespondTime()).build();
    }

    @Transactional
    @Override
    public void update(EmailDTO emailDTO) {
        System.out.println(emailDTO.getRemark());
        String offerId = emailDTO.getOfferId();
        Optional<Offer> offerOptional = offerRepository.findById(offerId);
        if (offerOptional.isPresent()) {
            Offer offer = offerOptional.get();
            offer.setSendStatus("1");
            offer.setResult(emailDTO.getResult());
            offer.setRespondTime(new Timestamp(System.currentTimeMillis()));
            offer.setRemark(emailDTO.getRemark());
            offerRepository.save(offer);
            String content=emailDTO.getContent();
            String applicationId=offer.getApplicationId();
            String companyId=offer.getCompanyId();
            content=content.replaceAll("\\[candidate_name\\]",applicationRepository.getOne(applicationId).getResume().getName());
            content=content.replaceAll("\\[position_name\\]",applicationRepository.getOne(applicationId).getJob().getName());
            content=content.replaceAll("\\[company_name\\]",companyRepository.getOne(companyId).getCompanyName());
            mail.send("chorespore@163.com", emailDTO.getReceiver(), emailDTO.getSubject(), content);
        } else {
            throw new ValidationException("Wrong offer ID.");
        }
    }

    @Override
    public void offer(String offerId, String result) {
        Offer offer = offerRepository.getOne(offerId);
        offer.setResult(result);
        offer.setRespondTime(new Timestamp(System.currentTimeMillis()));
        offerRepository.save(offer);
    }


}
