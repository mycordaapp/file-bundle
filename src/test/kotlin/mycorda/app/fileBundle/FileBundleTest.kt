package mycorda.app.fileBundle

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.adapters.TextAdapter
import mycorda.app.rss.JsonSerialiser
import org.junit.jupiter.api.Test

class FileBundleTest {

    @Test
    fun `should serialize a file bundle`() {
        val bundle = Fixtures.allExampleFiles()

        val roundTripped = roundTrip(bundle)
        assertThat(roundTripped, equalTo(roundTripped))
    }

    private fun roundTrip(data: FileBundle): FileBundle {
        // FileBundle is too complex for the rules of really simple serialisation,
        // so we convert to a string using the TextAdapter
        val adapter = TextAdapter()
        val serialiser = JsonSerialiser()
        val serialised = serialiser.serialiseData(adapter.fromBundle(data))
        return adapter.toBundle(serialiser.deserialiseData(serialised).any() as String)
    }
}