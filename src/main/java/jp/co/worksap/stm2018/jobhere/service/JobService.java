package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> list(Company company);

    List<JobDTO> listAll(String userid);

    JobDTO update(Company company, String id, JobDTO jobDTO);

    JobDTO save(Company company, JobDTO jobDTO);

    JobDTO getDetail(String id);

}
