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

import javax.annotation.Nullable;

public class Location {
  final int line;
  final int endLine;
  @Nullable final Integer column;
  @Nullable final Integer endColumn;
  private final boolean hasRange;

  private Location(Builder builder) {
    this.line = builder.line;
    this.endLine = builder.endLine;
    this.column = builder.column;
    this.endColumn = builder.endColumn;
    this.hasRange = builder.column != null;
  }

  public boolean hasRange() {
    return hasRange;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    @Nullable private Integer line;
    @Nullable private Integer column;
    @Nullable private Integer endLine;
    @Nullable private Integer endColumn;

    public Builder line(int line) {
      this.line = line;
      return this;
    }

    public Builder column(int column) {
      this.column = column;
      return this;
    }

    public Builder endLine(int endLine) {
      this.endLine = endLine;
      return this;
    }

    public Builder endColumn(int endColumn) {
      this.endColumn = endColumn;
      return this;
    }

    private void validate() {
      if (!isValid()) {
        throw new InvalidLocationParametersError();
      }

      if (endLine == null) {
        endLine = line;
      }
    }

    private boolean isValid() {
      if (line == null || line <= 0) {
        return false;
      }

      if (endLine != null && endLine < line) {
        return false;
      }

      if (column != null && endColumn != null) {
        return 0 <= column && column < endColumn;
      }

      return column == null && endColumn == null;
    }

    public Location build() {
      validate();
      return new Location(this);
    }

    static class InvalidLocationParametersError extends RuntimeException {

    }
  }
}
