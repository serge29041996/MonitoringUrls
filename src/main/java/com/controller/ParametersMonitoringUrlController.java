package com.controller;

import com.common.StatusInfo;
import com.common.entities.ParametersMonitoringUrl;
import com.service.IParametersMonitoringUrlService;
import com.service.IValidationStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
@Api(value = "parametersMonitoringUrls")
public class ParametersMonitoringUrlController {

  @Autowired
  private IParametersMonitoringUrlService parametersMonitoringUrlService;

  @Autowired
  private IValidationStatusService validationStatusService;

  /**
   * Method for handle get request of parameters with using id.
   * @param id id of need parameters
   * @return responseEntity object with found object and Http Status
   */
  @ApiOperation(value = "Get a parameters monitoring url by id")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Succesfull get parameters monitoring for url"),
      @ApiResponse(code = 404, message = "Parameters monitoring for url have not found")
      })
  @GetMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<Object> getParameters(@PathVariable("id") long id) {
    ParametersMonitoringUrl parametersMonitoringUrl =
        parametersMonitoringUrlService.getParametersById(id);
    return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
  }

  /**
   * Method for handle post request of params with using url.
   * @param parametersMonitoringUrl parameters for monitoring url
   * @param ucBuilder object for constructing URI
   * @return responseEntity object with header and Http Status
   */
  @ApiOperation(value = "Save a parameters monitoring url")
  @ApiResponses({
      @ApiResponse(code = 201, message = "Parameters monitoring for url have saved"),
      @ApiResponse(code = 400, message = "Validation errors for parameters monitoring"),
      @ApiResponse(code = 409, message = "Parameters monitoring for url have already exist")
      })
  @PostMapping("/parameters")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<Object> saveParameters(@RequestBody @Valid
                                                ParametersMonitoringUrl parametersMonitoringUrl,
                                        UriComponentsBuilder ucBuilder) {
    ParametersMonitoringUrl savedParametersMonitoringUrl =
        parametersMonitoringUrlService.saveParametersUrl(parametersMonitoringUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("parameters/{id}")
        .buildAndExpand(savedParametersMonitoringUrl.getId()).toUri());
    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  /**
   * Method for handle put request of params with using url.
   * @param id id of the updated object
   * @param parametersMonitoringUrl parameters for monitoring url
   * @return responseEntity object with updated object and Http Status
   */
  @ApiOperation(value = "Update parameters monitoring of existing url")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Parameters monitoring for url have updated"),
      @ApiResponse(code = 400, message = "Validation errors for parameters monitoring"),
      @ApiResponse(code = 404, message = "Parameters monitoring for url have not found"),
      @ApiResponse(code = 409, message = "Parameters monitoring for url have already exist")
      })
  @PutMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<Object> updateParameters(@PathVariable("id") long id,
                                            @RequestBody @Valid
                                                ParametersMonitoringUrl parametersMonitoringUrl) {
    parametersMonitoringUrlService.updateParametersUrl(id, parametersMonitoringUrl);
    return new ResponseEntity<>(parametersMonitoringUrl, HttpStatus.OK);
  }

  /**
   * Method for handle delete request of params with using url.
   * @param id id of the removable object
   * @return responseEntity object with Http Status
   */
  @ApiOperation(value = "Delete parameters monitoring url by id")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Succesfull delete parameters monitoring for url"),
      @ApiResponse(code = 404, message = "Parameters monitoring for url have not found")
      })
  @DeleteMapping("/parameters/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<Object> deleteParameters(@PathVariable("id") long id) {
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Method for handle get request for checking status of url with parameters using id.
   * @param id id of need parameters for url
   * @return responseEntity object with status of url and cause of critical status and Http Status
   */
  @ApiOperation(value = "Get a parameters monitoring url by id")
  @ApiResponse(code = 200, message = "Status for url have checked")
  @GetMapping("/status/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<Object> getStatusForParameters(@PathVariable("id") long id) {
    StatusInfo statusInfo = validationStatusService.checkResponse(id);
    return new ResponseEntity<>(statusInfo, HttpStatus.OK);
  }
}
