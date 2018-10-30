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
package com.janosgyerik.sonar.markdown.plugin.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import javax.annotation.CheckForNull;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;

public class TestUtils {

  private TestUtils() {
    // utility class, forbidden constructor
  }

  public static InputFile newInputFile(String content) {
    return new InputFile() {
      @Override
      public String relativePath() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String absolutePath() {
        throw new UnsupportedOperationException();
      }

      @Override
      public File file() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Path path() {
        throw new UnsupportedOperationException();
      }

      @CheckForNull
      @Override
      public String language() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Type type() {
        throw new UnsupportedOperationException();
      }

      @Override
      public InputStream inputStream() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String contents() {
        return content;
      }

      @Override
      public Status status() {
        throw new UnsupportedOperationException();
      }

      @Override
      public int lines() {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean isEmpty() {
        throw new UnsupportedOperationException();
      }

      @Override
      public TextPointer newPointer(int line, int lineOffset) {
        throw new UnsupportedOperationException();
      }

      @Override
      public TextRange newRange(TextPointer start, TextPointer end) {
        throw new UnsupportedOperationException();
      }

      @Override
      public TextRange newRange(int startLine, int startLineOffset, int endLine, int endLineOffset) {
        throw new UnsupportedOperationException();
      }

      @Override
      public TextRange selectLine(int line) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Charset charset() {
        throw new UnsupportedOperationException();
      }

      @Override
      public URI uri() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String filename() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String key() {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean isFile() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
