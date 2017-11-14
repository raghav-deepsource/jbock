package net.zerobuilder.examples.gradle;

import java.util.List;
import net.jbock.CommandLineArguments;
import net.jbock.Description;
import net.jbock.EverythingAfter;
import net.jbock.OtherTokens;
import net.jbock.ShortName;

@CommandLineArguments
abstract class Rm {

  @ShortName('r')
  abstract boolean recursive();

  @ShortName('f')
  abstract boolean force();

  @OtherTokens
  abstract List<String> otherTokens();

  @EverythingAfter("--")
  @Description({
      "@EverythingAfter to create a last resort",
      "for problematic @OtherTokens.",
      "For example, when the file name is '-f'"})
  abstract List<String> filesToDelete();
}