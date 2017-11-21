package net.zerobuilder.examples.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RequiredManTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void success() {
    RequiredMan requiredMan = RequiredMan_Parser.parse(new String[]{"--dir", "A"});
    assertThat(requiredMan.dir()).isEqualTo("A");
  }

  @Test
  public void errorDirMissing() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Missing required option: DIR");
    RequiredMan_Parser.parse(new String[]{});
  }

  @Test
  public void errorDetachedDetached() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Found token: --dir, but option DIR is not repeatable");
    RequiredMan_Parser.parse(new String[]{"--dir", "A", "--dir", "B"});
  }

  @Test
  public void errorAttachedDetached() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Found token: --dir, but option DIR is not repeatable");
    RequiredMan_Parser.parse(new String[]{"--dir=A", "--dir", "B"});
  }

  @Test
  public void errorAttachedAttached() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Found token: --dir=B, but option DIR is not repeatable");
    RequiredMan_Parser.parse(new String[]{"--dir=A", "--dir=B"});
  }

  @Test
  public void errorDetachedAttached() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Found token: --dir=B, but option DIR is not repeatable");
    RequiredMan_Parser.parse(new String[]{"--dir", "A", "--dir=B"});
  }
}
