
config ?= compileClasspath
version ?= $(shell grep 'version' gradle.properties | cut -d'=' -f2)

clean:
	./gradlew clean

build:
	./gradlew zipPlugin

test:
	./gradlew zipPlugin
	./gradlew test

SHELL := /bin/bash
install:
	rm -rf ${HOME}/.nextflow/plugins/nf-select-${version}
	./gradlew unzipPlugin
	nextflow run src/test/nextflow/main.nf -c <(echo "plugins { id 'nf-select@${version}' }")

uninstall:
	rm -rf ${HOME}/.nextflow/plugins/nf-select-${version}