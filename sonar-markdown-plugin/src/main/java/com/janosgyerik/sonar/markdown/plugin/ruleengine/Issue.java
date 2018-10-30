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
package com.janosgyerik.sonar.markdown.plugin.ruleengine;

import com.janosgyerik.sonar.markdown.plugin.Location;
import org.sonar.check.Rule;

import static com.janosgyerik.sonar.markdown.plugin.Utils.getRule;

public class Issue {
  private final String ruleKey;
  private final String message;
  private final Location location;

  private Issue(Builder builder) {
    Rule rule = getRule(builder.check.getClass());
    this.ruleKey = rule.key();
    this.message = rule.name();
    this.location = builder.location;
  }

  public String ruleKey() {
    return ruleKey;
  }

  public String message() {
    return message;
  }

  public Location location() {
    return location;
  }

  public boolean hasRange() {
    return location.hasRange();
  }

  public static Builder newBuilder(Check check) {
    return new Builder(check);
  }

  public static class Builder {
    private final Check check;
    private Location location;

    private Location.Builder locationBuilder = Location.newBuilder();

    private Builder(Check check) {
      this.check = check;
    }

    public Issue build() {
      validate();
      return new Issue(this);
    }

    private void validate() {
      location = locationBuilder.build();
    }

    public Builder line(int line) {
      locationBuilder.line(line);
      return this;
    }

    public Builder column(int column) {
      locationBuilder.column(column);
      return this;
    }

    public Builder endColumn(int endColumn) {
      locationBuilder.endColumn(endColumn);
      return this;
    }
  }
}
