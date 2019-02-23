package com.common;

import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.localserver.EchoHandler;
import org.apache.http.localserver.LocalServerTestBase;
import org.apache.http.localserver.RandomHandler;
import org.apache.http.localserver.SSLTestContexts;
import org.apache.http.protocol.HttpRequestHandler;

public class TestServer extends LocalServerTestBase {
  private int numberSecondsSocketTimeout;

  public TestServer() {}

  public TestServer(int numberSecondsSocketTimeout) {
    this.numberSecondsSocketTimeout = numberSecondsSocketTimeout;
  }

  @Override
  public void setUp() throws Exception {
    SocketConfig socketConfig = SocketConfig.custom()
        .setSoTimeout(numberSecondsSocketTimeout * 1000).build();
    this.serverBootstrap = ServerBootstrap.bootstrap().setSocketConfig(socketConfig)
        .setServerInfo("TEST/1.1").registerHandler("/echo/*", new EchoHandler())
        .registerHandler("/random/*", new RandomHandler());
    if(this.scheme.equals(LocalServerTestBase.ProtocolScheme.https)) {
      this.serverBootstrap.setSslContext(SSLTestContexts.createServerSSLContext());
    }

    this.connManager = new PoolingHttpClientConnectionManager();
    this.clientBuilder = HttpClientBuilder.create().setDefaultSocketConfig(socketConfig)
        .setConnectionManager(this.connManager);
  }

  @Override
  public void shutDown() throws Exception {
    if(this.httpclient != null) {
      this.httpclient.close();
    }

    if(this.server != null) {
      this.server.shutdown(1L, TimeUnit.SECONDS);
    }
  }

  public void setHandlerForRequest(String urlSuffix, HttpRequestHandler handler) {
    this.serverBootstrap.registerHandler(urlSuffix, handler);
  }

  public String getServerUrl() throws Exception {
    HttpHost target = this.start();
    return "http://localhost:" + target.getPort();
  }

}
