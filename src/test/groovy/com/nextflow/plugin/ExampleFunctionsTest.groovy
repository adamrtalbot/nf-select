package com.nextflow.plugin

import nextflow.Session
import spock.lang.Specification

class SelectFunctionsTest extends Specification {

    def 'should return random string' () {
        given:
        def example = new SelectFunctions()
        example.init(new Session([:]))

        when:
        def result = example.randomString(9)
        println result

        then:
        result.size()==9
    }
}