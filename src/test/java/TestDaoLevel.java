import com.MonitoringUrlsSpringClass;
import com.common.ParametersMonitoringUrl;
import com.dao.ParametersMonitoringUrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {MonitoringUrlsSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
public class TestDaoLevel {
  @Autowired
  private ParametersMonitoringUrlRepository parametersMonitoringUrlRepository;

  @Test
  public void testFindByName() {
    LocalDateTime beginTime = LocalDateTime.of(2018, Month.JULY, 12, 10, 11, 0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String text = beginTime.format(formatter);
    LocalDateTime formattedBeginTime = LocalDateTime.parse(text, formatter);

    LocalDateTime endTime = LocalDateTime.of(2019, Month.MAY, 16, 12, 0, 0);
    text = endTime.format(formatter);
    LocalDateTime formattedEndTime = LocalDateTime.parse(text, formatter);

    ParametersMonitoringUrl webUrl = new ParametersMonitoringUrl("someUrl", formattedBeginTime,
        formattedEndTime, 1, 2, 3, 200,
        1, 100, "OK");
    webUrl = parametersMonitoringUrlRepository.save(webUrl);

    ParametersMonitoringUrl findByIdInfoWeb = parametersMonitoringUrlRepository.findById(webUrl.getId());

    assertThat(findByIdInfoWeb).isEqualTo(webUrl);
  }
}
