package com.nextflow.plugin

import nextflow.Session
import spock.lang.Specification

class SelectFunctionsTest extends Specification {

    def 'should check if string exists in list case insensitive' () {
        given:
        def example = new SelectFunctions()
        example.init(new Session([:]))
        def testList = ['Hello', 'World', 'Test']

        expect:
        example.containsIgnoreCase(testList, 'hello') == true
        example.containsIgnoreCase(testList, 'WORLD') == true
        example.containsIgnoreCase(testList, 'test') == true
        example.containsIgnoreCase(testList, 'notfound') == false
        example.containsIgnoreCase(testList, '') == false
    }
}
