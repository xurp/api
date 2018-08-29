package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;

import java.util.List;

public interface JobService {

    public List<JobDTO> list();
    public JobDTO update(String id,JobDTO jobDTO);
    public JobDTO save(JobDTO jobDTO);
    public JobDTO getDetail(String id);

}
