# Notes

This page contains miscellaneous notes on developing a plugin.

## Setting up the plugin

[https://github.com/adamrtalbot/nf-select/pull/1](https://github.com/adamrtalbot/nf-select/pull/1)

- I cloned forked the repo from https://github.com/edn-es/nf-plugin-template which comes with a nice amount of features but is a little bit complicated to understand.
- Renaming was hard because of the build system, however I managed it by:
  - Renaming every file from 'Example' to 'Select'
  - Renaming classes within the files
  - Adding the extensionPoints to the paths in nf-plugin.gradle <- this one tripped me up for ages. But then I commented it out and it still worked?
  - I'm still not sure how the import system works.
  - I confirmed stuff by adding an error to the example function, which made all tests fail.
- Check out Stephen Kelly's notes here for additional details: https://github.com/stevekm/nf-niceutils/blob/main/NOTES.md

## Adding CI

[https://github.com/adamrtalbot/nf-select/pull/2](https://github.com/adamrtalbot/nf-select/pull/2)

- The CI test came from https://github.com/Midnighter/cookiecutter-nf-plugin which is older but simpler. It's much easier to use because the cookiecutter solves everything for you, but the features from Jorge's template are nice to have.
- I initially added a test for Java 21 (Temurin) but it raised the error "Unsupported class file major version 65". Since it didn't fix it I decided to give up and just remove it, not sure if it works on Nextflow but that might not matter once we start developing properly.

## Adding a function

[https://github.com/adamrtalbot/nf-select/pull/3](https://github.com/adamrtalbot/nf-select/pull/3)

Now I had the template in place, this was quite easy. It's straightforward Groovy and while not perfect, it was pretty straightforward. I added a slightly pointless method to make my life easier.

All pretty straightforward...

## Testing

The two main test paths for a function are:

- [ExampleFunctionsTest.groovy](./src/test/groovy/com/nextflow/plugin/ExampleFunctionsTest.groovy): Tests for the function. Think of these as your "Groovy" unit tests.
- [PluginTest.groovy](./src/test/groovy/com/nextflow/plugin/PluginTest.groovy): Plugin tests which write temporary Nextflow and run them. Jorge has made a pretty cool mock of Nextflow which means you don't need 1 million Nextflow scripts and it's very ligthweight. This is the "Nextflow" unit tests.

Writing these wasn't too bad with the help of Claude. Thanks Claude.

## Installing and using locally

I found installing a bit confusing because Jorge invented a number of convenience Gradle tasks, which aren't clear. The key parts are this:

- [generateIdx](buildSrc/src/main/groovy/nextflow/plugins/generateIdx.groovy): scan sources and generate the extensions.idx resource, dunno what this does.
- [zipPlugin](buildSrc/src/main/groovy/nextflow/plugins/ZipPluginTask.groovy): compile and package your plugin in a zip file located at ready to be used by Nextflow which will be located at build/plugin/${pluginName}-${version}.zip
- [unzipPlugin](buildSrc/src/main/groovy/nextflow/plugins/UnzipPluginTask.groovy): extract the zip file created by zipPlugin into your $HOME/.nextflow/plugins so you can use it directly locally in your pipeline. This replaces the [`make install` that Ben uses in nf-boost](https://github.com/bentsherman/nf-boost/blob/c734f7490abcf81e136942443d80bd7f38d45a21/Makefile#L46-L49)
- [jsonPlugin](buildSrc/src/main/groovy/nextflow/plugins/jsonPluginTask.groovy) create the json spec required by nextflow to publish a plugin. It will be located at ./build/plugin/${pluginName}-${version}-meta.json and which can be added to the [Nextflow plugins JSON](https://github.com/nextflow-io/plugins/blob/main/plugins.json).
- clean: Clearup old resources

To make life easier I made a simple Makefile which runs these tasks for you. It's very similar to the ones in nf-hello.

- `make clean`: Clean task to remove build artifacts
- `make build`: Creates the plugin zip file
- `make test`: Builds the plugin zip and runs all tests
- `make install`: Installs the plugin locally by:
  - Removes any existing plugin version from ~/.nextflow/plugins
  - Extracts the plugin using Gradle unzipPlugin task
  - Tests the plugin by running a sample Nextflow script with the plugin enabled

## Docs

The template comes with a build the docs style page in Java. Not sure I like it but it's pre-built. You have to enable docs from Github Actions in your Github repo settings. I would prefer to switch this to markdown if possible.