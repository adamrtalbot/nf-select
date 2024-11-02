package com.nextflow.plugin

import nextflow.Channel
import nextflow.plugin.Plugins
import nextflow.plugin.TestPluginDescriptorFinder
import nextflow.plugin.TestPluginManager
import nextflow.plugin.extension.PluginExtensionProvider
import org.pf4j.PluginDescriptorFinder
import spock.lang.Shared
import test.Dsl2Spec
import test.MockScriptRunner

import java.nio.file.Path

class PluginTest extends Dsl2Spec{

    @Shared String pluginsMode

    def setup() {
        // reset previous instances
        PluginExtensionProvider.reset()
        // this need to be set *before* the plugin manager class is created
        pluginsMode = System.getProperty('pf4j.mode')
        System.setProperty('pf4j.mode', 'dev')
        // the plugin root should
        def root = Path.of('.').toAbsolutePath().normalize()
        def manager = new TestPluginManager(root){
            @Override
            protected PluginDescriptorFinder createPluginDescriptorFinder() {
                return new TestPluginDescriptorFinder(){
                    @Override
                    protected Path getManifestPath(Path pluginPath) {
                        return pluginPath.resolve('build/tmp/jar/MANIFEST.MF')
                    }
                }
            }
        }
        Plugins.init(root, 'dev', manager)
    }

    def cleanup() {
        Plugins.stop()
        PluginExtensionProvider.reset()
        pluginsMode ? System.setProperty('pf4j.mode',pluginsMode) : System.clearProperty('pf4j.mode')
    }

    def 'should starts' () {
        when:
        def SCRIPT = '''
            channel.of('hi!') 
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == 'hi!'
        result.val == Channel.STOP
    }
    def 'should execute containsIgnoreCase function' () {
        when:
        def SCRIPT = '''
            include {containsIgnoreCase} from 'plugin/nf-select'
            channel
                .of( containsIgnoreCase(['Hello', 'World'], 'HELLO') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should handle non-matching case with containsIgnoreCase' () {
        when:
        def SCRIPT = '''
            include {containsIgnoreCase} from 'plugin/nf-select'
            channel
                .of( containsIgnoreCase(['Hello', 'World'], 'notfound') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == false
        result.val == Channel.STOP
    }

    def 'should execute containsIgnoreCase with list' () {
        when:
        def SCRIPT = '''
            include {containsIgnoreCase} from 'plugin/nf-select'
            channel
                .of( containsIgnoreCase(['Hello', 'World'], 'HELLO') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute containsIgnoreCase with strings' () {
        when:
        def SCRIPT = '''
            include {containsIgnoreCase} from 'plugin/nf-select'
            channel
                .of( containsIgnoreCase('Hello', 'HELLO') )
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }
    def 'should execute checkInObject with string input' () {
        when:
        def SCRIPT = '''
            include {checkInObject} from 'plugin/nf-select'
            channel
                .of( checkInObject('foo,bar,baz', 'BAR') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute checkInObject with list input' () {
        when:
        def SCRIPT = '''
            include {checkInObject} from 'plugin/nf-select'
            channel
                .of( checkInObject('foo,bar,baz', ['BAR', 'BAZ']) )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute checkInObject with custom separator' () {
        when:
        def SCRIPT = '''
            include {checkInObject} from 'plugin/nf-select'
            channel
                .of( checkInObject('foo|bar|baz', 'BAR', false, '|') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute checkInObject with default choice' () {
        when:
        def SCRIPT = '''
            include {checkInObject} from 'plugin/nf-select'
            channel
                .of( checkInObject('', 'anything', true) )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute checkInParam with string input' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            channel
                .of( checkInParam('foo,bar,baz', 'BAR') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute checkInParam with list input' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            channel
                .of( checkInParam('foo,bar,baz', ['BAR', 'BAZ']) )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute checkInParam with custom separator' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            channel
                .of( checkInParam('foo|bar|baz', 'BAR', false, '|') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should execute checkInParam with default choice' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            channel
                .of( checkInParam('', 'anything', true) )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }
    def 'should select from parameter value' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            params.tools = "star"
            channel
                .of( checkInParam(params.tools, 'star') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'should return false from parameter value' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            params.tools = "star"
            channel
                .of( checkInParam(params.tools, 'notstar') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == false
        result.val == Channel.STOP
    }

    def 'should ignore empty parameter value' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            params.tools = ""
            channel
                .of( checkInParam(params.tools, 'star') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == false
        result.val == Channel.STOP
    }

    def 'can be inverted' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            params.tools = ""
            channel
                .of( !checkInParam(params.tools, 'star') )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'null params are ignored' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            params.tools = null
            channel
                .of( checkInParam(params.tools, 'star', true) )      
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }

    def 'combine two params' () {
        when:
        def SCRIPT = '''
            include {checkInParam} from 'plugin/nf-select'
            params.tools = "foo"
            params.skip_tools = "bar"
            channel
                .of( checkInParam(params.tools, 'foo') && checkInParam(params.skip_tools, 'bar') )     
            '''
        and:
        def result = new MockScriptRunner([:]).setScript(SCRIPT).execute()
        then:
        result.val == true
        result.val == Channel.STOP
    }
}