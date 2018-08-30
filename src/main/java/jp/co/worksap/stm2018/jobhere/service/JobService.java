package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> list();

    JobDTO update(String id, JobDTO jobDTO);

    JobDTO save(JobDTO jobDTO);

    JobDTO getDetail(String id);

}
