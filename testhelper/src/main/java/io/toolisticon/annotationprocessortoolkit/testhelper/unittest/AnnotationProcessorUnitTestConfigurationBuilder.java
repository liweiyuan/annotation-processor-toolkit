package io.toolisticon.annotationprocessortoolkit.testhelper.unittest;


import io.toolisticon.annotationprocessortoolkit.testhelper.validator.TestMessageValidator;

import javax.tools.JavaFileObject;


/**
 * Configuration builder class for of unit tests.
 */
public final class AnnotationProcessorUnitTestConfigurationBuilder {


    private AnnotationProcessorUnitTestConfigurationBuilder() {
    }

    public BaseConfigurationBuilder start() {
        return new BaseConfigurationBuilder();
    }

    public static class BaseConfigurationBuilder {

        private Boolean shouldCompileSuccessfully = true;
        private JavaFileObject[] expectedGeneratedJavaFileObjects;
        private TestMessageValidator testMessageValidator;
        private AbstractUnitTestAnnotationProcessorClass processor;
        private String customSourceFile;

        public MessageEvaluation addMessageValidator() {
            return new MessageEvaluation(this);
        }

        public BaseConfigurationBuilder setProcessor(AbstractUnitTestAnnotationProcessorClass processorToBeSet) {
            this.processor = processorToBeSet;
            return this;
        }


        public BaseConfigurationBuilder compilationShouldFail() {
            shouldCompileSuccessfully = false;
            return this;
        }

        public BaseConfigurationBuilder compilationShouldSucceed() {
            shouldCompileSuccessfully = true;
            return this;
        }

        public BaseConfigurationBuilder resourceShouldMatch(JavaFileObject... expectedGeneratedJavaFileObjects) {
            this.expectedGeneratedJavaFileObjects = expectedGeneratedJavaFileObjects;
            return this;
        }

        public BaseConfigurationBuilder useCustomSourceFile(String customSourceFile) {
            this.customSourceFile = customSourceFile;
            return this;
        }

        public AnnotationProcessorUnitTestConfiguration build() {
            if (testMessageValidator == null) {
                return new AnnotationProcessorUnitTestConfiguration(customSourceFile, processor, shouldCompileSuccessfully, expectedGeneratedJavaFileObjects);
            } else {
                return new AnnotationProcessorUnitTestConfiguration(customSourceFile, processor, shouldCompileSuccessfully, expectedGeneratedJavaFileObjects,
                        testMessageValidator);
            }
        }
    }


    public static class MessageEvaluation {

        private final BaseConfigurationBuilder baseConfigurationBuilder;
        private String[] warningChecks;
        private String[] errorChecks;
        private String[] noteChecks;


        MessageEvaluation(BaseConfigurationBuilder baseConfigurationBuilder) {
            this.baseConfigurationBuilder = baseConfigurationBuilder;
        }

        public MessageEvaluation setWarningChecks(String... warningChecksToSet) {
            this.warningChecks = warningChecksToSet;
            return this;
        }

        public MessageEvaluation setErrorChecks(String... errorChecksToSet) {
            this.errorChecks = errorChecksToSet;
            return this;
        }

        public MessageEvaluation setNoteChecks(String... noteChecksToSet) {
            this.noteChecks = noteChecksToSet;
            return this;
        }

        public BaseConfigurationBuilder finishMessageValidator() {
            baseConfigurationBuilder.testMessageValidator = new TestMessageValidator(
                    errorChecks != null ? errorChecks : new String[0],
                    warningChecks != null ? warningChecks : new String[0],
                    noteChecks != null ? noteChecks : new String[0]
            );
            return baseConfigurationBuilder;
        }

    }


    public static BaseConfigurationBuilder createTestConfig() {

        AnnotationProcessorUnitTestConfigurationBuilder testBuilder = new AnnotationProcessorUnitTestConfigurationBuilder();

        return testBuilder.start();

    }


}
