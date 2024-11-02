package com.nextflow.plugin

import groovy.transform.PackageScope

@PackageScope
class SelectConfiguration {

    final private int maxSize

    SelectConfiguration(Map map){
        def config = map ?: Collections.emptyMap()
        maxSize = (config.maxSize ?: 1000) as int
    }

    int getMaxRandomSizeString(){
        maxSize
    }
}
