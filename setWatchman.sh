#!/bin/bash

function move_files {
    cp -rf ./testrepo/com.github.jamesarthurholland ~/.alfalfa/repository/
    cp -rf ./alfalfa/src/test/resources/exampleWorkingDirectory ~/testoutput
}

move_files

