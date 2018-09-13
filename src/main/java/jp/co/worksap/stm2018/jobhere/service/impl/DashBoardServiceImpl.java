package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.response.DashboardDTO;
import jp.co.worksap.stm2018.jobhere.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashBoardServiceImpl implements DashboardService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private StepRepository stepRepository;

    @Override
    public DashboardDTO find(String hrId) {
        int jobCnt = 0, candidateCnt = 0, hrCnt = 0;
        List<String> deptList = new ArrayList<>();

        Map<String, Integer> applicationMap = new HashMap<>();
        Map<String, Integer> offerMap = new HashMap<>();

        User hr = userRepository.getOne(hrId);
        Company company = hr.getCompany();
        List<Job> jobList = company.getJobs();

        jobCnt = jobList.size();

        for (Job job : jobList) {
            String id = job.getId();
            List<Step> stepList = stepRepository.findByJobId(job.getId());
//            stepList.sort((s1, s2) -> -Double.compare(s1.getIndex(), s2.getIndex()));
//            Double stepMax = stepList.get(0).getIndex();
            if (stepList.size() == 0) {
                stepList = stepRepository.findByJobId("-1");
            }

            Double stepMax = stepList.stream().map(step -> step.getIndex()).reduce((max, i) -> max = max > i ? max : i).get();

            for (Application application : job.getApplications()) {
                candidateCnt++;
                String dept = job.getDepartment();
                Double curStep = Double.valueOf(application.getStep());

                if (!applicationMap.containsKey(dept)) {
                    applicationMap.put(dept, 0);
                    offerMap.put(dept, 0);
                }

                applicationMap.put(dept, applicationMap.get(dept) + 1);

                if (Math.abs(curStep - stepMax) < 0.01) {
                    offerMap.put(dept, offerMap.get(dept) + 1);
                }

            }
        }

        for (User user : company.getUsers()) {
            if (user.getRole().equals("hr"))
                hrCnt++;
        }

        applicationMap.forEach((key, value) -> {
            String item = key + "-" + value + "-" + offerMap.get(key);
            deptList.add(item);
        });

        return DashboardDTO.builder()
                .jobCnt(jobCnt)
                .candidateCnt(candidateCnt)
                .hrCnt(hrCnt)
                .deptList(deptList).build();
    }
}
