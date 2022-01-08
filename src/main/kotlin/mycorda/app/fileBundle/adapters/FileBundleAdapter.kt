package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.FileBundle

/**
 * The generic signature for any FileBundleAdapter
 */
interface FileBundleAdapter<T> {
    fun fromBundle(bundle: FileBundle): T
    fun toBundle(adapted: T): FileBundle
}