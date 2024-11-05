package com.nextflow.plugin

import groovy.transform.CompileStatic
import nextflow.plugin.extension.Function
import nextflow.plugin.extension.PluginExtensionPoint
import nextflow.Session

@CompileStatic
class SelectFunctions extends PluginExtensionPoint{

    private Session session

    @Override
    protected void init(Session session) {
        this.session = session
    }

    /*
     * Check if a string exists in a list, ignoring case sensitivity
     *
     * @param list      List of strings to search through
     * @param searchStr String to search for
     * @return         true if searchStr exists in list (case insensitive), false otherwise
     *
     * Using @Function annotation allows this function to be imported from the pipeline script
     * 
     * Example:
     *    containsIgnoreCase(['Hello', 'World'], 'hello') // returns true
     *    containsIgnoreCase(['Hello', 'World'], 'WORLD') // returns true
     *    containsIgnoreCase(['Hello', 'World'], 'notfound') // returns false
     */
    @Function
    boolean containsIgnoreCase(List<String> list, String searchStr) {
        return list.any { it.toString().strip().equalsIgnoreCase(searchStr.strip()) }
    }

    /*
     * Compare two strings for equality, ignoring case sensitivity
     *
     * @param str First string to compare
     * @param searchStr Second string to compare
     * @return    true if strings are equal (case insensitive), false otherwise
     *
     * Using @Function annotation allows this function to be imported from the pipeline script
     * 
     * Example:
     *    containsIgnoreCase('Hello', 'hello') // returns true
     *    containsIgnoreCase('World', 'WORLD') // returns true
     *    containsIgnoreCase('Hello', 'World') // returns false
     */
    @Function
    boolean containsIgnoreCase(String str, String searchStr) {
        return str.strip().equalsIgnoreCase(searchStr.strip())
    }
    
    /*
     * Check if pattern exists in select list but not in antiSelect list
     *
     * @param pattern       Value or List to check (can be String or List)
     * @param select        Comma-separated string of allowed values (empty by default)
     * @param antiSelect    Comma-separated string of excluded values (empty by default) 
     * @param defaultChoice If true, return true when pattern is null (default: false)
     * @param separator     The separator used in the select/antiSelect strings (default: comma)
     * @return              true if pattern matches select criteria and not antiSelect, false otherwise
     *
     * Using @Function annotation allows this function to be imported from the pipeline script
     * 
     * Example:
     *    select(pattern: 'bar', select: 'foo,bar,baz')                 // returns true
     *    select(pattern: ['bar','foo'], select: 'foo,bar,baz')        // returns true
     *    select(pattern: 'bar', select: 'foo,bar', antiSelect: 'bar') // returns false
     *    select(pattern: 'qux', select: 'foo,bar,baz')                // returns false
     *    select(pattern: null, defaultChoice: true)                    // returns true
     *    select(pattern: 'bar', select: 'foo;bar;baz', separator: ';') // returns true
     *    select(pattern: 'bar', antiSelect: 'bar,baz')                // returns false
     */
     */    @Function
    boolean select(Map options = null) {
        def pattern           = options?.pattern
        String select         = options?.select        ?: ''
        String antiSelect     = options?.antiSelect    ?: ''
        boolean defaultChoice = options?.defaultChoice ?: false
        String separator      = options?.separator     ?: ','

        if (pattern == null || pattern == '') return defaultChoice

        List<String> selectList = select ? select.tokenize(separator).collect{it.trim()} : []
        List<String> antiSelectList = antiSelect ? antiSelect.tokenize(separator).collect{it.trim()} : []

        if (pattern instanceof List) {
            return pattern.every { item ->
                (selectList.isEmpty() || containsIgnoreCase(selectList, item.toString())) &&
                (antiSelectList.isEmpty() || !containsIgnoreCase(antiSelectList, item.toString()))
            }
        } else if (pattern instanceof String) {
            return (selectList.isEmpty() || containsIgnoreCase(selectList, pattern.toString())) &&
                   (antiSelectList.isEmpty() || !containsIgnoreCase(antiSelectList, pattern.toString()))
        }

        return defaultChoice
    }

}