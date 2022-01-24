# File Bundle

[![Circle CI](https://circleci.com/gh/mycordaapp/file-bundle.svg?style=shield)](https://circleci.com/gh/mycordaapp/file-bundle)
[![Licence Status](https://img.shields.io/github/license/mycordaapp/file-bundle)](https://github.com/mycordaapp/file-bundle/blob/master/licence.txt)

## What it does?

The motivation behind the `File Bundle` is to provide a consistent and simple abstraction for managing sets of
configuration / metadata files needed by most devops tool and applications. So for `K8s` the bundle would hold raw K8
manifests, helm charts and so on. And for `Terraform` it would hold the set of terraform templates.

Using the `File Bundle` the consumer is abstracted from the underlying storage, making it simpler to build and maintain
a consistent bundle on cloud scale systems.

Please note that this in NOT a generalised file store. There are some important limitations:

* the size of a bundle is limited (limit to be defined, assume  10MB for now)
* the bundle can text files and binary files
* persistence is at a bundle level
* unix executable flag (`chmod +x`) is preserved

A set of [adapters](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))
can convert the bundle to and from other formats for persistence. For example:

* `TextAdapter` converts a format that holds the content as a single human ready text string
* `FilesAdapter` converts to and from a list of Java File objects.


## Dependencies

As with everything in [myCorda dot App](https://mycorda.app), this library has minimal dependencies:

* Kotlin 1.4
* Java 11
* The object [Registry](https://github.com/mycordaapp/registry#readme)
* The [Commons](https://github.com/mycordaapp/commons#readme) module
* The [Simple KV Store(sks)](https://github.com/mycordaapp/simple-kv-store#readme) module

## Adding as a dependency

Maven jars are deployed using [JitPack](https://jitpack.io/).
See [releases](https://github.com/mycordaapp/file-bundle/releases) for version details.

```groovy
//add jitpack repo
maven { url "https://jitpack.io" }

// add dependency 
implementation "com.github.mycordaapp:file-bundle:<release>"
```

_JitPack build status is at https://jitpack.io/com/github/mycordaapp/file-bundle/$releaseTag/build.log_

