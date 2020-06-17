#!/bin/bash

function move_files {
    rm -rf ~/.alfalfa/repository/com.github.jamesarthurholland/
    cp -rf ./testrepo/com.github.jamesarthurholland ~/.alfalfa/repository/
    cp -rf ./alfalfa/src/test/resources/exampleWorkingDirectory ~/testoutput
}

move_files

