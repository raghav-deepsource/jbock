package net.jbock.coerce.mappers;

import net.jbock.com.squareup.javapoet.CodeBlock;

abstract class BasicFloatCoercion extends BasicNumberCoercion {

  BasicFloatCoercion(Class<?> trigger) {
    super(trigger);
  }

  @Override
  final CodeBlock map() {
    return CodeBlock.builder().add(".map($T::valueOf)", Float.class).build();
  }
}