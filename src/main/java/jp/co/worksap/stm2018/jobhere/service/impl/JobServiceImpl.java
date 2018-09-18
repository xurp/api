package jp.co.worksap.stm2018.jobhere.service.impl;

import jp.co.worksap.stm2018.jobhere.dao.*;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ItemDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.JobStepDTO;
import jp.co.worksap.stm2018.jobhere.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    //    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final StepRepository stepRepository;
    private final ApplicationRepository applicationRepository;
    private final ItemRepository itemRepository;

    @Autowired
    JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository,StepRepository stepRepository,ApplicationRepository applicationRepository,ItemRepository itemRepository) {
        this.itemRepository=itemRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.stepRepository=stepRepository;
        this.applicationRepository=applicationRepository;
    }

    @Override
    public List<JobDTO> list(Company company) {
        Company c = companyRepository.findById(company.getId()).get();//lazy, may be need this
        List<Job> jobList = c.getJobs();
        //List<Job> jobList = jobRepository.findAll();
        List<JobDTO> jobDTOList = new ArrayList<>();
        for (Job job : jobList) {
            jobDTOList.add(JobDTO.builder()
                    .id(job.getId())
                    .name(job.getName())
                    .detail(job.getDetail())
                    .count(job.getCount())
                    .department(job.getDepartment())
                    .remark(job.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(job.getUpdateTime()).company(job.getCompany()).build());
        }
        return jobDTOList;
    }

    @Override
    public List<JobDTO> listAll(String userid) {
        List<Job> jobList = jobRepository.findAll();
        List<JobDTO> jobDTOList = new ArrayList<>();
        for (Job job : jobList) {
            List<Application> applications=job.getApplications();
            boolean flag=false;
            for(Application a:applications){
                if(a.getUser().getId().equals(userid)){
                    flag=true;//has applied
                    break;
                }
            }
            if(flag)
            jobDTOList.add(JobDTO.builder()
                    .id(job.getId())
                    .name(job.getName())
                    .detail(job.getDetail())
                    .count(job.getCount())
                    .department(job.getDepartment())
                    .remark(job.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(job.getUpdateTime())
                    .company(job.getCompany()).applied(true).build());
            else
                jobDTOList.add(JobDTO.builder()
                        .id(job.getId())
                        .name(job.getName())
                        .detail(job.getDetail())
                        .count(job.getCount())
                        .department(job.getDepartment())
                        .remark(job.getRemark())
                        .createTime(job.getCreateTime())
                        .updateTime(job.getUpdateTime())
                        .company(job.getCompany()).applied(false).build());
        }
        return jobDTOList;
    }

    @Transactional
    @Override
    public JobDTO update(Company company, String id, JobDTO jobDTO) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setUpdateTime(timestamp);
            job.setCount(jobDTO.getCount());
            job.setDepartment(jobDTO.getDepartment());
            job.setDetail(jobDTO.getDetail());
            job.setName(jobDTO.getName());
            job.setRemark(jobDTO.getRemark());
            jobRepository.save(job);
            Company c = companyRepository.findById(company.getId()).get();//lazy, so it is necessary to search from db. if set it to eagar, all should be eagar
            job.setCompany(c);
            c.addJob(job);
            return JobDTO.builder()
                    .id(id)
                    .name(jobDTO.getName())
                    .detail(jobDTO.getDetail())
                    .count(jobDTO.getCount())
                    .department(jobDTO.getDepartment())
                    .remark(jobDTO.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(timestamp).company(c).build();
        } else {
            throw new ValidationException("Job id does not exist!");
        }
    }

    @Transactional
    @Override
    public JobDTO save(Company company, JobDTO jobDTO) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Job jobToSave = new Job();
        jobToSave.setId(uuid);
        jobToSave.setName(jobDTO.getName());
        jobToSave.setDetail(jobDTO.getDetail());
        jobToSave.setCount(jobDTO.getCount());
        jobToSave.setDepartment(jobDTO.getDepartment());
        jobToSave.setRemark(jobDTO.getRemark());
        jobToSave.setUpdateTime(timestamp);
        //jobRepository.save(jobToSave);
        Company c = companyRepository.findById(company.getId()).get();//lazy, so it is necessary to search from db. if set it to eagar, all should be eagar
        jobToSave.setCompany(c);
        c.addJob(jobToSave);
        companyRepository.save(c);
        return JobDTO.builder().id(uuid).name(jobDTO.getName())
                .detail(jobDTO.getDetail())
                .count(jobDTO.getCount())
                .department(jobDTO.getDepartment())
                .remark(jobDTO.getRemark())
                .createTime(timestamp)
                .updateTime(timestamp).company(c).build();
    }

    @Override
    public JobStepDTO getDetail(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            List<Step> stepList=stepRepository.findByJobId(job.getId());
            if(stepList==null||stepList.size()==0)
                stepList=stepRepository.findByJobId("-1");
            List<Step> sortedList = stepList.stream().sorted((a, b) -> Double.compare(a.getIndex(),b.getIndex())).collect(Collectors.toList());
            return JobStepDTO.builder().id(job.getId()).name(job.getName())
                    .detail(job.getDetail())
                    .count(job.getCount())
                    .department(job.getDepartment())
                    .remark(job.getRemark())
                    .createTime(job.getCreateTime())
                    .updateTime(job.getUpdateTime()).step(sortedList).build();
        } else {
            throw new ValidationException("Job id does not exist!");
        }
    }

    @Override
    public List<Step> getStepList(String jobId) {
        List<Step> stepList=stepRepository.findByJobId(jobId);
        if(stepList==null||stepList.size()==0)
            stepList=stepRepository.findByJobId("-1");
        List<Step> sortedList = stepList.stream().sorted((a, b) -> Double.compare(a.getIndex(),b.getIndex())).collect(Collectors.toList());
        return sortedList;
    }

    @Transactional
    @Override
    public void updateJobStep(JobStepDTO jobStepDTO) {
        //judge by step name
        String jobId=jobStepDTO.getId();
        List<Step> newStepList=jobStepDTO.getStep();
        List<Step> stepList=stepRepository.findByJobId(jobId);
        boolean flag=false;
        if(stepList==null||stepList.size()==0) {
            stepList = stepRepository.findByJobId("-1");
            flag=true;
        }
        for(Step s:stepList)
            System.out.println(s.getName());
        Optional<Job> jobOptional=jobRepository.findById(jobId);
        if(!jobOptional.isPresent()){
            throw new ValidationException("Job id is wrong");
        }
        List<Application> applicationList=jobOptional.get().getApplications();
        System.out.println(applicationList.size());
        Set<String> stepSet=new HashSet<>();//steps that has applications, not all steps
        for(Application application:applicationList) {
            for(Step step:stepList) {
                 if (Math.abs(Double.parseDouble(application.getStep().replace("+", "").replace("-", "")) - step.getIndex()) < 0.01) {
                        stepSet.add(step.getName());
                 }
        }
        }
        Set<String> newStepSet=new HashSet<>();
        newStepList.forEach(a->newStepSet.add(a.getName()));
        for(String s:stepSet){
            if(!newStepSet.contains(s))
                throw new ValidationException("You can't remove step which has applications now.");
        }
        if(flag){//the job uses default steps, create its new steps
            List<Application> tosave=new ArrayList<>();
            for (Step newStep : newStepList) {
                newStep.setId(UUID.randomUUID().toString().replace("-", ""));
                newStep.setJobId(jobId);
                stepRepository.save(newStep);
                for(Application application:applicationList){

                    String stepName="";
                    for(Step step:stepList) {
                        System.out.println(step.getName()+" "+step.getIndex());
                         System.out.println("application"+Double.parseDouble(application.getStep().replace("+", "").replace("-", "")));
                        if (Math.abs(Double.parseDouble(application.getStep().replace("+", "").replace("-", "")) - step.getIndex()) < 0.01) {

                            stepName=step.getName();
                            break;
                        }

                    }
                    System.out.println("######################");
                    System.out.println(stepName);
                    System.out.println(newStep.getName());
                    System.out.println(stepName.equals(newStep.getName()));
                    if(stepName.equals(newStep.getName())){
                        //if save new application:ConcurrentModificationException null--for(Application application:applicationList)
                        //I do not know why
                        Application newApplication=applicationRepository.findById(application.getId()).get();
                        String pre="";
                        if(application.getStep().charAt(0)=='+'||application.getStep().charAt(0)=='-')
                            pre+=application.getStep().charAt(0);
                        if(application.getStep().charAt(1)=='-')
                            pre+='-';
                        newApplication.setStep(pre+newStep.getIndex()+"");
                        tosave.add(newApplication);
                        //applicationRepository.save(newApplication);

                    }
                }
            }
            for(int i=0;i<tosave.size();i++){
                applicationRepository.save(tosave.get(i));
            }
        }
        else {//the job has its own steps
            List<Application> tosave=new ArrayList<>();
            for (Step newStep : newStepList) {
                newStep.setJobId(jobId);
                for (Step step : stepList) {
                    if (newStep.getName().equals(step.getName())) {
                        newStep.setId(step.getId());
                        break;
                    }

                }
                for(Application application:applicationList){
                    String stepName="";
                    for(Step step:stepList) {
                        if (Math.abs(Double.parseDouble(application.getStep().replace("+", "").replace("-", "")) - step.getIndex()) < 0.01) {
                            stepName=step.getName();
                            break;
                        }

                    }
                    if(stepName.equals(newStep.getName())){
                        Application newApplication=applicationRepository.findById(application.getId()).get();
                        String pre="";
                        if(application.getStep().charAt(0)=='+'||application.getStep().charAt(0)=='-')
                            pre+=application.getStep().charAt(0);
                        if(application.getStep().charAt(1)=='-')
                            pre+='-';
                        newApplication.setStep(pre+newStep.getIndex()+"");
                        tosave.add(newApplication);

                        //applicationRepository.save(application);
                    }
                }
            }
            for(int i=0;i<tosave.size();i++){
                applicationRepository.save(tosave.get(i));
            }
            stepList.forEach(a->stepRepository.deleteById(a.getId()));
            newStepList.forEach(a->stepRepository.save(a));
        }

    }

    @Override
    public List<Item> getItemList(String stepId) {
        List<Item> itemList=stepRepository.findById(stepId).get().getItems();
        return itemList;
    }

    @Override
    public void updateStepItem(ItemDTO itemDTO) {
        Optional<Step> stepOptional=stepRepository.findById(itemDTO.getStepId());
        if(!stepOptional.isPresent())
            throw new ValidationException("StepId error.");
        Step step=stepOptional.get();
        List<Item> olditemList=step.getItems();
        List<Item> newitemList=itemDTO.getItemList();
        Optional<Job> jobOptional=jobRepository.findById(itemDTO.getJobId());
        if(!jobOptional.isPresent()){
            throw new ValidationException("Job id is wrong");
        }
        List<Application> applicationList=jobOptional.get().getApplications();
        Set<String> stepSet=new HashSet<>();//steps that has applications, not all steps
        for(Application application:applicationList) {
                if (Math.abs(Double.parseDouble(application.getStep().replace("+", "").replace("-", "")) - step.getIndex()) < 0.01) {
                    throw new ValidationException("You can't remove items which has assessments now.");
                }
        }
            for (Item newitem : newitemList) {
                for (Item olditem : olditemList) {
                    if (newitem.getName().equals(olditem.getName())) {
                        newitem.setId(olditem.getId());
                        break;
                    }
                }
            }

        olditemList.forEach(a->itemRepository.deleteById(a.getId()));
        newitemList.forEach(a->itemRepository.save(a));


    }
}

