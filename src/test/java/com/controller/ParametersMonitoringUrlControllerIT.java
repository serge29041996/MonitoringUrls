package com.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.MonitoringUrlsSpringClass;
import com.common.DataUtils;
import com.common.TimeData;
import com.common.entities.ParametersMonitoringUrl;
import com.controller.exceptions.handler.ApiExceptionData;
import com.controller.exceptions.handler.ApiValidationExceptionData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.IParametersMonitoringUrlService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Realization tests for controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = MonitoringUrlsSpringClass.class)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:test.properties")
@AutoConfigureTestDatabase
public class ParametersMonitoringUrlControllerIT {
  @Autowired
  private MockMvc mvc;

  private static final String URL_FOR_TEST = "testUrl";
  private static final String URL_FOR_PARAMETERS_ID = "/parameters/{id}";
  private static final String URL_FOR_PARAMETERS_POST = "/parameters";

  @Autowired
  private IParametersMonitoringUrlService parametersMonitoringUrlService;

  @Before
  public void deleteAllBuildings(){
    parametersMonitoringUrlService.deleteAll();
  }

  @Test
  public void testGetExistParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));

    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService
        .saveParametersUrl(webParameters);

    mvc.perform(get(URL_FOR_PARAMETERS_ID, savedParameters.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("url", is(URL_FOR_TEST)));

  }

  @Test
  public void testGetParametersWithStringId() throws Exception {
    MvcResult mvcResult =
    mvc.perform(get(URL_FOR_PARAMETERS_ID, "some id")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat(message).isEqualTo("The parameter 'id' of value 'some id' could not be converted" +
        " to type 'long'");
  }

  @Test
  public void testGetNotExistParameters() throws Exception {
    long idNotExistParameters = 0;
    MvcResult mvcResult =
        mvc.perform(get(URL_FOR_PARAMETERS_ID, idNotExistParameters)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()).andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat(message).isEqualTo("Parameters with id " + idNotExistParameters + " was not found");
  }

  @Test
  public void testPostValidParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));

    MvcResult mvcResult =
        mvc.perform(
            post(URL_FOR_PARAMETERS_POST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(webParameters)))
            .andExpect(status().isCreated())
            .andExpect(header().string("location",
                containsString("http://localhost/parameters/")))
            .andReturn();

    long id = Long.parseLong(mvcResult.getResponse().getRedirectedUrl().split("/")[4]);

    mvc.perform(
        get(URL_FOR_PARAMETERS_ID,id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().
            contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("url", is(URL_FOR_TEST)));
  }

  @Test
  public void testPostWrongParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));
    webParameters.setMinSizeResponse(100);
    webParameters.setMaxSizeResponse(100);

    MvcResult mvcResult =
        mvc.perform(
            post(URL_FOR_PARAMETERS_POST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(webParameters)))
            .andExpect(status().isBadRequest())
            .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat(message).isEqualTo("Min and max size of response is equal");
  }

  @Test
  public void testPostInvalidParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));
    webParameters.setExpectedCodeResponse(99);

    MvcResult mvcResult =
        mvc.perform(
            post(URL_FOR_PARAMETERS_POST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(webParameters)))
            .andExpect(status().isBadRequest())
            .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    List<ApiValidationExceptionData> fieldExceptions = asJavaObjectApiException(requestBody)
        .getSubErrors();
    assertThat(fieldExceptions.get(0).getMessage()).isEqualTo("Expected code of response should" +
        " be greater than 100");
  }

  @Test
  public void testPostMalformedJSONParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));
    String stringParametersJSON = asJsonString(webParameters);
    String changedJSONStringParameters = stringParametersJSON.replace(":", ";");

    MvcResult mvcResult =
        mvc.perform(
            post(URL_FOR_PARAMETERS_POST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(changedJSONStringParameters))
            .andExpect(status().isBadRequest())
            .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat(message).isEqualTo("Malformed JSON request");
  }

  @Test
  public void testPostExistingParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));
    parametersMonitoringUrlService.saveParametersUrl(webParameters);

    MvcResult mvcResult =
        mvc.perform(
            post(URL_FOR_PARAMETERS_POST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(webParameters)))
            .andExpect(status().isConflict())
            .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat(message).isEqualTo("Parameters for url testUrl already exist");
  }

  @Test
  public void testPutValidParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));
    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService
        .saveParametersUrl(webParameters);

    savedParameters.setTimeResponseOk(3);
    savedParameters.setTimeResponseWarning(4);
    savedParameters.setTimeResponseCritical(5);

    mvc.perform(
        put(URL_FOR_PARAMETERS_ID, savedParameters.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(savedParameters)))
        .andExpect(status().isOk());

    mvc.perform(
        get(URL_FOR_PARAMETERS_ID, savedParameters.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().
            contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("timeResponseOk", is(3)))
        .andExpect(jsonPath("timeResponseWarning", is(4)))
        .andExpect(jsonPath("timeResponseCritical", is(5)));
  }

  @Test
  public void testPutWrongParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));
    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService
        .saveParametersUrl(webParameters);

    savedParameters.setTimeResponseOk(5);
    savedParameters.setTimeResponseWarning(4);
    savedParameters.setTimeResponseWarning(5);

    MvcResult mvcResult = mvc.perform(
        put(URL_FOR_PARAMETERS_ID, savedParameters.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(savedParameters)))
        .andExpect(status().isBadRequest()).andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat("Time response should not be equal for status ok and other status")
        .isSubstringOf(message);
  }

  @Test
  public void testDeleteExistingParameters() throws Exception {
    ParametersMonitoringUrl webParameters = DataUtils.initValidParametersWithDateMonitoring(
        URL_FOR_TEST, new TimeData(12,0,0),
        new TimeData(23,0,0));
    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService
        .saveParametersUrl(webParameters);

    mvc.perform(
        delete(URL_FOR_PARAMETERS_ID,savedParameters.getId()))
        .andExpect(status().isOk());

    mvc.perform(
        get(URL_FOR_PARAMETERS_ID, savedParameters.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteNotExistingParameters() throws Exception {
    mvc.perform(
        delete(URL_FOR_PARAMETERS_ID,1))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetStatusForUrl() throws Exception {
    ParametersMonitoringUrl parametersMonitoringUrl = initializeExistUrlInformation();
    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService
        .saveParametersUrl(parametersMonitoringUrl);
    mvc.perform(
        get("/status/{id}",savedParameters.getId()))
        .andExpect(status().isOk());
  }

  private ParametersMonitoringUrl initializeExistUrlInformation() {
    ParametersMonitoringUrl parametersMonitoringUrl = DataUtils
        .initValidParametersWithDateMonitoring("https://www.gog.com/",
            new TimeData(0,0,0),
            new TimeData(23,59,59));

    parametersMonitoringUrl.setTimeResponseOk(1);
    parametersMonitoringUrl.setTimeResponseWarning(2);
    parametersMonitoringUrl.setTimeResponseCritical(3);
    parametersMonitoringUrl.setExpectedCodeResponse(200);
    parametersMonitoringUrl.setMinSizeResponse(500000);
    parametersMonitoringUrl.setMaxSizeResponse(560950);
    return parametersMonitoringUrl;
  }

  private String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ApiExceptionData asJavaObjectApiException(final String JSONResponse){
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(JSONResponse,ApiExceptionData.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
