package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;

public interface JobService {

    public JobDTO update(String id,JobDTO jobDTO);
    public JobDTO save(JobDTO jobDTO);
    public JobDTO getDetail(String id);
}
