# ddfadm

Portable jvm based replacement for [ddf-boot](https://github.com/oconnormi/dev-tools/) and [docker-ddf-base](https://github.com/oconnormi/docker-ddf-base)
_*WARNING:*
_This is still heavily a work in progress!!_

## Background

A couple years back I started a small project called `ddf-base` with the goal of supporting ddf based distributions within docker containers.
This project was done entirely in bash scripts and while initially small and simple has grown in complexity. The scripts contained in the project have grown in number and testing is entirely manual (writing tests for these scripts proved to be a lot of work and so I never really got far).

The `ddf-base` project also grew to support a number of downstream custom ddf distributions, so the challenge of maintaining the scripts and adding new features while maintaining downstream compatiblity has grown. 

At its core the `ddf-base` project aims to solve the most common and pressing steps for bringing up a ready to use ddf based system, which includes

* Generating temporary certificates when necessary
  * Handle local demoCA
  * Handle remote CFSSL based CA
* Updating system properties with host specific values
* Configuring solr and solr cloud connections
* Configure ldap connection
* Install/Uninstall Features
* Install profiles
* Pre-load content and metadata (for testing purposes)
* Configure CDM
* Configure IDP client

The `ddf-boot` script in [dev-tools](https://github.com/oconnormi/dev-tools) is capable of a smaller subset of these tasks but is somewhat version aware (something that `ddf-base` is lacking)


## Initial Goals

As the usage of the `ddf-base` docker image has grown its time for a more robust solution!
This project may only be the first pass at handling the automation tasks of bootstrapping a ddf system. As such its current goal is relatively simple, reach full parity with `ddf-base`, and the `ddf-boot` shell script from my `dev-tools` repository. Simple!

## The Future!

Once full parity is reached then some newer features should be added.

### More Supported Configurations
* Handle multiple versions for all options
  * Experiment building configuration models for features by examining the metatype contained in the current version
* Handle configuration of federated sources
* Handle configuration of federation strategies (fanout, etc)
* And probably many more...


### Multiple Configuration Formats

Currently `ddf-base` exclusively uses environment variables as a means of configuration input. This model should still be supported (and maintained backwards compatible for a while) but will probably need to use a better naming strategy going forward.

The existing shell script version of `ddf-boot` on the other hand exclusively uses command line options. I would like to eventually support this alongside environment variables.

The end result may end up being a tool that can be configured via a file (possibly `yaml`, `toml`, whatever) but also in combination with cli flags and environment variables.

### Tests, tests, tests

Finally everything *must* be tested, manual testing makes me sleepy :-)
