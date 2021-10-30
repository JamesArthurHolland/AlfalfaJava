package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header;

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
        Pattern fileSpecifiedPattern = Pattern.compile(".*-o\\s+(.*\\.[-._A-Za-z0-9]+).*"); // TODO there was $ at end. assumed mistake. inspect
        Pattern typeSystemPattern = Pattern.compile(".*-ts\\s+([-._A-Za-z0-9]+).*");

        Matcher outFileMatcher  = fileSpecifiedPattern.matcher(headerLine);
        Matcher noFileSpecifiedHeaderMatcher= noFileSpecifiedPattern.matcher(headerLine);
        Matcher typeSystemMatcher = typeSystemPattern.matcher(headerLine);


        String filename = "";
        String typeSystemName = null;
        if (typeSystemMatcher.matches()) {
            typeSystemName = typeSystemMatcher.group(1);
        }
        if (noFileSpecifiedHeaderMatcher.matches()) {
            return new HeaderValidationResponse(HeaderValidationResponse.VALID_HEADER_NO_OUTPUT_FILE_NAMED, filename, typeSystemName);
        }
        if (outFileMatcher.matches()) {
            filename = outFileMatcher.group(1);
            return new HeaderValidationResponse(HeaderValidationResponse.VALID_HEADER_OUTPUT_FILE_NAMED, filename, typeSystemName);
        }


        return new HeaderValidationResponse(HeaderValidationResponse.INVALID_HEADER);
    }


}
