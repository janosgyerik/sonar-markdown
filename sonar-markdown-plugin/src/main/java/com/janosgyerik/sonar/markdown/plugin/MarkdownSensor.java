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
import com.janosgyerik.sonar.markdown.plugin.ruleengine.Engine;
import com.janosgyerik.sonar.markdown.plugin.ruleengine.Issue;
import java.io.InputStream;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import static com.janosgyerik.sonar.markdown.plugin.PluginApiUtils.newRange;

public class MarkdownSensor implements Sensor {

  private static final Logger LOG = Loggers.get(MarkdownSensor.class);

  private final Checks<Check> checks;

  public MarkdownSensor(CheckFactory checkFactory) {
    checks = checkFactory.<Check>create(MarkdownRulesDefinition.REPOSITORY_KEY).addAnnotatedChecks(
      (Iterable) MarkdownChecks.getChecks());
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.onlyOnLanguage(MarkdownLanguage.KEY)
      .name("SonarMarkdown");
  }

  @Override
  public void execute(SensorContext context) {
    Engine ruleEngine = new Engine(checks.all());

    for (InputFile inputFile : getInputFiles(context)) {
      try (InputStream inputStream = inputFile.inputStream()) {
        Engine.ScanResult scanResult = ruleEngine.scan(inputFile);
        scanResult.issues.forEach(issue -> reportIssue(issue, context, inputFile));
      } catch (Exception e) {
        LOG.error("Error analyzing file " + inputFile.toString(), e);
      }
    }
  }

  private void reportIssue(Issue issue, SensorContext context, InputFile inputFile) {
    NewIssue newIssue = context.newIssue();
    NewIssueLocation location = newIssue.newLocation()
      .on(inputFile)
      .message(issue.message());
    if (issue.hasRange()) {
      location.at(newRange(inputFile, issue.location()));
    } else {
      location.at(inputFile.selectLine(issue.location().line));
    }
    newIssue.forRule(RuleKey.of(MarkdownRulesDefinition.REPOSITORY_KEY, issue.ruleKey()))
      .at(location);

    newIssue.save();
  }

  private static Iterable<InputFile> getInputFiles(SensorContext context) {
    FileSystem fs = context.fileSystem();
    return fs.inputFiles(fs.predicates()
      .hasLanguage(MarkdownLanguage.KEY));
  }
}
