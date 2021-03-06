package net.jbock.coerce;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import net.jbock.compiler.ParamName;
import net.jbock.compiler.TypeTool;
import net.jbock.compiler.ValidationException;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

/**
 * Coercion input: Information about a single parameter (option or param).
 */
public class BasicInfo {

  private final ParamName paramName;

  private final ExecutableElement sourceMethod;

  private final TypeTool tool;

  private final ClassName optionType;

  // nullable
  private final TypeElement mapperClass;

  BasicInfo(Optional<TypeElement> mapperClass, ParamName paramName, ClassName optionType,
            ExecutableElement sourceMethod,
            TypeTool tool) {
    this.mapperClass = mapperClass.orElse(null);
    this.paramName = paramName;
    this.optionType = optionType;
    this.sourceMethod = sourceMethod;
    this.tool = tool;
  }

  public Optional<CodeBlock> findAutoMapper(TypeMirror testType) {
    Optional<CodeBlock> mapExpr = AutoMapper.findAutoMapper(tool(), testType);
    if (mapExpr.isPresent()) {
      return mapExpr;
    }
    if (tool.isEnumType(testType)) {
      return Optional.of(CodeBlock.of("$T::valueOf", testType));
    }
    return Optional.empty();
  }

  public ParamName parameterName() {
    return paramName;
  }

  public ParameterSpec constructorParam(TypeMirror type) {
    return ParameterSpec.builder(TypeName.get(type), paramName.camel()).build();

  }

  public TypeMirror returnType() {
    return sourceMethod.getReturnType();
  }

  public ValidationException failure(String message) {
    return ValidationException.create(sourceMethod, message);
  }

  public TypeTool tool() {
    return tool;
  }

  Optional<TypeElement> mapperClass() {
    return Optional.ofNullable(mapperClass);
  }

  public ClassName optionType() {
    return optionType;
  }
}
