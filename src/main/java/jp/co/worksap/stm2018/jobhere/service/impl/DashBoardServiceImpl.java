package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.response.DashboardDTO;
import jp.co.worksap.stm2018.jobhere.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DashBoardServiceImpl implements DashboardService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private OfferRepository offerRepository;

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
                Double curStep = Double.valueOf(application.getStep().replaceAll("-", "").replaceAll("\\+", ""));

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

    @Override
    @Transactional
    public List<Map<String, String>> export(String hrId) {
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, ArrayList<Assessment>> applicationMap = new LinkedHashMap<>();

        User hr = userRepository.getOne(hrId);
        List<Assessment> assessmentList = assessmentRepository.findAll();
        Company company = hr.getCompany();

        System.out.println(assessmentList.size());

        for (Assessment assessment : assessmentList) {
            Cooperator cooperator = assessment.getCooperator();
            if (cooperator != null && cooperator.getCompanyId().equals(company.getId())) {
                Application application = applicationRepository.getOne(assessment.getApplicationId());

                if (applicationMap.containsKey(application.getId())) {
                    applicationMap.get(application.getId()).add(assessment);
                } else {
                    ArrayList<Assessment> list = new ArrayList<>();
                    list.add(assessment);
                    applicationMap.put(application.getId(), list);
                }
            }
        }

        applicationMap.forEach((id, list) -> {
            Application application = applicationRepository.getOne(id);
            list.sort(Comparator.comparingDouble(x -> Double.valueOf(x.getStep())));
            Job job = application.getJob();
            Resume resume = application.getResume();
            List<Offer> offerList = offerRepository.findByApplicationId(application.getId());
            List<Step> stepList = stepRepository.findByJobId(job.getId());
            if (stepList.size() == 0) {
                stepList = stepRepository.findByJobId("-1");
            }

            Map<String, String> map = new LinkedHashMap<>();

            map.put(("Position"), job.getName());
            map.put("Candidate", resume.getName());
            map.put("Email", resume.getEmail());
            map.put("Phone", resume.getPhone());
            map.put("Department", job.getDepartment());

            for (Assessment assessment : list) {
                stepList.sort(Comparator.comparingDouble(Step::getIndex));
                Optional<Step> stepOptional = stepList.stream().filter(x -> x.getIndex() >= Double.valueOf(assessment.getStep())).findFirst();
//                Step step = stepRepository.getByJobIdAndIndex(job.getId(), Double.valueOf(assessment.getStep()));

                Cooperator cooperator = assessment.getCooperator();
                map.put("Cooperator", cooperator.getName());
                map.put("Step", stepOptional.get().getName());
                parseScore(assessment.getStep(), assessment.getScore()).forEach((k, v) -> map.put(k, v));
                map.put("Comment", assessment.getComment());
                map.put("Pass", assessment.getPass());
                map.put("Assessment", assessment.getAssessmentTime() == null ? "" : assessment.getAssessmentTime().toString());
            }

            if (offerList.size() == 1)
                map.put("Results", "offer");
            else
                map.put("Results", "");

            mapList.add(map);
        });

        return mapList;

/*
        for (Assessment assessment : assessmentList) {
            Cooperator cooperator = assessment.getCooperator();
            if (cooperator != null && cooperator.getCompanyId().equals(company.getId())) {

                Application application = applicationRepository.getOne(assessment.getApplicationId());
                List<Offer> offerList = offerRepository.findByApplicationId(application.getId());
                Job job = application.getJob();
                Resume resume = application.getResume();
                Step step = stepRepository.getByJobIdAndIndex(job.getId(), Double.valueOf(assessment.getStep()));
                List<Step> stepList = stepRepository.findByJobId(job.getId());
                if (stepList.size() == 0) {
                    stepList = stepRepository.findByJobId("-1");
                }
                stepList.sort(Comparator.comparingDouble(Step::getIndex));

                Double stepMax = stepList.stream().map(s -> s.getIndex()).reduce((max, i) -> max = max > i ? max : i).get();

                if (!applicationMap.containsKey(assessment.getApplicationId())) {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("Candidate", resume.getName());
                    map.put("Email", resume.getEmail());
                    map.put("Phone", resume.getPhone());
                    map.put("Cooperator", cooperator.getName());
                    map.put("Department", cooperator.getDepartment());
                    map.put("Position", application.getJob().getName());
                    map.put("Step", step.getName());
                    map.put("Assessment", assessment.getAssessmentTime() == null ? "" : assessment.getAssessmentTime().toString());
                    parseScore(assessment.getStep(), assessment.getScore()).forEach((k, v) -> map.put(k, v));
                    map.put("Comment", assessment.getComment());
                    map.put("Pass", assessment.getPass());

                    if (Double.valueOf(assessment.getStep()) - stepMax < 0.01) {
                        if (offerList.size() == 1)
                            map.put("Results", "offer");
                        else
                            map.put("Results", "");
                    }

                    mapList.add(map);

                } else {


                }


            }
        }
*/


    }

    Map<String, String> parseScore(String index, String score) {
        Map<String, String> map = new HashMap<>();
        if (score != null && score.contains(";")) {
            String[] items = score.split(";");
            for (String item : items) {
                if (item.contains(":")) {
                    String[] entry = item.split(":");
                    map.put(entry[0] + " " + index, entry[1]);
                }
            }
        }
        return map;
    }
}
