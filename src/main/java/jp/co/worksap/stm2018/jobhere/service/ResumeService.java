package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;

public interface ResumeService {
    void update(String id,ResumeDTO jobDTO);

    ResumeDTO find(String id);

    ResumeDTO save(Resume resume);
}
