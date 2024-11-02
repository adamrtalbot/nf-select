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

    def 'should check parameter string with various inputs using checkInObject' () {
        given:
        def example = new SelectFunctions()
        example.init(new Session([:]))

        expect:
        // Basic string matching
        example.checkInObject('foo,bar,baz', 'bar') == true
        example.checkInObject('foo,bar,baz', 'BAR') == true
        example.checkInObject('foo,bar,baz', 'missing') == false
        
        // List matching
        example.checkInObject('foo,bar,baz', ['bar', 'baz']) == true
        example.checkInObject('foo,bar,baz', ['missing', 'nothere']) == false
        
        // Default choice behavior
        example.checkInObject('', 'anything', true) == true
        example.checkInObject(null, 'anything', true) == true
        example.checkInObject('foo,bar,baz', 'missing', true) == false
        
        // Custom separator
        example.checkInObject('foo;bar;baz', 'bar', false, ';') == true
        example.checkInObject('foo|bar|baz', 'bar', false, '|') == true
        
        // Whitespace handling
        example.checkInObject('foo, bar, baz', 'bar') == true
        example.checkInObject('foo,   bar,baz  ', 'baz') == true
    }

    def 'should check parameter string with various inputs using checkInParam' () {
        given:
        def example = new SelectFunctions()
        example.init(new Session([:]))

        expect:
        // Basic string matching
        example.checkInParam('foo,bar,baz', 'bar') == true
        example.checkInParam('foo,bar,baz', 'BAR') == true
        example.checkInParam('foo,bar,baz', 'missing') == false
        
        // List matching
        example.checkInParam('foo,bar,baz', ['bar', 'baz']) == true
        example.checkInParam('foo,bar,baz', ['missing', 'nothere']) == false
        
        // Default choice behavior
        example.checkInParam('', 'anything', true) == true
        example.checkInParam(null, 'anything', true) == true
        example.checkInParam('foo,bar,baz', 'missing', true) == false
        
        // Custom separator
        example.checkInParam('foo;bar;baz', 'bar', false, ';') == true
        example.checkInParam('foo|bar|baz', 'bar', false, '|') == true
        
        // Whitespace handling
        example.checkInParam('foo, bar, baz', 'bar') == true
        example.checkInParam('foo,   bar,baz  ', 'baz') == true
    }

    def 'should handle edge cases in parameter checking' () {
        given:
        def example = new SelectFunctions()
        example.init(new Session([:]))

        expect:
        // Empty inputs
        example.checkInObject('', '') == false
        example.checkInObject('', '', true) == true
        example.checkInParam('', '') == false
        example.checkInParam('', '', true) == true
        
        // Non-string/non-list input
        example.checkInObject('foo,bar,baz', 123) == false
        example.checkInObject('foo,bar,baz', 123, true) == true
        example.checkInParam('foo,bar,baz', 123) == false
        example.checkInParam('foo,bar,baz', 123, true) == true
        
        // Mixed case with whitespace
        example.checkInObject('FoO, BAR,   baZ', 'foo') == true
        example.checkInObject('FoO, BAR,   baZ', ['FOO', 'baz']) == true
        example.checkInParam('FoO, BAR,   baZ', 'foo') == true
        example.checkInParam('FoO, BAR,   baZ', ['FOO', 'baz']) == true
    }
}
