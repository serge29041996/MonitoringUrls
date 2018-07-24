package com.controller;

import com.common.ParametersMonitoringUrl;
import com.common.exceptions.*;
import com.service.IParametersMonitoringUrlService;
import com.service.ParametersMonitoringUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
@Api(value = "parametersMonitoringUrls", description = "Operations pertaining to parameters of monitoring urls")
public class ParametersMonitoringUrlController {

  @Autowired
  private IParametersMonitoringUrlService parametersMonitoringUrlService;

  @ApiOperation(value = "Get a parameters monitoring url by id")
  @GetMapping("/parameters/{id}")
  public ResponseEntity<?> getParameters(@PathVariable("id") long id) {
    try {
      ParametersMonitoringUrl parametersMonitoringUrl = parametersMonitoringUrlService.getParametersById(id);
      return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
    } catch (NotFoundParametersUrlException exception) {
      return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @ApiOperation(value = "Save a parameters monitoring url")
  @PostMapping("/parameters")
  public ResponseEntity<?> saveParameters(@RequestBody @Valid ParametersMonitoringUrl parametersMonitoringUrl,
                                        UriComponentsBuilder ucBuilder) {
    try {
      ParametersMonitoringUrl savedParametersMonitoringUrl =
          parametersMonitoringUrlService.saveParametersUrl(parametersMonitoringUrl);

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(ucBuilder.path("building/{id}").buildAndExpand(savedParametersMonitoringUrl.getId()).toUri());
      return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    } catch (ExistingParametersUrlException existingParametersUrlException) {
      return new ResponseEntity<>(existingParametersUrlException.getMessage(), HttpStatus.CONFLICT);
    } catch (CompareTimesException compareTimesException) {
      return new ResponseEntity<>(compareTimesException.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (EqualTimesException equalTimesException) {
      return new ResponseEntity<>(equalTimesException.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Update parameters monitoring of existing url")
  @PutMapping("/parameters/{id}")
  public ResponseEntity<?> updateParameters(@PathVariable("id") long id,
                                            @RequestBody @Valid ParametersMonitoringUrl parametersMonitoringUrl) {
    try{
      parametersMonitoringUrlService.updateParametersUrl(id, parametersMonitoringUrl);
      return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
    } catch (NotFoundParametersUrlException notFoundParametersUrlException) {
      return new ResponseEntity<>(notFoundParametersUrlException.getMessage(), HttpStatus.NOT_FOUND);
    } catch (EqualParametersException equalParametersException) {
      return new ResponseEntity<>(equalParametersException.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (CompareTimesException compareTimesException) {
      return new ResponseEntity<>(compareTimesException.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (EqualTimesException equalTimesException) {
      return new ResponseEntity<>(equalTimesException.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Delete parameters monitoring url by id")
  @DeleteMapping("/parameters/{id}")
  public ResponseEntity<?> deleteParameters(@PathVariable("id") long id) {
    try{
      parametersMonitoringUrlService.deleteParametersMonitoringUrl(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (NotFoundParametersUrlException notFoundParametersUrlException) {
      return new ResponseEntity<>(notFoundParametersUrlException.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

}
