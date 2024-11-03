package com.nextflow.plugin

import nextflow.Session
import spock.lang.Specification

class SelectFunctionsTest extends Specification {

    def 'should check if string exists in list case insensitive' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))
        def testList = ['Hello', 'World', 'Test']

        expect:
        select.containsIgnoreCase(testList, 'hello') == true
        select.containsIgnoreCase(testList, 'WORLD') == true
        select.containsIgnoreCase(testList, 'test') == true
        select.containsIgnoreCase(testList, 'notfound') == false
        select.containsIgnoreCase(testList, '') == false
    }
    
    def 'should check if string exists in list case insensitive' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))
        def testList = ['Hello', 'World', 'Test']

        expect:
        select.containsIgnoreCase(testList, 'hello') == true
        select.containsIgnoreCase(testList, 'WORLD') == true
        select.containsIgnoreCase(testList, 'test') == true
        select.containsIgnoreCase(testList, 'notfound') == false
        select.containsIgnoreCase(testList, '') == false
    }

    def 'should check parameter string with various inputs using checkInObject' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))

        expect:
        // Basic string matching
        select.checkInObject('foo,bar,baz', 'bar') == true
        select.checkInObject('foo,bar,baz', 'BAR') == true
        select.checkInObject('foo,bar,baz', 'missing') == false
        
        // List matching
        select.checkInObject('foo,bar,baz', ['bar', 'baz']) == true
        select.checkInObject('foo,bar,baz', ['missing', 'nothere']) == false
        
        // Default choice behavior
        select.checkInObject('', 'anything', true) == true
        select.checkInObject(null, 'anything', true) == true
        select.checkInObject('foo,bar,baz', 'missing', true) == false
        
        // Custom separator
        select.checkInObject('foo;bar;baz', 'bar', false, ';') == true
        select.checkInObject('foo|bar|baz', 'bar', false, '|') == true
        
        // Whitespace handling
        select.checkInObject('foo, bar, baz', 'bar') == true
        select.checkInObject('foo,   bar,baz  ', 'baz') == true
    }

    def 'should check parameter string with various inputs using checkInParam' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))

        expect:
        // Basic string matching
        select.checkInParam('foo,bar,baz', 'bar') == true
        select.checkInParam('foo,bar,baz', 'BAR') == true
        select.checkInParam('foo,bar,baz', 'missing') == false
        
        // List matching
        select.checkInParam('foo,bar,baz', ['bar', 'baz']) == true
        select.checkInParam('foo,bar,baz', ['missing', 'nothere']) == false
        
        // Default choice behavior
        select.checkInParam('', 'anything', true) == true
        select.checkInParam(null, 'anything', true) == true
        select.checkInParam('foo,bar,baz', 'missing', true) == false
        
        // Custom separator
        select.checkInParam('foo;bar;baz', 'bar', false, ';') == true
        select.checkInParam('foo|bar|baz', 'bar', false, '|') == true
        
        // Whitespace handling
        select.checkInParam('foo, bar, baz', 'bar') == true
        select.checkInParam('foo,   bar,baz  ', 'baz') == true
    }

    def 'should handle edge cases in parameter checking' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))

        expect:
        // Empty inputs
        select.checkInObject('', '') == false
        select.checkInObject('', '', true) == true
        select.checkInParam('', '') == false
        select.checkInParam('', '', true) == true
        
        // Non-string/non-list input
        select.checkInObject('foo,bar,baz', 123) == false
        select.checkInObject('foo,bar,baz', 123, true) == true
        select.checkInParam('foo,bar,baz', 123) == false
        select.checkInParam('foo,bar,baz', 123, true) == true
        
        // Mixed case with whitespace
        select.checkInObject('FoO, BAR,   baZ', 'foo') == true
        select.checkInObject('FoO, BAR,   baZ', ['FOO', 'baz']) == true
        select.checkInParam('FoO, BAR,   baZ', 'foo') == true
        select.checkInParam('FoO, BAR,   baZ', ['FOO', 'baz']) == true
    }
}
