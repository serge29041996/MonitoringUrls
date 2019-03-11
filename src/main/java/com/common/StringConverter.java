/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common;

import com.common.entities.StatusUrl;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Class for converting response to String representation.
 */
public final class StringConverter {
  private static final Logger logger = Logger.getLogger(StringConverter.class);

  private StringConverter() {}

  /**
   * Read entity as string.
   * It begins from defining of charset of response.
   * If charset is unknown set to UTF-8 format
   * Next if we cannot read information with charset we save information about error
   * and set status for url, from where response is obtained, to critical.
   * @param entity entity of http response
   * @param statusUrl information about url, parameters for defining status, status and time of
   *                  defining status
   * @param statusInfo information about id of url, status for defining status and cause of all
   *                   statuses, expect status OK
   * @return string representation of the entity
   */
  public static String entityToString(HttpEntity entity, StatusUrl statusUrl,
                                      StatusInfo statusInfo) {
    String contentResponseString = null;
    ContentType contentType = ContentType.get(entity);
    Charset charset = null;
    if (contentType != null) {
      charset = contentType.getCharset();
    }
    try {
      if (charset == null) {
        contentResponseString = EntityUtils.toString(entity, "UTF-8");
      } else {
        contentResponseString = EntityUtils.toString(entity, charset);
      }
    } catch (IOException e) {
      if (charset == null) {
        logger.error("Cannot get content with charset UTF-8");
        statusInfo.setCauseStatus("Cannot get content with charset UTF-8");
      } else {
        logger.error("Cannot get content with charset " + charset);
        statusInfo.setCauseStatus("Cannot get content with charset " + charset);
      }
      statusUrl.setStatus("CRITICAL");
    }
    return contentResponseString;
  }
}
