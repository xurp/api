package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.InvitationRepository;
import jp.co.worksap.stm2018.jobhere.dao.ResumeRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.model.domain.Invitation;
import jp.co.worksap.stm2018.jobhere.model.domain.Job;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.response.InvitationDTO;
import jp.co.worksap.stm2018.jobhere.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Override
    public InvitationDTO create(String resumeId, String jobId) {
        Resume resume = resumeRepository.getOne(resumeId);
        User user = userRepository.getByResume(resume);

        Invitation invitation = Invitation.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .userId(user.getId())
                .jobId(jobId)
                .inviteTime(new Timestamp(System.currentTimeMillis())).build();

        invitationRepository.save(invitation);

        return InvitationDTO.builder()
                .invitationId(invitation.getId())
                .userId(invitation.getUserId())
                .jobId(invitation.getJobId())
                .inviteTime(invitation.getInviteTime()).build();
    }

    @Override
    public List<InvitationDTO> list(User user) {
        List<InvitationDTO> invitationDTOList = new ArrayList<>();
        if (user.getRole().equals("hr")) {
            List<Job> jobList = user.getCompany().getJobs();
            for (Job job : jobList) {
                List<Invitation> invitationList = invitationRepository.getByJobId(job.getId());
                for (Invitation invitation : invitationList) {
                    invitationDTOList.add(InvitationDTO.builder()
                            .invitationId(invitation.getId())
                            .userId(invitation.getUserId())
                            .jobId(invitation.getJobId())
                            .inviteTime(invitation.getInviteTime()).build());
                }
            }
        } else if (user.getRole().equals("candidate")) {
            List<Invitation> invitationList = invitationRepository.getByUserId(user.getId());
            for (Invitation invitation : invitationList) {
                invitationDTOList.add(InvitationDTO.builder()
                        .invitationId(invitation.getId())
                        .userId(invitation.getUserId())
                        .jobId(invitation.getJobId())
                        .inviteTime(invitation.getInviteTime()).build());
            }
        }

        return invitationDTOList;
    }
}
