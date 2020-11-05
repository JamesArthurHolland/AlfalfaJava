<?php

namespace {{PROJECT}}\DI;

use DI\ContainerBuilder;

class PhpDiBuilderManager implements BuilderManager
{
    private $builder;

    public function __construct()
    {
        $this->builder = new ContainerBuilder();
        $this->builder->useAnnotations(true);
    }

    public function addDiToBuilder() {
        $files = glob(__DIR__ . '/di/*.php');

        foreach ($files as $file) {
            $this->builder->addDefinitions($file);
        }

    }

    public function build() {
        return $this->builder->build();
    }
}