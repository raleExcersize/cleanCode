/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clean.code.successive.refinement.error;

/**
 *
 * @author Nevena
 */
public class ArgsException extends Exception {

    private static final long serialVersionUID = 1L;

    private Character errorArgumentId = '\0';
    private String errorParameter = "TILT";
    private ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.OK;

    public String getErrorParameter() {
        return errorParameter;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public char getErrorArgumentId() {
        return errorArgumentId;
    }

    public void errorArgumentId(Character errorArgumentId) {
        this.errorArgumentId = errorArgumentId;
    }

    public enum ErrorCode {

        OK,
        MISSING_STRING,
        MISSING_INTEGER,
        MISSING_DOUBLE,
        INVALID_INTEGER,
        INVALID_DOUBLE,
        UNEXPECTED_ARGUMENT,
        INVALID_ARGUMENT_NAME,
        INVALID_ARGUMENT_FORMAT
    }

    public ArgsException(String message) {
        super(message);
    }

    public ArgsException(ArgsException.ErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ArgsException(String errorParameter, ArgsException.ErrorCode errorCode) {
        this(null, errorParameter, errorCode);
    }

    public ArgsException(Character errorArgumentId, ArgsException.ErrorCode errorCode) {
        this(errorArgumentId, null, errorCode);
    }

    public ArgsException(Character errorArgumentId, String errorParameter, ArgsException.ErrorCode errorCode) {
        if (errorArgumentId != null) {
            this.errorArgumentId = errorArgumentId;
        }
        if (errorParameter != null) {
            this.errorParameter = errorParameter;

        }
        this.errorCode = errorCode;
    }

    public String errorMessage() throws Exception {
        switch (errorCode) {
            case OK:
                throw new Exception("TILT: Should not get here.");
            case UNEXPECTED_ARGUMENT:
                return String.format("Argument -%c unexpected.", errorArgumentId);
            case MISSING_STRING:
                return String.format("Could not find string parameter for -%c.",
                        errorArgumentId);
            case INVALID_INTEGER:
                return String.format(
                        "Argument -%c expects an integer but was '%s'.",
                        errorArgumentId, errorParameter);
            case MISSING_INTEGER:
                return String.format("Could not find integer parameter for -%c.",
                        errorArgumentId);
            case MISSING_DOUBLE:
                return String.format("Could not find double parameter for -%c.",
                        errorArgumentId);
            case INVALID_DOUBLE:
                return String.format(
                        "Argument -%c expects a double but was '%s'.",
                        errorArgumentId, errorParameter);
            case INVALID_ARGUMENT_NAME:
                return String.format(
                        "Argument: %c has invalid format.", errorArgumentId);
        }
        return "";
    }

}
