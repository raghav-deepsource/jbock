package net.jbock.coerce;

import net.jbock.com.squareup.javapoet.CodeBlock;
import net.jbock.com.squareup.javapoet.TypeName;
import net.jbock.compiler.Constants;

class OptionalIntCoercion extends BasicIntegerCoercion {

  @Override
  public TypeName trigger() {
    return Constants.OPTIONAL_INT;
  }

  @Override
  public boolean special() {
    return true;
  }

  @Override
  public CodeBlock jsonExpr(String param) {
    return CodeBlock.builder()
        .add("($L.isPresent() ? $L.getAsInt() : $S)",
            param,
            param,
            "null")
        .build();
  }
}
