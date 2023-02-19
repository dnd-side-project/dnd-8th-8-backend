package com.dnd.weddingmap.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
  private List<String> acceptedValues;

  @Override
  public void initialize(ValidEnum annotation) {
    acceptedValues = Stream.of(annotation.value().getEnumConstants())
        .map(Enum::name)
        .toList();
  }

  @Override
  public boolean isValid(Enum value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return acceptedValues.contains(value.toString());
  }
}