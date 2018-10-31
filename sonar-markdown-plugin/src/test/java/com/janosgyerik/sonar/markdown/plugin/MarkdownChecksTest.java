package com.janosgyerik.sonar.markdown.plugin;

import org.junit.jupiter.api.Test;
import org.sonar.check.Rule;

import static com.janosgyerik.sonar.markdown.plugin.Utils.getRule;
import static org.assertj.core.api.Assertions.assertThat;

class MarkdownChecksTest {
  @Test
  void verify_all_rules_define_essential_properties() {
    MarkdownChecks.all().forEach(check -> {
      Rule rule = getRule(check);
      assertThat(rule.key()).isNotBlank();
      assertThat(rule.name()).isNotBlank();
      assertThat(rule.description()).isNotBlank();
      assertThat(rule.tags()).isNotEmpty();
    });
  }
}
