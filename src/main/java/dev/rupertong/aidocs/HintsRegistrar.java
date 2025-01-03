package dev.rupertong.aidocs;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;

// Register hints for resources for GraalVM native image compilation
@Configuration
public class HintsRegistrar implements RuntimeHintsRegistrar {
  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    hints.resources().registerPattern("*.pdf");
    hints.resources().registerPattern("*.st");
  }
}
