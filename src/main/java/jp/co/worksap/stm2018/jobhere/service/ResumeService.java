package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;

import java.util.List;

public interface ResumeService {
    void update(String id, ResumeDTO jobDTO);

    ResumeDTO find(String id);

    ResumeDTO save(Resume resume);

    List<ResumeDTO> list();
}
