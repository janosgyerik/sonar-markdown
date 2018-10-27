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
import org.sonar.api.config.internal.MapSettings;

import static com.janosgyerik.sonar.markdown.plugin.MarkdownLanguage.FILE_SUFFIXES_KEY;
import static org.assertj.core.api.Assertions.assertThat;

class MarkdownLanguageTest {

  @Test
  void should_have_correct_file_extensions() {
    MapSettings mapSettings = new MapSettings();
    MarkdownLanguage markdownLanguage = new MarkdownLanguage(mapSettings.asConfig());
    assertThat(markdownLanguage.getFileSuffixes()).containsExactly(".md");
  }

  @Test
  void can_override_file_extensions() {
    MapSettings mapSettings = new MapSettings();
    mapSettings.setProperty(FILE_SUFFIXES_KEY, ".foo,.bar");
    MarkdownLanguage markdownLanguage = new MarkdownLanguage(mapSettings.asConfig());
    assertThat(markdownLanguage.getFileSuffixes()).containsExactly(".foo",".bar");
  }
}
