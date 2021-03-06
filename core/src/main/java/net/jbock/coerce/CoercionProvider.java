package net.jbock.coerce;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import net.jbock.coerce.matching.AutoMatcher;
import net.jbock.coerce.matching.MapperMatcher;
import net.jbock.compiler.ParamName;
import net.jbock.compiler.TypeTool;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

import static net.jbock.coerce.NonFlagSkew.REPEATABLE;

public class CoercionProvider {

  public static Coercion nonFlagCoercion(ExecutableElement sourceMethod, ParamName paramName, Optional<TypeElement> mapperClass,
                                         Optional<TypeElement> collectorClass, ClassName optionType, TypeTool tool) {
    return findCoercion(new BasicInfo(mapperClass, paramName, optionType, sourceMethod, tool), collectorClass);
  }

  private static Coercion findCoercion(BasicInfo basicInfo, Optional<TypeElement> collector) {
    return collector.<Coercion>map(collectorClass -> {
      CollectorInfo collectorInfo = new CollectorClassValidator(basicInfo::failure,
          basicInfo.tool(), collectorClass, basicInfo.returnType()).getCollectorInfo();
      ParameterSpec constructorParam = basicInfo.constructorParam(basicInfo.returnType());
      TypeMirror inputType = collectorInfo.inputType();
      CodeBlock mapExpr = basicInfo.mapperClass()
          .map(mapperClass -> collectorPresentExplicit(basicInfo, inputType, mapperClass))
          .orElseGet(() -> collectorPresentAuto(basicInfo, inputType));
      return new NonFlagCoercion(basicInfo, mapExpr, collectorInfo.collectExpr(),
          CodeBlock.of("$N", constructorParam), REPEATABLE, constructorParam);
    }).orElseGet(() -> {
      if (basicInfo.mapperClass().isPresent()) {
        return new MapperMatcher(basicInfo, basicInfo.mapperClass().get()).findCoercion();
      } else {
        return new AutoMatcher(basicInfo).findCoercion();
      }
    });
  }

  private static CodeBlock collectorPresentAuto(BasicInfo basicInfo, TypeMirror inputType) {
    return basicInfo.findAutoMapper(inputType)
        .orElseThrow(() -> basicInfo.failure(String.format("Unknown parameter type: %s. Try defining a custom mapper.",
            inputType)));
  }

  private static CodeBlock collectorPresentExplicit(BasicInfo basicInfo, TypeMirror inputType, TypeElement mapperClass) {
    return new MapperClassValidator(basicInfo::failure, basicInfo.tool(), inputType, mapperClass).getMapExpr()
        .orElseThrow(basicInfo::failure);
  }
}
