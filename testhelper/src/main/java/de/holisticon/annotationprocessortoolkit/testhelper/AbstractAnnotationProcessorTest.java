package de.holisticon.annotationprocessortoolkit.testhelper;

import com.google.testing.compile.CompileTester;
import de.holisticon.annotationprocessortoolkit.testhelper.validator.TestMessageValidator;
import de.holisticon.annotationprocessortoolkit.testhelper.validator.TestValidator;
import de.holisticon.annotationprocessortoolkit.testhelper.validator.TestValidatorType;
import org.hamcrest.MatcherAssert;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;


/**
 * Abstract base class which allows parameterized tests.
 */

public abstract class AbstractAnnotationProcessorTest<T extends AnnotationProcessorCommonTestConfiguration> {

    public static final String TEST_EXECUTION_MESSAGE = "!!!--- TEST WAS EXECUTED ---!!!";

    private T annotationProcessorCommonTestConfiguration;

    public AbstractAnnotationProcessorTest(T annotationProcessorCommonTestConfiguration) {
        this.annotationProcessorCommonTestConfiguration = annotationProcessorCommonTestConfiguration;
    }


    /**
     * Gets the annotation processor to used for testing.
     *
     * @return the annotation processor to use for testing
     */
    protected abstract Processor getAnnotationProcessor();

    /**
     * Gets the {@link JavaFileObject} to be compiled.
     *
     * @return the code to be compiled
     */
    protected abstract JavaFileObject getSourceFileForCompilation();


    private Processor getWrappedProcessor() {
        return AnnotationProcessorWrapper.wrapProcessor(getAnnotationProcessor());
    }


    protected void test() {

        JavaFileObject testClassSource = getSourceFileForCompilation();

        TestMessageValidator messageValidationTest = (TestMessageValidator) getTestOfType(
                annotationProcessorCommonTestConfiguration.getTestcases(),
                TestValidatorType.MESSAGE_VALIDATOR);


        if (messageValidationTest == null || messageValidationTest.getErrors().length == 0) {
            CompileTester.SuccessfulCompilationClause compileTester = assertAbout(javaSource())
                    .that(testClassSource)
                    .processedWith(this.getWrappedProcessor()).compilesWithoutError();

            if (annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed() != null) {
                MatcherAssert.assertThat("Compiling should have succeed", annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed());
            }

            // check for warnings
            if (messageValidationTest != null) {
                for (String warning : messageValidationTest.getWarnings()) {
                    compileTester.withWarningContaining(warning);
                }
            }


            // check if test was executed
            compileTester.withNoteContaining(TEST_EXECUTION_MESSAGE);

        } else {
            CompileTester.UnsuccessfulCompilationClause compileTester = assertAbout(javaSource())
                    .that(testClassSource)
                    .processedWith(this.getWrappedProcessor()).failsToCompile();

            if (annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed() != null) {
                MatcherAssert.assertThat("Compiling should have failed", !annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed());
            }

            // check for errors
            for (String error : messageValidationTest.getErrors()) {
                compileTester.withErrorContaining(error);
            }

            // check if test was executed
            compileTester.withNoteContaining(TEST_EXECUTION_MESSAGE);
        }


    }

    protected T getAnnotationProcessorTestConfiguration() {
        return annotationProcessorCommonTestConfiguration;
    }

    private TestValidator getTestOfType(TestValidator[] testValidators, TestValidatorType type) {

        if (testValidators == null || type == null) {
            return null;
        }

        for (TestValidator testValidator : testValidators) {

            if (type.equals(testValidator.getAnnotationProcessorTestType())) {
                return testValidator;
            }

        }

        return null;

    }


}
