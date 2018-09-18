package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.Company;
import jp.co.worksap.stm2018.jobhere.model.domain.Item;
import jp.co.worksap.stm2018.jobhere.model.domain.Step;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ItemDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.JobStepDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> list(Company company);

    List<JobDTO> listAll(String userid);

    JobDTO update(Company company, String id, JobDTO jobDTO);

    JobDTO save(Company company, JobDTO jobDTO);

    JobStepDTO getDetail(String id);

    List<Step> getStepList(String jobId);

    void updateJobStep(JobStepDTO jobStepDTO);

    List<Item> getItemList(String itemId);

    void updateStepItem(ItemDTO itemDTO);

}
