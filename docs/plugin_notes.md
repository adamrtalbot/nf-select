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
