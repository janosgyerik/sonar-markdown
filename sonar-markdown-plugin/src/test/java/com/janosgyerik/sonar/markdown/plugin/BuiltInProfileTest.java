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

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import static org.assertj.core.api.Assertions.assertThat;

class BuiltInProfileTest {

  private final BuiltInQualityProfilesDefinition underTest = new BuiltInProfile();

  @Test
  void builtin_profile_has_same_number_of_rules_as_number_of_checks() {
    BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();
    underTest.define(context);

    Map<String, Map<String, BuiltInQualityProfilesDefinition.BuiltInQualityProfile>> profilesByLanguageAndName = context.profilesByLanguageAndName();
    assertThat(profilesByLanguageAndName).hasSize(1);

    Map<String, BuiltInQualityProfilesDefinition.BuiltInQualityProfile> firstBuiltInQualityProfile = profilesByLanguageAndName.values()
      .iterator()
      .next();
    assertThat(firstBuiltInQualityProfile).hasSize(MarkdownChecks.getChecks().size());
  }
}