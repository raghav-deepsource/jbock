package net.jbock.coerce.mapper;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public abstract class MapperType {

  private final boolean optional;
  private final boolean supplier; // wrapped in Supplier?
  private final List<TypeMirror> solution; // solved typevars of mapperClass

  MapperType(
      boolean supplier,
      List<TypeMirror> solution,
      boolean optional) {
    this.optional = optional;
    this.supplier = supplier;
    this.solution = solution;
  }

  public static ReferenceMapperType create(
      boolean supplier,
      boolean optional,
      TypeElement mapperClass,
      List<TypeMirror> solution,
      TypeMirror innerType) {
    return new ReferenceMapperType(mapperClass, supplier, solution, optional, innerType);
  }

  public static AutoMapperType create(TypeMirror innerType, CodeBlock createExpression, boolean optional) {
    return new AutoMapperType(innerType, createExpression, optional);
  }

  public boolean isOptional() {
    return optional;
  }

  public abstract TypeMirror mapperType();

  public boolean supplier() {
    return supplier;
  }

  public List<TypeMirror> solution() {
    return solution;
  }

}