# File Bundle

[home](../README.md)

```kotlin
// create a bundle using the builder
val id = UniqueId.randomUUID()
val bundle = FileBundleBuilder()
    .withName("HelloWorldBundle")
    .withId(id)
    .addItem(TextBundleItem("greeting.txt", "Hello, world"))
    .addItem(BinaryBundleItem("greeting.bin", "Hello, world".toByteArray()))
    .build()

// or by scanning a directory 
val bundle = ScanDirectoryBuilder()
    .withId(id)
    .withName("My Bundle Name")
    .withBaseDirectory("/mydirectory")
    .build()


// convert it to a text file for storage / serialisation  
val textAdapter = TextAdapter()
val bundleAsText = textAdapter.fromBundle(bundle)

// convert back to a directory of files
val filesAdapter = FilesAdapter("/some/place/to/store/the/bundle")
val bundleAsFiles = filesAdapter.fromBundle(bundle)

```