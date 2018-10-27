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

import org.junit.jupiter.api.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownRulesDefinitionTest {

  @Test
  void test() {
    MarkdownRulesDefinition rulesDefinition = new MarkdownRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);

    assertThat(context.repositories()).hasSize(1);

    RulesDefinition.Repository markdownRepository = context.repository("markdown");

    assertThat(markdownRepository.name()).isEqualTo("SonarAnalyzer");
    assertThat(markdownRepository.language()).isEqualTo("markdown");
    assertThat(markdownRepository.rules()).hasSize(MarkdownChecks.getChecks().size());

    RulesDefinition.Rule rule = markdownRepository.rule("NoWhitespaceAtEOL");
    assertThat(rule).isNotNull();
    assertThat(rule.name()).isEqualTo("Do not leave whitespace at end of lines");
    assertThat(rule.type()).isEqualTo(RuleType.CODE_SMELL);

    assertAllRuleParametersHaveDescription(markdownRepository);
  }

  private void assertAllRuleParametersHaveDescription(RulesDefinition.Repository repository) {
    for (RulesDefinition.Rule rule : repository.rules()) {
      for (RulesDefinition.Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }
}
