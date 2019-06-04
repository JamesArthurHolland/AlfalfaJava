package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header;

/**
 * Created by jamie on 03/07/16.
 */
public class HeaderValidationResponse
{
    public static final int INVALID_HEADER = 0;
    public static final int VALID_HEADER_NO_OUTPUT_FILE_NAMED = 1;
    public static final int VALID_HEADER_OUTPUT_FILE_NAMED = 2;

    protected int validationCode = 0;
    protected String fileName;

    public HeaderValidationResponse(int validationCode)
    {
        this.validationCode = validationCode;
    }

    public HeaderValidationResponse(int validationCode, String fileName)
    {
        this.validationCode = validationCode;
        this.fileName = fileName;
    }

    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

    public boolean isValidHeader() {
        switch (getValidationCode()) {
            case INVALID_HEADER:
                return false;
            case VALID_HEADER_NO_OUTPUT_FILE_NAMED:
                return true;
            case VALID_HEADER_OUTPUT_FILE_NAMED:
                return true;
        }
        return false;
    }

    public String getFileName()
    {
        return this.fileName;
    }
}
