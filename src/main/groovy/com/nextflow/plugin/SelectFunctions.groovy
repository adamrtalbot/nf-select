package com.nextflow.plugin

import groovy.transform.CompileStatic
import nextflow.plugin.extension.Function
import nextflow.plugin.extension.PluginExtensionPoint
import nextflow.Session

@CompileStatic
class SelectFunctions extends PluginExtensionPoint{

    private Session session
    private SelectConfiguration configuration

    @Override
    protected void init(Session session) {
        this.session = session
        this.configuration = parseConfig(session.config.navigate('example') as Map)
    }

    protected SelectConfiguration parseConfig(Map map){
        new SelectConfiguration(map)
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
        return list.any { it.toString().equalsIgnoreCase(searchStr) }
    }

    /*
     * Compare two strings for equality, ignoring case sensitivity
     *
     * @param str1 First string to compare
     * @param str2 Second string to compare
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
    boolean containsIgnoreCase(String str1, String str2) {
        return str1.equalsIgnoreCase(str2)
    }

}
