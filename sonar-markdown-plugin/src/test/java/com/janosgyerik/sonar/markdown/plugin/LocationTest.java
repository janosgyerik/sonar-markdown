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

import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocationTest {
  @ParameterizedTest
  @MethodSource("buildersWithInvalidLocationParameters")
  void fail_to_build_with_invalid_parameters(Location.Builder builder) {
    assertThrows(Location.Builder.InvalidLocationParametersError.class, builder::build);
  }

  private static Stream<Arguments> buildersWithInvalidLocationParameters() {
    return Stream.of(
      Arguments.of(Location.newBuilder()),
      Arguments.of(Location.newBuilder().line(0)),
      Arguments.of(Location.newBuilder().line(-1)),
      Arguments.of(Location.newBuilder().endLine(1)),
      Arguments.of(Location.newBuilder().column(1)),
      Arguments.of(Location.newBuilder().endColumn(1)),
      Arguments.of(Location.newBuilder().line(2).endLine(1)),
      Arguments.of(Location.newBuilder().line(1).endLine(2).column(4).endColumn(3)),
      Arguments.of(Location.newBuilder().line(-1).endLine(2).column(3).endColumn(4)),
      Arguments.of(Location.newBuilder().line(1).endLine(-2).column(3).endColumn(4)),
      Arguments.of(Location.newBuilder().line(1).endLine(2).column(-3).endColumn(4)),
      Arguments.of(Location.newBuilder().line(1).endLine(2).column(3).endColumn(-4)),
      Arguments.of(Location.newBuilder().line(1).endLine(2).column(3)),
      Arguments.of(Location.newBuilder().line(1).endLine(2).endColumn(4))
    );
  }

  @ParameterizedTest
  @MethodSource("buildersWithValidLocationParameters")
  void build_with_valid_parameters(Location.Builder builder, int line, int endLine, @Nullable Integer column, @Nullable Integer endColumn) {
    Location location = builder.build();
    assertThat(location.line).isEqualTo(line);
    assertThat(location.endLine).isEqualTo(endLine);
    assertThat(location.column).isEqualTo(column);
    assertThat(location.endColumn).isEqualTo(endColumn);
  }

  private static Stream<Arguments> buildersWithValidLocationParameters() {
    return Stream.of(
      Arguments.of(Location.newBuilder().line(1).endLine(2).column(3).endColumn(4), 1, 2, 3, 4),
      Arguments.of(Location.newBuilder().line(1).endLine(2), 1, 2, null, null),
      Arguments.of(Location.newBuilder().line(1), 1, 1, null, null)
    );
  }
}
