package jp.co.worksap.stm2018.jobhere.service;

import jp.co.worksap.stm2018.jobhere.dao.ResumeRepository;
import jp.co.worksap.stm2018.jobhere.dao.UserRepository;
import jp.co.worksap.stm2018.jobhere.http.ValidationException;
import jp.co.worksap.stm2018.jobhere.model.domain.Resume;
import jp.co.worksap.stm2018.jobhere.model.domain.User;
import jp.co.worksap.stm2018.jobhere.model.dto.request.ResumeDTO;
import jp.co.worksap.stm2018.jobhere.service.impl.ResumeServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ResumeServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private ResumeServiceImpl resumeService;

    @Test
    public void testUpdateWithNoneExistedUser() {
        Mockito
                .when(userRepository.findById("not-exist"))
                .thenReturn(Optional.empty());
        try {
            resumeService.update("not-exist", new ResumeDTO());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), ValidationException.class);
        }
    }

    @Test
    public void testUpdate() {
        Resume resume = Resume.builder()
                .name("person A")
                .gender("male")
                .age(10)
                .email("A@test.com")
                .phone("13012345678")
                .degree("master")
                .school("pku")
                .major("cs")
                .intro("intro")
                .open(false)
                .build();
        User user = User.builder()
                .id("uuu")
                .username("user")
                .resume(resume)
                .build();
        Mockito
                .when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        ResumeDTO resumeDTO = ResumeDTO.builder()
                .name("person B")
                .gender("female")
                .age(20)
                .email("B@test.com")
                .phone("13112345678")
                .degree("professor")
                .school("mit")
                .major("science")
                .intro("intro changed")
                .open(true)
                .build();
        resumeService.update(user.getId(), resumeDTO);
    }

    @Test
    public void testFindWithNoneExistedUser() {
        Mockito
                .when(userRepository.findById("not-exist"))
                .thenReturn(Optional.empty());
        try {
            resumeService.find("not-exist");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), ValidationException.class);
        }
    }

    @Test
    public void testFind() {
        Resume resume = Resume.builder()
                .name("person A")
                .gender("male")
                .age(10)
                .email("A@test.com")
                .phone("13012345678")
                .degree("master")
                .school("pku")
                .major("cs")
                .intro("intro")
                .open(false)
                .build();
        User user = User.builder()
                .id("uuu")
                .resume(resume)
                .build();
        Mockito
                .when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        ResumeDTO resumeDTO = ResumeDTO.builder()
                .name("person A")
                .gender("male")
                .age(10)
                .email("A@test.com")
                .phone("13012345678")
                .degree("master")
                .school("pku")
                .major("cs")
                .intro("intro")
                .open(false)
                .build();
        Assert.assertEquals(resumeDTO, resumeService.find(user.getId()));
    }

    @Test
    public void testSave() {
        Resume resume = Resume.builder()
            .name("person A")
            .gender("male")
            .age(10)
            .email("A@test.com")
            .phone("13012345678")
            .degree("master")
            .school("pku")
            .major("cs")
            .intro("intro")
            .open(false)
            .build();
        ResumeDTO resumeDTO = ResumeDTO.builder()
                .name("person A")
                .gender("male")
                .age(10)
                .email("A@test.com")
                .phone("13012345678")
                .degree("master")
                .school("pku")
                .major("cs")
                .intro("intro")
                .open(false)
                .build();
        Assert.assertEquals(resumeDTO, resumeService.save(resume));
    }
}
