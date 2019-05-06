package com.github.jamesarthurholland.alfalfa.configurationBuilder.header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jamie on 03/07/16.
 */
public class HeaderHandler
{
    public static HeaderValidationResponse validateHeader(String headerLine)
    {
        Pattern noFileSpecifiedPattern = Pattern.compile("^\\s*\\{\\{ALFALFA\\}\\}\\s*$"); // TODO there was $ at end. assumed mistake. inspect
        Pattern fileSpecifiedPattern = Pattern.compile("^\\s*\\{\\{ALFALFA -o (.*)\\}\\}\\s*$"); // TODO there was $ at end. assumed mistake. inspect
        Matcher outFileMatcher  = fileSpecifiedPattern.matcher(headerLine);
        Matcher noFileSpecifiedHeaderMatcher= noFileSpecifiedPattern.matcher(headerLine);

        if (noFileSpecifiedHeaderMatcher.matches()) {
            return new HeaderValidationResponse(HeaderValidationResponse.VALID_HEADER_NO_OUTPUT_FILE_NAMED);
        }
        if (outFileMatcher.matches()) {
            String fileName = outFileMatcher.group(1);
            return new HeaderValidationResponse(HeaderValidationResponse.VALID_HEADER_OUTPUT_FILE_NAMED, fileName);
        }

        return new HeaderValidationResponse(HeaderValidationResponse.INVALID_HEADER);
    }


}
