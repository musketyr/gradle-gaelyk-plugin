# Gradle Gaelyk plugin

![Gaelyk Logo](http://d.hatena.ne.jp/images/keyword/283651.png)

The plugin provides tasks for managing [Gaelyk](http://gaelyk.appspot.com/) projects in any given Gradle build. It extends
the War plugin.

## Usage

To use the Gaelyk plugin, apply the plugin to your build script:

    apply plugin: 'gaelyk'

The plugin JAR and the App Engine tools SDK library need to be defined in the classpath of your build script. You can
either get the plugin from the GitHub download section or upload it to your local repository. You'll also have to assign
the base directory of your web application (`war` by default). The following code snippet shows an example:

    buildscript {
        repositories {
            add(new org.apache.ivy.plugins.resolver.URLResolver()) {
                name = 'GitHub'
                addArtifactPattern 'http://cloud.github.com/downloads/[organisation]/[module]/[module]-[revision].[ext]'
            }
            mavenCentral()
        }

        dependencies {
            classpath 'bmuschko:gradle-gaelyk-plugin:0.3'
        }
    }

    webAppDirName = new File("war")

## Tasks
* `gaelykListPlugins`: Shows all available plugins from the Gaelyk plugins catalogue.
* `gaelykInstallPlugin`: Installs plugin provided by the command line property `plugin`. Plugin must be plugin identificator from the catalgoue or ZIP
archive either on the file system or on the web.
 _Example:_ `gradle gaelykInstallPlugin -Pplugin=http://cloud.github.com/downloads/bmuschko/gaelyk-jsonlib-plugin/gaelyk-jsonlib-plugin-0.2.zip`
installs the [JSON plugin](https://github.com/bmuschko/gaelyk-jsonlib-plugin).
* `gaelykUninstallPlugin`: Uninstalls plugin specified by given `path` or `name` provided by the
 command line property `plugin`. Path or name can easily be determined by running the `gaelykListInstalledPlugins` task.
 The name is the name of the original file without the ZIP extension.
 _Example:_ `gradle gaelykUninstallPlugin -Pplugin=http://cloud.github.com/downloads/bmuschko/gaelyk-jsonlib-plugin/gaelyk-jsonlib-plugin-0.2.zip`
uninstalls the [JSON plugin](https://github.com/bmuschko/gaelyk-jsonlib-plugin). `gradle gaelykUninstallPlugin -Pplugin=gaelyk-jsonlib-plugin-0.2` would do the same work.
* `gaelykListInstalledPlugins`: Shows plugins that have been installed by the `gaelykInstallPlugin` task.
* `gaelykPrecompileGroovlet`: Precompiles Groovlets to minimize startup costs.
## Task Rules

* `gaelykCreateController<ControllerName>`: Creates a Gaelyk controller (Groovlet). Optionally, you can define the directory
to put the file in using the command line property `dir`. _Example:_ `gradle gaelykCreateControllerUser` creates the file
`user.groovy` in the directory `war/WEB-INF/groovy`.
* `gaelykCreateView<ViewName>`: Creates a Gaelyk view (Groovy template). Optionally, you can define the directory
to put the file in using the command line property `dir`. _Example:_ `gaelykCreateViewAddress -Pdir=address` creates the file
`address.gtpl` in the directory `war/address`.