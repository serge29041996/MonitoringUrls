package com.controller;

import com.common.ParametersMonitoringUrl;
import com.common.exceptions.*;
import com.service.IParametersMonitoringUrlService;
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
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> getParameters(@PathVariable("id") long id) {
    ParametersMonitoringUrl parametersMonitoringUrl = parametersMonitoringUrlService.getParametersById(id);
    return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
  }

  @ApiOperation(value = "Save a parameters monitoring url")
  @PostMapping("/parameters")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> saveParameters(@RequestBody @Valid ParametersMonitoringUrl parametersMonitoringUrl,
                                        UriComponentsBuilder ucBuilder) {
    ParametersMonitoringUrl savedParametersMonitoringUrl =
        parametersMonitoringUrlService.saveParametersUrl(parametersMonitoringUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("parameters/{id}").buildAndExpand(savedParametersMonitoringUrl.getId()).toUri());
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Update parameters monitoring of existing url")
  @PutMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> updateParameters(@PathVariable("id") long id,
                                            @RequestBody @Valid ParametersMonitoringUrl parametersMonitoringUrl) {
    parametersMonitoringUrlService.updateParametersUrl(id, parametersMonitoringUrl);
    return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete parameters monitoring url by id")
  @DeleteMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> deleteParameters(@PathVariable("id") long id) {
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
