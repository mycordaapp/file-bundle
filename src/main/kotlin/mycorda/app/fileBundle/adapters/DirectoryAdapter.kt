package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.FileBundle
import java.io.File

class DirectoryAdapter : FileBundleAdapter<List<File>> {
    override fun fromBundle(bundle: FileBundle): List<File> {
        TODO("Not yet implemented")
    }

    override fun toBundle(adapted: List<File>): FileBundle {
        TODO("Not yet implemented")
    }
}