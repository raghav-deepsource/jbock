package net.jbock.compiler;

import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.util.List;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Collections.singletonList;
import static net.jbock.compiler.ProcessorTest.withImports;

class PositionalTest {


  @Test
  void simpleOptional() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "",
        "  @PositionalParameter(optional = true)",
        "  abstract Optional<String> a();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }

  @Test
  void mustDeclareAsOptionalInt() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class InvalidArguments {",
        "  @PositionalParameter abstract OptionalInt a();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.InvalidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .failsToCompile()
        .withErrorContaining("Declare this parameter optional.");
  }

  @Test
  void positionalOptionalsAnyOrder() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "  @PositionalParameter(position = 1, optional = true) abstract Optional<String> a();",
        "  @PositionalParameter(position = 10, optional = true) abstract OptionalInt b();",
        "  @PositionalParameter(position = 100, optional = true) abstract Optional<String> c();",
        "  @PositionalParameter(position = 1000, optional = true) abstract OptionalInt d();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }

  @Test
  void positionalAutomaticPosition() {
    // position can be inferred from rank (optional after required)
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "  @PositionalParameter abstract String a();",
        "  @PositionalParameter(optional = true) abstract OptionalInt b();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }

  @Test
  void positionalConflict() {
    // two positionals with same rank (both required) -> position must be specified
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class InvalidArguments {",
        "  @PositionalParameter abstract int a();",
        "  @PositionalParameter abstract int b();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.InvalidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .failsToCompile()
        .withErrorContaining("Define a unique position.");
  }

  @Test
  void positionalNonzeroNoInfer() {
    // there is a non-zero position -> all positions must be explicit and unique
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class InvalidArguments {",
        "  @PositionalParameter abstract String a();",
        "  @PositionalParameter(optional = true) abstract OptionalInt b();",
        "  @PositionalParameter(position = 1, repeatable = true) abstract Optional<String> c();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.InvalidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .failsToCompile()
        .withErrorContaining("Define a unique position.");
  }


  @Test
  void positionalBadReturnTypeStringBuilder() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class InvalidArguments {",
        "  @PositionalParameter abstract StringBuilder a();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.InvalidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .failsToCompile()
        .withErrorContaining("Unknown parameter type. Define a custom mapper.");
  }


  @Test
  void validPositionalBooleanObject() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "  @PositionalParameter abstract Boolean a();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }


  @Test
  void validPositionalBooleanPrimitive() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class InvalidArguments {",
        "  @PositionalParameter abstract boolean a();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.InvalidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }

  @Test
  void positionalAllRanks() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "  @PositionalParameter(repeatable = true) abstract List<String> a();",
        "  @PositionalParameter abstract String b();",
        "  @PositionalParameter(optional = true) abstract Optional<String> c();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }

  @Test
  void twoPositionalLists() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class InvalidArguments {",
        "  @PositionalParameter(repeatable = true) abstract List<String> a();",
        "  @PositionalParameter(repeatable = true) abstract List<String> b();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.InvalidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .failsToCompile()
        .withErrorContaining("There can only be one one repeatable positional parameter.");
  }

  @Test
  void validList() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class ValidArguments {",
        "  @PositionalParameter(repeatable = true) abstract List<String> a();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.ValidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .compilesWithoutError();
  }


  @Test
  void mustDeclareAsOptional() {
    List<String> sourceLines = withImports(
        "@CommandLineArguments",
        "abstract class InvalidArguments {",
        "  @PositionalParameter abstract Optional<Integer> a();",
        "}");
    JavaFileObject javaFile = forSourceLines("test.InvalidArguments", sourceLines);
    assertAbout(javaSources()).that(singletonList(javaFile))
        .processedWith(new Processor())
        .failsToCompile()
        .withErrorContaining("Declare this parameter optional.");
  }
}