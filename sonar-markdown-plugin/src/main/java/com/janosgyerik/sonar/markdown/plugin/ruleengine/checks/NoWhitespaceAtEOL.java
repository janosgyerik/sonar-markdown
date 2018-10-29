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
package com.janosgyerik.sonar.markdown.plugin.ruleengine.checks;

import com.janosgyerik.sonar.markdown.plugin.MarkdownRulesDefinition;
import com.janosgyerik.sonar.markdown.plugin.ruleengine.Check;
import com.janosgyerik.sonar.markdown.plugin.ruleengine.EngineContext;
import com.janosgyerik.sonar.markdown.plugin.ruleengine.Issue;
import java.io.IOException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(
  key = NoWhitespaceAtEOL.RULE_KEY,
  name = NoWhitespaceAtEOL.NAME,
  description = NoWhitespaceAtEOL.DESCRIPTION,
  tags = {"sloppy", "portability"}
)
public class NoWhitespaceAtEOL implements Check {

  static final String RULE_KEY = "NoWhitespaceAtEOL";
  static final RuleKey API_RULE_KEY = RuleKey.of(MarkdownRulesDefinition.REPOSITORY_KEY, RULE_KEY);

  static final String NAME = "There should be no trailing whitespace at the end of lines";
  static final String DESCRIPTION = "Whitespace at the end of lines is practically invisible,\n" +
    "but in some implementations it may have a meaning,\n" +
    "leading to differently rendered output.\n" +
    "To avoid confusion, and to get consistent results, it's best to avoid it.";

  private EngineContext engineContext;

  @Override
  public void initialize(EngineContext engineContext) {
    this.engineContext = engineContext;
  }

  @Override
  public void enterFile(InputFile inputFile) throws IOException {
    int lineNum = 0;
    for (String line : inputFile.contents().split("\n")) {
      lineNum++;
      if (line.endsWith(" ") || line.endsWith("\t")) {
        Issue issue = Issue.newBuilder()
          .ruleKey(API_RULE_KEY)
          .message(NAME)
          .line(lineNum)
          .column(line.replaceAll("\\s+$", "").length())
          .endColumn(line.length())
          .build();
        engineContext.reportIssue(issue);
      }
    }
  }
}
