import com.MonitoringUrlsSpringClass;
import com.common.ParametersMonitoringUrl;
import com.dao.ParametersMonitoringUrlRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
  public void testFindById() {
    Date beginTime = getFormattedISODate(12, 0, 0);
    Date endTime = getFormattedISODate(23, 59, 59);

    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("someUrl", beginTime,
        endTime, 1, 2, 3, 200,
        1, 100);
    webParams = parametersMonitoringUrlRepository.save(webParams);

    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository.findById(webParams.getId());

    assertThat(findByIdParams).isEqualTo(webParams);
  }

  @Test
  public void testFindByUrl() {
    Date beginTime = getFormattedISODate(12, 0, 0);
    Date endTime = getFormattedISODate(23, 59, 59);
    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    parametersMonitoringUrlRepository.save(webParams);

    ParametersMonitoringUrl findByUrlParams = parametersMonitoringUrlRepository.findByUrl(webParams.getUrl());

    assertThat(findByUrlParams).isEqualTo(webParams);
  }

  @Test
  public void testDeleteEntity() {
    Date beginTime = getFormattedISODate(12, 0, 0);
    Date endTime = getFormattedISODate(23, 59, 59);
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
    Date beginTime = getFormattedISODate(12, 0, 0);
    Date endTime = getFormattedISODate(23, 59, 59);
    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    webParams = parametersMonitoringUrlRepository.save(webParams);
    webParams.setMaxSizeResponse(150);
    parametersMonitoringUrlRepository.save(webParams);

    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository.findById(webParams.getId());

    assertThat(findByIdParams.getMaxSizeResponse()).isEqualTo(webParams.getMaxSizeResponse());
  }

  @Test
  public void testCountEntity() {
    Date beginTime = getFormattedISODate(12, 0, 0);
    Date endTime = getFormattedISODate(23, 59, 59);
    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    parametersMonitoringUrlRepository.save(webParams);

    beginTime = getFormattedISODate(10, 0, 0);
    endTime = getFormattedISODate(13, 59, 59);
    webParams = new ParametersMonitoringUrl("test-url", beginTime,
        endTime, 1, 2, 3, 200,
        1, 1000, "test");
    parametersMonitoringUrlRepository.save(webParams);

    long numberEntities = parametersMonitoringUrlRepository.count();

    assertThat(numberEntities).isEqualTo(2);
  }

  private Date getFormattedISODate(int hour, int minute, int second) {
    int year = 2200,
        month = 11,
        day = 1;

    Calendar calendarBeginTime = new GregorianCalendar(year, month, day, hour, minute, second);
    Date time = calendarBeginTime.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    String result = dateFormat.format(time);
    Date formattedTime = null;
    try {
      formattedTime = dateFormat.parse(result);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return formattedTime;
  }

  @After
  public void clearDatabase() {
    parametersMonitoringUrlRepository.deleteAll();
  }
}
