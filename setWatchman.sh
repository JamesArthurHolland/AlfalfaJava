#!/bin/bash

function move_files {
    cp -r ./testrepo/com.github.jamesarthurholland/ ~/.alfalfa/repository/
    cp -r ./alfalfa/src/test/resources/exampleWorkingDirectory ~/testoutput
}

move_files

