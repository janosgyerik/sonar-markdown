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

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

public class MarkdownLanguage extends AbstractLanguage {

  public static final String KEY = "markdown";
  public static final String FILE_SUFFIXES_KEY = "sonar.markdown.file.suffixes";
  public static final String FILE_SUFFIXES_DEFAULT_VALUE = ".md";

  private final Configuration configuration;

  public MarkdownLanguage(Configuration configuration) {
    super(KEY, "Markdown");
    this.configuration = configuration;
  }

  @Override
  public String[] getFileSuffixes() {
    String[] suffixes = configuration.getStringArray(FILE_SUFFIXES_KEY);
    if (suffixes == null || suffixes.length == 0) {
      suffixes = new String[] {FILE_SUFFIXES_DEFAULT_VALUE};
    }
    return suffixes;
  }
}
