package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.HeaderHandler;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.HeaderValidationResponse;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.InvalidHeaderException;

import java.util.ArrayList;

public class BaseTemplateParser {
    private ArrayList<String> templateLines;
    private HeaderValidationResponse header;

    public BaseTemplateParser(ArrayList<String> lines)
    {
        HeaderValidationResponse headerValidationResponse = HeaderHandler.validateHeader(lines.get(0));
        if(headerValidationResponse.isValidHeader()) {
            lines.remove(0);
            this.header = headerValidationResponse;
            this.templateLines = lines;
        }
        else {
            throw new InvalidHeaderException();
        }
    }

    public ArrayList<String> getTemplateLines(){
        return templateLines;
    }

    public HeaderValidationResponse getHeader() {
        return header;
    }

    public void setHeader(HeaderValidationResponse header) {
        this.header = header;
    }
}
