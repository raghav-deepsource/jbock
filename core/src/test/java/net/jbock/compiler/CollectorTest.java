package net.jbock.compiler;

import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.util.List;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Collections.singletonList;
import static net.jbock.compiler.ProcessorTest.withImports;

class CollectorTest {

  @Test
  void validCollector() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "  @Parameter(repeatable = true, collectedBy = MyCollector.class) abstract Set<String> strings();",
        "  static class MyCollector implements Supplier<Collector<String, ?, Set<String>>> {",
        "    public Collector<String, ?, Set<String>> get() {",
        "      return Collectors.toSet();",
        "    }",
        "  }",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }

  @Test
  void validBigIntegers() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "  @Parameter(repeatable = true, mappedBy = HexMapper.class, collectedBy = MyCollector.class)",
        "  abstract Set<BigInteger> bigIntegers();",
        "  static class MyCollector implements Supplier<Collector<BigInteger, ?, Set<BigInteger>>> {",
        "    public Collector<BigInteger, ?, Set<BigInteger>> get() {",
        "      return Collectors.toSet();",
        "    }",
        "  }",
        "  static class HexMapper implements Supplier<Function<String, BigInteger>> {",
        "    public Function<String, BigInteger> get() {",
        "      return s -> new BigInteger(s, 16);",
        "    }",
        "  }",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }
}
