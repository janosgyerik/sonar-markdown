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
package com.janosgyerik.sonar.markdown.plugin;

import com.janosgyerik.sonar.markdown.plugin.ruleengine.Check;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.rule.RuleKey;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownSensorTest {

  private Path workDir;
  private Path projectDir;
  private SensorContextTester sensorContext;

  @BeforeEach
  void setUp() throws IOException {
    workDir = Files.createTempDirectory("markdownTest");
    workDir.toFile().deleteOnExit();
    projectDir = Files.createTempDirectory("markdownTestProject");
    projectDir.toFile().deleteOnExit();
    sensorContext = SensorContextTester.create(workDir);
    sensorContext.fileSystem().setWorkDir(workDir);
  }

  @Test
  void test_description() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();

    getSensor("foo").describe(descriptor);
    assertThat(descriptor.name()).isEqualTo("SonarMarkdown");
    assertThat(descriptor.languages()).containsOnly("markdown");
  }

  @Test
  void test_line_issue() {
    InputFile inputFile = createInputFile("README.md", InputFile.Type.MAIN,
      "line with trailing whitespace ");
    sensorContext.fileSystem().add(inputFile);
    MarkdownSensor markdownSensor = getSensor("NoWhitespaceAtEOL");
    markdownSensor.execute(sensorContext);
    assertThat(sensorContext.allIssues()).hasSize(1);
  }

  @Test
  void test_failure_empty_file() {
    InputFile failingFile = createInputFile("README.md", InputFile.Type.MAIN, "");
    sensorContext.fileSystem().add(failingFile);
    MarkdownSensor markdownSensor = getSensor("foo");
    markdownSensor.execute(sensorContext);

    // TODO verify failure
  }

  @Test
  void test_workdir_failure() {
    MarkdownSensor markdownSensor = getSensor("foo");
    markdownSensor.execute(SensorContextTester.create(workDir));

    // TODO verify failure
  }

  private MarkdownSensor getSensor(String... activeRuleArray) {
    Set<String> activeRuleSet = new HashSet<>(Arrays.asList(activeRuleArray));
    List<Class<? extends Check>> ruleClasses = MarkdownChecks.all();
    List<String> allKeys = ruleClasses.stream().map(ruleClass -> ((org.sonar.check.Rule) ruleClass.getAnnotations()[0]).key()).collect(Collectors.toList());
    ActiveRulesBuilder rulesBuilder = new ActiveRulesBuilder();
    allKeys.forEach(key -> {
      NewActiveRule newActiveRule = rulesBuilder.create(RuleKey.of(MarkdownRulesDefinition.REPOSITORY_KEY, key));
      if (activeRuleSet.contains(key)) {
        newActiveRule.activate();
        if (key.equals("S1451")) {
          newActiveRule.setParam("headerFormat", "some header format");
        }
      }
    });
    ActiveRules activeRules = rulesBuilder.build();
    CheckFactory checkFactory = new CheckFactory(activeRules);
    Checks<Check> checks = checkFactory.create(MarkdownRulesDefinition.REPOSITORY_KEY);
    checks.addAnnotatedChecks((Iterable) ruleClasses);
    return new MarkdownSensor(checkFactory);
  }

  private InputFile createInputFile(String filename, InputFile.Type type, String content) {
    Path filePath = projectDir.resolve(filename);
    return TestInputFileBuilder.create("module", projectDir.toFile(), filePath.toFile())
      .setCharset(StandardCharsets.UTF_8)
      .setLanguage(MarkdownLanguage.KEY)
      .setContents(content)
      .setType(type)
      .build();
  }
}
