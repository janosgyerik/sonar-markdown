/*
 * MIT License
 *
 * Copyright (c) 2018 Janos Gyerik
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.sonar.it.markdown;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.OrchestratorBuilder;
import com.sonar.orchestrator.locator.FileLocation;
import com.sonar.orchestrator.locator.Location;
import com.sonar.orchestrator.locator.MavenLocation;
import java.io.File;
import java.util.List;
import javax.annotation.CheckForNull;
import org.apache.commons.lang.StringUtils;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.sonarqube.ws.WsMeasures;
import org.sonarqube.ws.WsMeasures.Measure;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measure.ComponentWsRequest;

import static java.util.Collections.singletonList;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  MarkdownIntegrationTest.class,
  MarkdownTest.class
})
public class Tests {

  private static final String PROJECT_ROOT_DIR = "../projects";
  private static final String RESOURCE_DIRECTORY = "/com/sonar/it/markdown";

  @ClassRule
  public static final Orchestrator ORCHESTRATOR;

  static {
    String defaultRuntimeVersion = "true".equals(System.getenv("SONARSOURCE_QA")) ? null : "7.2-RC1";
    OrchestratorBuilder builder = Orchestrator.builderEnv()
      .setSonarVersion(System.getProperty("sonar.runtimeVersion", defaultRuntimeVersion))
      .restoreProfileAtStartup(FileLocation.ofClasspath(RESOURCE_DIRECTORY + "/empty-profile.xml"));

    String pluginVersion = System.getProperty("markdownVersion");
    Location pluginLocation;
    if (StringUtils.isEmpty(pluginVersion) || pluginVersion.endsWith("-SNAPSHOT")) {
      pluginLocation = FileLocation.byWildcardMavenFilename(new File("../../../sonar-markdown-plugin/build/libs"), "sonar-markdown-plugin-*-all.jar");
    } else {
      pluginLocation = MavenLocation.of("org.sonarsource.markdown", "sonar-markdown-plugin", pluginVersion);
    }
    builder.addPlugin(pluginLocation);

    ORCHESTRATOR = builder.build();
  }

  public static File projectDirectoryFor(String projectDirName) {
    return new File(Tests.PROJECT_ROOT_DIR, projectDirName);
  }

  @CheckForNull
  static Measure getMeasure(String componentKey, String metricKey) {
    WsMeasures.ComponentWsResponse response = newWsClient().measures().component(new ComponentWsRequest()
      .setComponent(componentKey)
      .setMetricKeys(singletonList(metricKey)));
    List<Measure> measures = response.getComponent().getMeasuresList();
    return measures.size() == 1 ? measures.get(0) : null;
  }

  @CheckForNull
  static Integer getMeasureAsInt(String componentKey, String metricKey) {
    Measure measure = getMeasure(componentKey, metricKey);
    return (measure == null) ? null : Integer.parseInt(measure.getValue());
  }

  static WsClient newWsClient() {
    return WsClientFactories.getDefault().newClient(HttpConnector.newBuilder()
      .url(ORCHESTRATOR.getServer().getUrl())
      .build());
  }
}
