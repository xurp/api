package jp.co.worksap.stm2018.jobhere.dao;

import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ResumeRepository extends JpaRepository<Resume, String> {
    Optional<Resume> findById(String id);

    List<Resume> getByOpen(boolean open);
}