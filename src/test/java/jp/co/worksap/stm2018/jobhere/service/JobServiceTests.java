package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.dao.CompanyRepository;
import jp.co.worksap.stm2018.jobhere.dao.JobRepository;
import jp.co.worksap.stm2018.jobhere.dao.StepRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.*;
import jp.co.worksap.stm2018.jobhere.model.dto.request.JobDTO;
import jp.co.worksap.stm2018.jobhere.model.dto.response.JobStepDTO;
import jp.co.worksap.stm2018.jobhere.service.impl.JobServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class JobServiceTests {

    @Autowired
    private MockMvc mvc;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private StepRepository stepRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(jobService).build();
    }

    @Test
    public void testList() {
        List<Job> jobList = new ArrayList<>();
        jobList.add(Job.builder().id("1").build());
        jobList.add(Job.builder().id("2").build());
        jobList.add(Job.builder().id("3").build());
        Company company = Company.builder()
                .id("123456")
                .companyName("works")
                .legalPerson("Tom")
                .jobs(jobList)
                .build();
//        Mockito
//                .when(companyRepository.findById(company.getId()))
//                .thenReturn(Optional.empty());
        Mockito
                .when(companyRepository.findById(company.getId()))
                .thenReturn(Optional.of(company));
        List<JobDTO> jobDTOListExpect = new ArrayList<>();
        jobDTOListExpect.add(JobDTO.builder().id("1").build());
        jobDTOListExpect.add(JobDTO.builder().id("2").build());
        jobDTOListExpect.add(JobDTO.builder().id("3").build());
        Assert.assertEquals(jobDTOListExpect, jobService.list(company));
    }

    @Test
    public void testListAll() {
        List<Job> jobList = new ArrayList<>();
        List<JobDTO> jobDTOList = new ArrayList<>();

        Job job1 = Job.builder()
                .id("job-1")
                .build();
        job1.addApplication(Application.builder().id("a1-1").user(User.builder().id("1").build()).build());
        job1.addApplication(Application.builder().id("a1-2").user(User.builder().id("2").build()).build());
        jobList.add(job1);
        jobDTOList.add(JobDTO.builder().id("job-1").applied(true).build());

        Job job2 = Job.builder()
                .id("job-2")
                .build();
        job2.addApplication(Application.builder().id("a2-1").user(User.builder().id("2").build()).build());
        job2.addApplication(Application.builder().id("a2-2").user(User.builder().id("3").build()).build());
        jobList.add(job2);
        jobDTOList.add(JobDTO.builder().id("job-2").applied(false).build());

        Mockito
                .when(jobRepository.findAll())
                .thenReturn(jobList);

        Assert.assertEquals(jobDTOList, jobService.listAll("1"));
    }

    @Test
    public void testUpdateNoneExistedJob() {
        Mockito
                .when(jobRepository.findById("not-exist"))
                .thenReturn(Optional.empty());
        try {
            jobService.update(new Company(), "not-exist", new JobDTO());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), ValidationException.class);
        }
    }

    @Test
    public void testUpdate() {
        Company company = Company.builder()
                .id("c-1")
                .companyName("company")
                .legalPerson("Person")
                .build();
        Job job = Job.builder()
                .id("j-1")
                .company(company)
                .build();
        JobDTO jobDTO = JobDTO.builder()
                .count(10)
                .name("job")
                .detail("job detail")
                .build();
        Mockito
                .when(companyRepository.findById(company.getId()))
                .thenReturn(Optional.of(company));
        Mockito
                .when(jobRepository.findById(job.getId()))
                .thenReturn(Optional.of(job));

        JobDTO jobDTOActual = jobService.update(company, job.getId(), jobDTO);
        Assert.assertEquals(jobDTOActual.getId(), job.getId());
        Assert.assertEquals(jobDTOActual.getCount(), jobDTO.getCount());
        Assert.assertEquals(jobDTOActual.getName(), jobDTO.getName());
        Assert.assertEquals(jobDTOActual.getDetail(), jobDTO.getDetail());
        Assert.assertNotNull(jobDTOActual.getUpdateTime());
    }

    @Test
    public void testSave() {
        Company company = Company.builder()
                .id("cccc")
                .companyName("company")
                .build();
        JobDTO jobDTO = JobDTO.builder()
                .name("job")
                .build();
        Mockito
                .when(companyRepository.findById(company.getId()))
                .thenReturn(Optional.of(company));
        JobDTO jobDTOActual = JobDTO.builder()
                .name("job")
                .build();
        Assert.assertEquals(jobDTOActual.getName(), jobService.save(company, jobDTO).getName());
    }

    @Test
    public void testGetDetailNoneExistedJob() {
        Mockito
                .when(jobRepository.findById("not-exist"))
                .thenReturn(Optional.empty());
        try {
            jobService.getDetail("not-exist");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), ValidationException.class);
        }
    }

    @Test
    public void testGetDetail() {
        Job job = Job.builder()
                .id("j-1")
                .name("job")
                .build();
        Mockito
                .when(jobRepository.findById(job.getId()))
                .thenReturn(Optional.of(job));
        List<Step> stepList = new ArrayList<>();
        stepList.add(Step.builder().jobId(job.getId()).name("step-1").build());
        stepList.add(Step.builder().jobId(job.getId()).name("step-2").build());
        stepList.add(Step.builder().jobId(job.getId()).name("step-3").build());
        Mockito
                .when(stepRepository.findByJobId(job.getId()))
                .thenReturn(stepList);
        JobStepDTO jobStepDTO = JobStepDTO.builder()
                .id(job.getId())
                .name(job.getName())
                .step(stepList)
                .build();
        Assert.assertEquals(jobStepDTO, jobService.getDetail(job.getId()));
    }

    @Test
    public void testGetDetailWithDefaultJobSteps() {
        Job job = Job.builder()
                .id("j-1")
                .name("job")
                .build();
        Mockito
                .when(jobRepository.findById(job.getId()))
                .thenReturn(Optional.of(job));
        Mockito
                .when(stepRepository.findByJobId(job.getId()))
                .thenReturn(new ArrayList<>());

        List<Step> stepList = new ArrayList<>();
        stepList.add(Step.builder().jobId(job.getId()).name("step-1").build());
        stepList.add(Step.builder().jobId(job.getId()).name("step-2").build());
        stepList.add(Step.builder().jobId(job.getId()).name("step-3").build());
        Mockito
                .when(stepRepository.findByJobId("-1"))
                .thenReturn(stepList);

        JobStepDTO jobStepDTO = JobStepDTO.builder()
                .id(job.getId())
                .name(job.getName())
                .step(stepList)
                .build();
        Assert.assertEquals(jobStepDTO, jobService.getDetail(job.getId()));
    }
}
