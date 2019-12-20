package net.jbock.compiler.view;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import net.jbock.compiler.Context;

import java.util.stream.Stream;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static net.jbock.compiler.Constants.STREAM_OF_STRING;
import static net.jbock.compiler.Constants.STRING;

/**
 * Generates the RegularPositionalOptionParser class.
 */
final class RegularPositionalOptionParser {

  static TypeSpec define(Context context) {
    FieldSpec value = FieldSpec.builder(STRING, "value").build();
    return TypeSpec.classBuilder(context.regularParamParserType())
        .superclass(context.paramParserType())
        .addMethod(readMethod(value))
        .addMethod(MethodSpec.methodBuilder("values")
            .returns(STREAM_OF_STRING)
            .addStatement("return $N == null ? $T.empty() : $T.of($N)",
                value, Stream.class, Stream.class, value)
            .addAnnotation(Override.class)
            .build())
        .addField(value)
        .addModifiers(PRIVATE, STATIC).build();
  }

  private static MethodSpec readMethod(FieldSpec value) {
    ParameterSpec valueParam = ParameterSpec.builder(STRING, "value").build();
    return MethodSpec.methodBuilder("read")
        .addParameter(valueParam)
        .returns(TypeName.INT)
        .addStatement("this.$N = $N", value, valueParam)
        .addStatement("return $L", 1)
        .addAnnotation(Override.class)
        .build();
  }
}
