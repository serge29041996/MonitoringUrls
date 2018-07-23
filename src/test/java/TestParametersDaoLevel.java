import com.MonitoringUrlsSpringClass;
import com.common.ParametersMonitoringUrl;
import com.dao.ParametersMonitoringUrlRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {MonitoringUrlsSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
public class TestParametersDaoLevel {
  @Autowired
  private ParametersMonitoringUrlRepository parametersMonitoringUrlRepository;

  @Test
  public void testFindById() {
    Date beginTime = getFormattedISOTime(12, 0, 0);
    Date endTime = getFormattedISOTime(23, 59, 59);

    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("someUrl", beginTime,
        endTime, 1, 2, 3, 200,
        1, 100);
    webParams = parametersMonitoringUrlRepository.save(webParams);

    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository.findById(webParams.getId());

    assertThat(findByIdParams).isEqualTo(webParams);
  }

  @Test
  public void testFindByUrl() {
    Date beginTime = getFormattedISOTime(12, 0, 0);
    Date endTime = getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    parametersMonitoringUrlRepository.save(webParams);

    ParametersMonitoringUrl findByUrlParams = parametersMonitoringUrlRepository.findByUrl(webParams.getUrl());

    assertThat(findByUrlParams).isEqualTo(webParams);
  }

  @Test
  public void testDeleteEntity() {
    Date beginTime = getFormattedISOTime(12, 0, 0);
    Date endTime = getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    webParams = parametersMonitoringUrlRepository.save(webParams);

    parametersMonitoringUrlRepository.delete(webParams.getId());

    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository.findById(webParams.getId());

    assertThat(findByIdParams).isNull();
  }

  @Test
  public void testUpdateEntity() {
    Date beginTime = getFormattedISOTime(12, 0, 0);
    Date endTime = getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    webParams = parametersMonitoringUrlRepository.save(webParams);
    webParams.setMaxSizeResponse(150);
    parametersMonitoringUrlRepository.save(webParams);

    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository.findById(webParams.getId());

    long countParams = parametersMonitoringUrlRepository.count();

    assertThat(findByIdParams.getMaxSizeResponse()).isEqualTo(webParams.getMaxSizeResponse());
    assertThat(countParams).isEqualTo(1);
  }

  @Test
  public void testCountEntity() {
    Date beginTime = getFormattedISOTime(12, 0, 0);
    Date endTime = getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    parametersMonitoringUrlRepository.save(webParams);

    beginTime = getFormattedISOTime(10, 0, 0);
    endTime = getFormattedISOTime(13, 59, 59);
    webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    parametersMonitoringUrlRepository.save(webParams);

    long numberEntities = parametersMonitoringUrlRepository.count();

    assertThat(numberEntities).isEqualTo(2);
  }

  private Date getFormattedISOTime(int hour, int minute, int second) {
    Calendar calendarBeginTime = Calendar.getInstance();

    calendarBeginTime.clear();
    calendarBeginTime.set(Calendar.HOUR_OF_DAY, hour);
    calendarBeginTime.set(Calendar.MINUTE, minute);
    calendarBeginTime.set(Calendar.SECOND, second);

    return calendarBeginTime.getTime();
  }

  @Before
  public void clearDatabase() {
    parametersMonitoringUrlRepository.deleteAll();
  }
}
