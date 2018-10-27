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

import org.sonar.api.batch.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

public class MarkdownRulesDefinition implements RulesDefinition {

  public static final String REPOSITORY_KEY = "markdown";
  
  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(REPOSITORY_KEY, MarkdownLanguage.KEY)
      .setName("SonarAnalyzer");

    MarkdownChecks.getChecks().forEach(check -> {
      org.sonar.check.Rule rule = (org.sonar.check.Rule) check.getAnnotation(org.sonar.check.Rule.class);

      repository.createRule(rule.key())
        .setName(rule.name())
        .setMarkdownDescription(rule.description())
        .setSeverity(Severity.INFO.toString())
        .setType(RuleType.CODE_SMELL)
        .addTags(rule.tags())
        .setActivatedByDefault(true);
    });

    repository.done();
  }
}