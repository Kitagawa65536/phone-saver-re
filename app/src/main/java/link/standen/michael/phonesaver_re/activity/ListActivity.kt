package link.standen.michael.phonesaver_re.activity

import android.os.Bundle
import android.widget.ListView
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity

/**
 * An activity for selecting a folder in the file system.
 */
abstract class ListActivity : AppCompatActivity() {

	companion object {
		const val LIST_STATE = "listState"
	}

	private var listState: Parcelable? = null

	override fun onResume() {
		super.onResume()

		// Restore the list view scroll location
		listState?.let { findViewById<ListView>(android.R.id.list).onRestoreInstanceState(listState) }
		listState = null
	}

	/**
	 * Load list view scroll position
	 */
	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		listState = savedInstanceState.getParcelable(LIST_STATE)
	}

	/**
	 * Save list view scroll position
	 */
	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		listState = findViewById<ListView>(android.R.id.list).onSaveInstanceState()
		outState.putParcelable(LIST_STATE, listState)
	}

}
