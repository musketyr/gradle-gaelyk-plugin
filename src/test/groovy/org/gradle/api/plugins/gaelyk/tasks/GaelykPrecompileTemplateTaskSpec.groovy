/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.plugins.gaelyk.tasks

import org.gradle.api.Project
import org.gradle.api.plugins.gaelyk.tools.TempDir
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import org.gradle.api.plugins.GroovyBasePlugin

class PrecompileTemplateTaskSpec extends Specification {
    def "Test precompile task"() {
        given:
            Project project = ProjectBuilder.builder().build()
            GroovyBasePlugin groovyBasePlugin = new GroovyBasePlugin()
            groovyBasePlugin.apply(project)
            def configuration = project.configurations.getByName(GroovyBasePlugin.GROOVY_CONFIGURATION_NAME)

            def dir = TempDir.createNew("precompile-task")
            def templatesSrcDir = new File(dir, '/templates')
            templatesSrcDir.mkdirs()
            new File(templatesSrcDir, 'datetime.gtpl').append('<html><body>Hello $world</body></html>')
            def packageDir = new File(templatesSrcDir, '/pkg')
            packageDir.mkdirs()
            new File(packageDir, 'datetime.gtpl').append('<html><body>Hello $world</body></html>')
            
            def classDestDir = new File(dir, '/classes')
            classDestDir.mkdirs()

            GaelykPrecompileTemplateTask task = project.task('precompile', type: GaelykPrecompileTemplateTask)
            task.groovyClasspath = configuration.asFileTree
            task.runtimeClasspath = project.files([])
            task.srcDir = templatesSrcDir
            task.destDir = classDestDir

        when:
            task.precompile()

        then:
            new File(classDestDir, '$gtpl$datetime.class').exists()
            new File(classDestDir.absolutePath + '/pkg', '$gtpl$datetime.class').exists()
    }
    
    
    
    def "Get script name"(){
        def info =  GaelykPrecompileTemplateTask.getTemplateScriptInfo(new File('gtpls'), new File('gtpls/xyz/template.gtpl'))
        expect:
            info.dir == 'xyz'
            info.file == '$gtpl$template.groovy'
    }
    
    def "Dir to pkg"(){
        expect:
        GaelykPrecompileTemplateTask.dirToPackage('') == ''
        GaelykPrecompileTemplateTask.dirToPackage('pkg') == 'pkg'
        GaelykPrecompileTemplateTask.dirToPackage('pkg/next/other') == 'pkg.next.other'
        GaelykPrecompileTemplateTask.dirToPackage('WEB-INF/something') == 'web_inf.something'
        GaelykPrecompileTemplateTask.dirToPackage('I"m extra obscure') == 'i_m_extra_obscure'
        
    }
}
