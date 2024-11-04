# nf-select: A Nextflow plugin for selecting files

nf-select is a Nextflow plugin that provides functionality for pulling strings out of lists. It's main use is to select tools to enable or disable particular tools but it could do anything else. For example use cases, you can see:

- nf-canary which enables or disables tests based on the parameter `run` or `skip`
- Sarek which enables or disables tools based on `--tools` and `--skip_tools`.

This plugin was made as a learning exercise, but it could be useful for others. If so I will contribute it to nextflow-io/plugins.

## Installation

Currently the plugin is in dev mode, so the easiest method is to clone the repo and run `./gradlew unzipPlugin` in the root of the project. This will build and copy the plugin to the Nextflow plugins directory. You will need to explicitly specify the correct version of the plugin (currently `v0.0.1dev`) in your `nextflow.config` or command line (see usage). For further information see the development guidance.

Alternatively, you can set the `NXF_PLUGINS_TEST_REPOSITORY` environment variable to point to the JSON file created as part of the build process.

## Usage

Currently, the plugin contains a few small functions:

- `checkInParam`: A method for checking if a single string is within a list of strings. e.g.:

```groovy
params.tools = 'foo,bar,baz'
checkInParam(params.tools, 'foo') // true
checkInParam(params.tools, 'qux') // false

params.skip_tools = 'qux'
checkInParam(params.skip_tools, 'qux') // true
```

It also contains a `defaultChoice` parameter for setting a value if the checkValue is not set. It must be boolean and by default is `false`:

```groovy
checkInParam(params.tools, null, defaultChoice = true) // true
```

Finally it includes a `separator` parameter for setting the separator for the list of strings. By default it is a comma:

```groovy
params.tools = "foo|bar|baz"
checkInParam(params.tools, "foo", defaultChoice = false, separator = "|")
```

In addition to `checkInParam` there is an alias `checkInObject` which just is a more accurate name for the same thing.

Furthermore, nf-select contains an additional function:

- `containsIgnoreCase`: A method for checking if a single string is within an array or a single string. e.g.:

```groovy
containsIgnoreCase('foo,bar,baz', 'foo') // true
containsIgnoreCase('foo'        , 'foo') // true
containsIgnoreCase('bar,baz'    , 'foo') // true
```

## Development

### Setup Development Environment

1. Clone the repository:

```bash
git clone https://github.com/adamrtalbot/nf-select.git
cd nf-select
```

2. Check it builds:

- Build the plugin:

```bash
make build
```

3. Develop as normal. Here is a brief description of the useful files:

- [SelectFunctions.groovy](src/main/groovy/com/nextflow/plugin/SelectFunctions.groovy): The main functions of the plugin. This is probably the file you want to modify.
- [SelectPlugin.groovy](src/main/groovy/com/nextflow/plugin/SelectPlugin.groovy): The main plugin initialisation file.
- [ExampleFunctionsTest.groovy](./src/test/groovy/com/nextflow/plugin/ExampleFunctionsTest.groovy): Tests for the function. Think of these as your "Groovy" unit tests.
- [PluginTest.groovy](./src/test/groovy/com/nextflow/plugin/PluginTest.groovy): Plugin tests which write temporary Nextflow and run them. Jorge has made a pretty cool mock of Nextflow which means you don't need 1 million Nextflow scripts and it's very ligthweight. This is the "Nextflow" unit tests.

4. Run tests:

```bash
make test
```

5. Install locally for testing:

```bash
make install
```

### Creating a Release

Adapted from [https://github.com/stevekm/nf-niceutils/blob/main/NOTES.md](https://github.com/stevekm/nf-niceutils/blob/main/NOTES.md)

- Update the `gradle.properties` file
- On Github actions, run the release pipeline and set the correct version
- Double check that the details are right

```json
{
    "version": "0.0.1dev",
    "date": "2024-11-03T19:26:25.767304Z",
    "url": "https://github.com/adamrtalbot/nf-select/releases/download/0.0.1dev/nf-select-0.0.1dev.zip",
    "requires": ">=22.10.0",
    "sha512sum": "b2d269eedbc1722089e8585cde2db998cb52e8981ae5a4c5b45a596a2f4565e5ca65a38157ae7d765c585274bbb13dd652b31e5f3769221c06f286131b333056"
}
```

- Grab the JSON and add it to the [Nextflow Plugins Registry](https://github.com/nextflow-io/plugins/) or your private fork, my fork of the registry is here https://github.com/adamrtalbot/plugin

To test it out, on another system without the plugin installed (but this repo cloned), run this

```bash
VERSION=$(grep 'version' gradle.properties | cut -d'=' -f2) NXF_PLUGINS_TEST_REPOSITORY=https://raw.githubusercontent.com/adamrtalbot/plugins/refs/heads/main/plugins.json 
nextflow run src/test/nextflow/main.nf -c <(echo "plugins { id 'nf-select@${VERSION}' }")
```

## Extras

If something goes wrong, try this and start again

```bash
./gradlew clean
./gradlew build
rm -rf .gradle
```

## Contributing

This is pretty janky, so I welcome any help.

1. Fork the repository
2. Create a feature branch
3. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

* Create an issue: https://github.com/adamrtalbot/nf-select/issues
* Documentation: https://adamrtalbot.github.io/nf-select
