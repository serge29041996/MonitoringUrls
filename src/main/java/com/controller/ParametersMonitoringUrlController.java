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
@Api(value = "parametersMonitoringUrls", description =
    "Operations pertaining to parameters of monitoring urls")
public class ParametersMonitoringUrlController {

  @Autowired
  private IParametersMonitoringUrlService parametersMonitoringUrlService;

  /**
   * Method for handle get request of parameters with using id.
   * @param id id of need parameters
   * @return ResponseEntity object with found object and Http Status
   */
  @ApiOperation(value = "Get a parameters monitoring url by id")
  @GetMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> getParameters(@PathVariable("id") long id) {
    ParametersMonitoringUrl parametersMonitoringUrl =
        parametersMonitoringUrlService.getParametersById(id);
    return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
  }

  /**
   * Method for handle post request of params with using url.
   * @param parametersMonitoringUrl parameters for monitoring url
   * @param ucBuilder object for constructing URI
   * @return esponseEntity object with header and Http Status
   */
  @ApiOperation(value = "Save a parameters monitoring url")
  @PostMapping("/parameters")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> saveParameters(@RequestBody @Valid
                                                ParametersMonitoringUrl parametersMonitoringUrl,
                                        UriComponentsBuilder ucBuilder) {
    ParametersMonitoringUrl savedParametersMonitoringUrl =
        parametersMonitoringUrlService.saveParametersUrl(parametersMonitoringUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("parameters/{id}")
        .buildAndExpand(savedParametersMonitoringUrl.getId()).toUri());
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

  /**
   * Method for handle put request of params with using url.
   * @param id id of the updated object
   * @param parametersMonitoringUrl parameters for monitoring url
   * @return ResponseEntity object with updated object and Http Status
   */
  @ApiOperation(value = "Update parameters monitoring of existing url")
  @PutMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> updateParameters(@PathVariable("id") long id,
                                            @RequestBody @Valid
                                                ParametersMonitoringUrl parametersMonitoringUrl) {
    parametersMonitoringUrlService.updateParametersUrl(id, parametersMonitoringUrl);
    return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
  }

  /**
   * Method for handle delete request of params with using url.
   * @param id id of the removable object
   * @return ResponseEntity object with Http Status
   */
  @ApiOperation(value = "Delete parameters monitoring url by id")
  @DeleteMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> deleteParameters(@PathVariable("id") long id) {
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
