package net.jbock.examples;

import net.jbock.examples.fixture.ParserFixture;
import org.junit.Test;

public class GradleArgumentsFooTest {

  private final ParserFixture<GradleArguments.Foo> f =
      ParserFixture.create(GradleArguments_Foo_Parser::parse);

  @Test
  public void testParserForNestedClass() {
    f.assertThat("--bar=4").isParsedAs("bar", 4);
  }
}