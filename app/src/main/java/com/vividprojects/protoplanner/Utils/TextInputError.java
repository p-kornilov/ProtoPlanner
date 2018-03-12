package com.vividprojects.protoplanner.Utils;

/**
 * Created by Smile on 12.03.2018.
 */

public class TextInputError extends Error {
    private String errorPart;

    public TextInputError(String errorPart) {
        this.errorPart = errorPart;
    }

    public String getErrorPart() {
        return errorPart;
    }
}
