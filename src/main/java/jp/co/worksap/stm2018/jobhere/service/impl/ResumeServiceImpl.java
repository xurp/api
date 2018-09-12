package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.ResumeRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResumeRepository resumeRepository;


    @Override
    public void update(String id, ResumeDTO resumeDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Resume resume = user.getResume();
            resume.setName(resumeDTO.getName());
            resume.setGender(resumeDTO.getGender());
            resume.setAge(resumeDTO.getAge());
            resume.setEmail(resumeDTO.getEmail());
            resume.setPhone(resumeDTO.getPhone());
            resume.setDegree(resumeDTO.getDegree());
            resume.setSchool(resumeDTO.getSchool());
            resume.setMajor(resumeDTO.getMajor());
            resume.setIntro(resumeDTO.getIntro());
            resume.setOpen(resumeDTO.isOpen());
            userRepository.save(user);
        } else {
            throw new ValidationException("User id does not exist!");
        }
    }

    @Override
    public ResumeDTO find(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Resume resume = user.getResume();
            return ResumeDTO.builder()
                    .id(resume.getId())
                    .name(resume.getName())
                    .gender(resume.getGender())
                    .age(resume.getAge())
                    .email(resume.getEmail())
                    .phone(resume.getPhone())
                    .degree(resume.getDegree())
                    .school(resume.getSchool())
                    .major(resume.getMajor())
                    .intro(resume.getIntro())
                    .open(resume.isOpen())
                    .build();
        } else {
            throw new ValidationException("User id does not exist!");
        }
    }

    @Override
    public ResumeDTO save(Resume resume) {
        /*Resume resume=new Resume();
        resume.setId(resumeDTO.getId());
        resume.setAge(resumeDTO.getAge());
        resume.setDegree(resumeDTO.getDegree());
        resume.setEmail(resumeDTO.getEmail());
        resume.setGender(resumeDTO.getGender());
        resume.setIntro(resumeDTO.getIntro());
        resume.setMajor(resumeDTO.getMajor());
        resume.setName(resumeDTO.getName());
        resume.setOpen(resumeDTO.isOpen());
        resume.setPhone(resumeDTO.getPhone());
        resume.setSchool(resumeDTO.getSchool());*/
        resumeRepository.save(resume);
        return ResumeDTO.builder()
                .id(resume.getId())
                .name(resume.getName())
                .gender(resume.getGender())
                .age(resume.getAge())
                .email(resume.getEmail())
                .phone(resume.getPhone())
                .degree(resume.getDegree())
                .school(resume.getSchool())
                .major(resume.getMajor())
                .intro(resume.getIntro())
                .open(resume.isOpen())
                .build();
    }

    @Override
    public List<ResumeDTO> list(String keyword) {
        List<User> userList = userRepository.getByRole("candidate");

        List<ResumeDTO> resumeDTOList = new ArrayList<>();
        for (User user : userList) {
            Resume resume = user.getResume();
            String info = resume.getSchool() + " " + resume.getMajor() + " " + resume.getDegree() + " " + resume.getIntro();
            info=info.toLowerCase();
            keyword = keyword.toLowerCase();
            if (resume.isOpen() && (keyword.equals("") || info.contains(keyword)))
                resumeDTOList.add(ResumeDTO.builder()
                        .id(resume.getId())
                        .name(resume.getName())
                        .gender(resume.getGender())
                        .age(resume.getAge())
                        .email(resume.getEmail())
                        .phone(resume.getPhone())
                        .degree(resume.getDegree())
                        .school(resume.getSchool())
                        .major(resume.getMajor())
                        .intro(resume.getIntro())
                        .open(resume.isOpen()).build());
        }
        return resumeDTOList;
    }
}
