<?php

namespace {{ROOT_NAMESPACE}}\Common;

interface StorageInterface
{
    public function save($entity);
    public function fetch($id);
}