package de.holisticon.example.annotationprocessortoolkit.usecase;

import de.holisticon.example.annotationprocessortoolkit.annotations.MethodWithOneStringParameterAndVoidReturnTypeAnnotation;
import de.holisticon.example.annotationprocessortoolkit.annotations.SomeInterface;
import de.holisticon.example.annotationprocessortoolkit.annotations.TypeThatIsAssignableToInterfaceAnnotation;

/**
 * Test class to show that the annotation processors are working correctly.
 */


@TypeThatIsAssignableToInterfaceAnnotation
public class TestClass implements SomeInterface {

    @MethodWithOneStringParameterAndVoidReturnTypeAnnotation
    public void testMethod(String a) {

    }

    @Override
    public String testMethod() {
        return null;
    }
}
