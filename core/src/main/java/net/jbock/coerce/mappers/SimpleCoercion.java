package net.jbock.coerce.mappers;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

class SimpleCoercion extends CoercionFactory {

  private final Function<TypeMirror, CodeBlock> createMapper;

  private SimpleCoercion(Function<TypeMirror, CodeBlock> createMapper) {
    this.createMapper = createMapper;
  }

  static SimpleCoercion create(String createFromString) {
    return new SimpleCoercion(type -> CodeBlock.of("$T::" + createFromString, type));
  }

  static SimpleCoercion create(Function<TypeMirror, CodeBlock> createMapper) {
    return new SimpleCoercion(createMapper);
  }

  @Override
  final CodeBlock createMapper(TypeMirror innerType) {
    return createMapper.apply(innerType);
  }
}