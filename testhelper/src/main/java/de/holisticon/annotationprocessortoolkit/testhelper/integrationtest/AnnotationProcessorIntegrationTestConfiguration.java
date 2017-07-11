package de.holisticon.annotationprocessortoolkit.testhelper.integrationtest;

import de.holisticon.annotationprocessortoolkit.testhelper.AnnotationProcessorCommonTestConfiguration;
import de.holisticon.annotationprocessortoolkit.testhelper.validator.TestValidator;

/**
 * Configuration of the an annotation processor test.
 */
public class AnnotationProcessorIntegrationTestConfiguration extends AnnotationProcessorCommonTestConfiguration {

    private final String source;


    public AnnotationProcessorIntegrationTestConfiguration(
            String source,
            Boolean compilingShouldSucceed,
            TestValidator... testcases) {

        super(compilingShouldSucceed, testcases);
        this.source = source;

    }


    public String getSource() {
        return source;
    }

}
