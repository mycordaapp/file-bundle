# File Bundle
[home](../README.md)



```kotlin
// create a bundle using the builder
val id = UniqueId.randomUUID()
val bundle =  FileBundleBuilder()
            .withName("HelloWorldBundle")
            .withId(id)
            .addItem(TextBundleItem("greeting.txt", "Hello, world"))
            .addItem(BinaryBundleItem("greeting.bin", "Hello, world".toByteArray()))
            .build()


val textAdapter = TextAdapter()
val bundleAsText = textAdapter.fromBundle(bundle)

val filesAdapter = FilesAdapter("/some/place/to/store/the/bundle")
val bundleAsFiles = filesAdapter.fromBundle(bundle)

```