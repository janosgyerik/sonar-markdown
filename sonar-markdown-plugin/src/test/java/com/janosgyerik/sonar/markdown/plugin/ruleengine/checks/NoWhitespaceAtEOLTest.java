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

import com.janosgyerik.sonar.markdown.plugin.Location;
import com.janosgyerik.sonar.markdown.plugin.ruleengine.EngineContext;
import com.janosgyerik.sonar.markdown.plugin.ruleengine.Issue;
import com.janosgyerik.sonar.markdown.plugin.utils.TestUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.janosgyerik.sonar.markdown.plugin.ruleengine.checks.NoWhitespaceAtEOL.API_RULE_KEY;
import static com.janosgyerik.sonar.markdown.plugin.ruleengine.checks.NoWhitespaceAtEOL.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class NoWhitespaceAtEOLTest {

  private final NoWhitespaceAtEOL underTest = new NoWhitespaceAtEOL();

  private final EngineContext engineContext = new EngineContext(Collections.emptyList());

  @BeforeEach
  void before() {
    underTest.initialize(engineContext);
  }

  @ParameterizedTest
  @MethodSource("contentsWithoutIssue")
  void find_no_issue_when_no_trailing_whitespace(String content) throws IOException {
    underTest.enterFile(TestUtils.newInputFile("foo"));
    assertThat(engineContext.getIssues()).isEmpty();
  }

  private static Stream<Arguments> contentsWithoutIssue() {
    return Stream.of(
      Arguments.of(""),
      Arguments.of("foo"),
      Arguments.of("foo bar"),
      Arguments.of("  \t \t foo bar"),
      Arguments.of("foo\nbar\nbaz")
    );
  }

  @ParameterizedTest
  @MethodSource("contentsWithExpectedLocations")
  void find_issue_with_correct_location(String content, int line, int endLine, int column, int endColumn) throws IOException {
    underTest.enterFile(TestUtils.newInputFile(content));

    Location location = Location.newBuilder()
      .line(line)
      .column(column)
      .endLine(endLine)
      .endColumn(endColumn)
      .build();

    assertThat(engineContext.getIssues())
      .extracting(Issue::ruleKey, Issue::message, Issue::location)
      .containsExactly(tuple(API_RULE_KEY, NAME, location));
  }

  private static Stream<Arguments> contentsWithExpectedLocations() {
    return Stream.of(
      Arguments.of("foo ", 1, 1, 3, 4),
      Arguments.of("foo   ", 1, 1, 3, 6),
      Arguments.of("foo\t", 1, 1, 3, 4),
      Arguments.of("foo\t\t\t", 1, 1, 3, 6),
      Arguments.of("foo \t \t", 1, 1, 3, 7),
      Arguments.of("foo\t \t ", 1, 1, 3, 7)
    );
  }

}
