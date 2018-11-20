package com.controller;

import com.MonitoringUrlsSpringClass;
import com.common.DataUtils;
import com.common.ParametersMonitoringUrl;
import com.controller.exceptions.handler.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.IParametersMonitoringUrlService;
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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class ParametersMonitoringUrlControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private IParametersMonitoringUrlService parametersMonitoringUrlService;

  @Before
  public void deleteAllBuildings(){
    parametersMonitoringUrlService.deleteAll();
  }

  @Test
  public void testGetExistParameters() throws Exception {
    ParametersMonitoringUrl webParameters = formValidParametersForUrl("testUrl");

    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService.saveParametersUrl(webParameters);

    mvc.perform(get("/parameters/{id}", savedParameters.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("url", is("testUrl")));

  }

  @Test
  public void testGetNotExistParameters() throws Exception {
    MvcResult mvcResult =
        mvc.perform(get("/parameters/{id}", 0)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()).andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat(message).isEqualTo("Parameters was not found");
  }

  @Test
  public void testPostValidParameters() throws Exception {
    ParametersMonitoringUrl webParameters = formValidParametersForUrl("testUrl");

    MvcResult mvcResult =
        mvc.perform(
            post("/parameters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(webParameters)))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", containsString("http://localhost/parameters/")))
            .andReturn();

    long id = Long.parseLong(mvcResult.getResponse().getRedirectedUrl().split("/")[4]);

    mvc.perform(
        get("/parameters/{id}",id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().
            contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("url", is("testUrl")));
  }

  @Test
  public void testPostWrongParameters() throws Exception {
    ParametersMonitoringUrl webParameters = formValidParametersForUrl("testUrl");
    webParameters.setMinSizeResponse(100);
    webParameters.setMaxSizeResponse(100);

    MvcResult mvcResult =
        mvc.perform(
            post("/parameters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(webParameters)))
            .andExpect(status().isBadRequest())
            .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat(message).isEqualTo("Min and max size of response is equal");
  }

  @Test
  public void testPutValidParameters() throws Exception {
    ParametersMonitoringUrl webParameters = formValidParametersForUrl("testUrl");
    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService.saveParametersUrl(webParameters);

    savedParameters.setTimeResponseOk(3);
    savedParameters.setTimeResponseWarning(4);
    savedParameters.setTimeResponseCritical(5);

    mvc.perform(
        put("/parameters/{id}", savedParameters.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(savedParameters)))
        .andExpect(status().isOk());

    mvc.perform(
        get("/parameters/{id}", savedParameters.getId())
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
    ParametersMonitoringUrl webParameters = formValidParametersForUrl("testUrl");
    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService.saveParametersUrl(webParameters);

    savedParameters.setTimeResponseOk(5);
    savedParameters.setTimeResponseWarning(4);
    savedParameters.setTimeResponseWarning(5);

    MvcResult mvcResult = mvc.perform(
        put("/parameters/{id}", savedParameters.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(savedParameters)))
        .andExpect(status().isBadRequest()).andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiException(requestBody).getMessage();
    assertThat("Time response should not be equal for status ok and other status").isSubstringOf(message);
  }

  @Test
  public void testDeleteExistingParameters() throws Exception {
    ParametersMonitoringUrl webParameters = formValidParametersForUrl("testUrl");
    ParametersMonitoringUrl savedParameters = parametersMonitoringUrlService.saveParametersUrl(webParameters);

    mvc.perform(
        delete("/parameters/{id}",savedParameters.getId()))
        .andExpect(status().isOk());

    mvc.perform(
        get("/parameters/{id}", savedParameters.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteNotExistingParameters() throws Exception {
    mvc.perform(
        delete("/parameters/{id}",1))
        .andExpect(status().isNotFound());
  }

  @Before
  public void clearDatabase() {
    parametersMonitoringUrlService.deleteAll();
  }

  private ParametersMonitoringUrl formValidParametersForUrl(String url) {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 0, 0);

    return new ParametersMonitoringUrl(url, beginTime,
        endTime, 1, 2, 3, 200,
        1, 100);
  }


  private String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ApiError asJavaObjectApiException(final String JSONResponse){
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(JSONResponse,ApiError.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
