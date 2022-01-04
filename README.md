# File Bundle

[![Circle CI](https://circleci.com/gh/mycordaapp/file-bundle.svg?style=shield)](https://circleci.com/gh/mycordaapp/file-bundle)
[![Licence Status](https://img.shields.io/github/license/mycordaapp/file-bundle)](https://github.com/mycordaapp/file-bundle/blob/master/licence.txt)

## What it does?

TODO !!! 


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

