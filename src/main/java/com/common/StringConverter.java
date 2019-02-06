package com.common;

import com.common.entities.StatusUrl;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class StringConverter {
  private StringConverter() {}

  /**
   * Read entity as string.
   * @param entity entity of response
   * @param statusUrl information about defining status url
   * @param statusInfo information about cause of status
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
        statusInfo.setCauseStatus("Cannot get content with charset UTF-8");
      } else {
        statusInfo.setCauseStatus("Cannot get content with charset " + charset);
      }
      statusUrl.setStatus("CRITICAL");
    }
    return contentResponseString;
  }
}
