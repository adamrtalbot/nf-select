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

    def 'should check if strings are equal case insensitive' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))

        expect:
        select.containsIgnoreCase('Hello', 'hello') == true
        select.containsIgnoreCase('World', 'WORLD') == true
        select.containsIgnoreCase('Hello', 'World') == false
        select.containsIgnoreCase('', '') == true
        select.containsIgnoreCase('  test  ', 'TEST') == true
    }

    def 'should check parameter string with various inputs using select' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))

        expect:
        // Basic string matching with select
        select.select(pattern: 'bar', select: 'foo,bar,baz') == true
        select.select(pattern: 'BAR', select: 'foo,bar,baz') == true
        select.select(pattern: 'missing', select: 'foo,bar,baz') == false
        
        // List pattern matching
        select.select(pattern: ['bar', 'baz'], select: 'foo,bar,baz') == true
        select.select(pattern: ['missing', 'nothere'], select: 'foo,bar,baz') == false
        
        // Anti-select matching
        select.select(pattern: 'bar', antiSelect: 'bar,baz') == false
        select.select(pattern: 'foo', antiSelect: 'bar,baz') == true
        
        // Combined select and anti-select
        select.select(pattern: 'bar', select: 'foo,bar,baz', antiSelect: 'baz') == true
        select.select(pattern: 'bar', select: 'foo,bar,baz', antiSelect: 'bar') == false
        
        // Default choice behavior
        select.select(pattern: null, defaultChoice: true) == true
        select.select(pattern: null, defaultChoice: false) == false
        
        // Custom separator
        select.select(pattern: 'bar', select: 'foo;bar;baz', separator: ';') == true
        select.select(pattern: 'bar', select: 'foo|bar|baz', separator: '|') == true
        
        // Whitespace handling
        select.select(pattern: 'bar', select: 'foo, bar, baz') == true
        select.select(pattern: 'baz', select: 'foo,   bar,baz  ') == true
    }

    def 'should handle edge cases in parameter checking' () {
        given:
        def select = new SelectFunctions()
        select.init(new Session([:]))

        expect:
        // Empty inputs
        select.select(pattern: '', select: '', defaultChoice: false) == false
        select.select(pattern: '', select: '', defaultChoice: true) == true
        
        // Empty select/antiSelect
        select.select(pattern: 'foo') == true
        select.select(pattern: 'foo', select: '', antiSelect: '') == true
        
        // Non-string/non-list input
        select.select(pattern: 123, select: 'foo,bar,baz') == false
        
        // Mixed case with whitespace
        select.select(pattern: 'foo', select: 'FoO, BAR,   baZ') == true
        select.select(pattern: ['FOO', 'baz'], select: 'FoO, BAR,   baZ') == true
        
        // Complex combinations
        select.select(pattern: ['foo', 'bar'], select: 'foo,bar', antiSelect: 'baz') == true
        select.select(pattern: ['foo', 'baz'], select: 'foo,bar', antiSelect: 'baz') == false
    }
}