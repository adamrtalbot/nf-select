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
     * Check if a value exists in a comma-separated parameter string
     *
     * @param parameter   Comma-separated string to search in
     * @param checkValue  Value to search for (can be String or List)
     * @param defaultTrue If true, return true when parameter is null or empty
     * @param separator   The separator used in the parameter string (default is comma)
     * @return           true if checkValue exists in parameter, false otherwise
     *
     * Using @Function annotation allows this function to be imported from the pipeline script
     * 
     * Example:
     *    checkInParam('foo,bar,baz', 'BAR')               // returns true
     *    checkInParam('foo,bar,baz', ['BAR'])             // returns true
     *    checkInParam('foo,bar,baz', 'notfound')          // returns false
     *    checkInParam('foo,bar,baz', 'missing')           // returns false (default)
     *    checkInParam('foo,bar,baz', 'missing', true)     // returns true
     *    checkInParam('', 'anything', true)               // returns true
     *    checkInParam(null, 'anything', true)             // returns true
     *    checkInParam('foo,bar,baz', 'bar')               // uses default comma separator
     *    checkInParam('foo;bar;baz', 'bar', false, ';')   // uses semicolon separator
     *    checkInParam('foo|bar|baz', 'bar', false, '|')   // uses pipe separator
     */
    @Function
    boolean checkInObject(String parameter, Object checkValue, boolean defaultChoice = false, String separator = ',') {
        if (!parameter) return defaultChoice
        
        List<String> parameterList = parameter.tokenize(separator)*.trim()
        
        if (checkValue instanceof List) {
            return checkValue.any { value -> containsIgnoreCase(parameterList, value.toString()) }
        } else if (checkValue instanceof String) {
            return containsIgnoreCase(parameterList, checkValue.toString())
        }
        
        return defaultChoice
    }

    @Function
    boolean checkInParam(String parameter, Object checkValue, boolean defaultChoice = false, String separator = ',') {
        return checkInObject(parameter, checkValue, defaultChoice, separator)
    }

}

// and:
// when          = { 
//     ( params.run ? params.run.split(',').any{ "NF_CANARY:${it.toUpperCase()}".contains(task.process) } : true ) && 
//     (!params.skip.split(',').any{ "NF_CANARY:${it.toUpperCase()}".contains(task.process) } ) 
// }
// */

