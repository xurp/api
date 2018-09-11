package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.ApplicationRepository;
import jp.co.worksap.stm2018.jobhere.dao.AppointedTimeRepository;
import jp.co.worksap.stm2018.jobhere.dao.OfferRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Application;
import jp.co.worksap.stm2018.jobhere.model.domain.AppointedTime;
import jp.co.worksap.stm2018.jobhere.model.domain.Offer;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ApplicationDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.EmailDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.OfferDTO;
import jp.co.worksap.stm2018.jobhere.service.AppointedTimeService;
import jp.co.worksap.stm2018.jobhere.service.OfferService;
import jp.co.worksap.stm2018.jobhere.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointedTimeServiceImpl implements AppointedTimeService {

    @Autowired
    private AppointedTimeRepository appointedTimeRepository;


    @Override
    public List<AppointedTime> getByOperationIdAndCooperatorId(String id1, String id2) {
        List<AppointedTime>  appointedTimeList=appointedTimeRepository.getByOperationIdAndCooperatorId(id1,id2);
        if(appointedTimeList==null || appointedTimeList.size()==0){
            throw new ValidationException("The link is wrong");
        }
        return appointedTimeList;
    }
}
