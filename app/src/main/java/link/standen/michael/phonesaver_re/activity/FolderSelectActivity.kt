package link.standen.michael.phonesaver_re.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import android.widget.AdapterView
import java.io.File
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import link.standen.michael.phonesaver_re.R.*
import link.standen.michael.phonesaver_re.util.DebugLogger
import link.standen.michael.phonesaver_re.util.LocationHelper

/**
 * An activity for selecting a folder in the file system.
 */
class FolderSelectActivity : ListActivity() {

	companion object {
		const val FOLDER_SELECTED = "FolderSelected"
	}

	private lateinit var log: DebugLogger

	private var currentPath: String = Environment.getExternalStorageDirectory().absolutePath

	private lateinit var listView: ListView

	private var folderList: List<String> = ArrayList()

	private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		when (item.itemId) {
			id.navigation_here -> {
				// Call back to previous activity with the location
				val intent = Intent()
				intent.putExtra(FOLDER_SELECTED, currentPath)
				setResult(RESULT_OK, intent)
				finish()
				return@OnNavigationItemSelectedListener true
			}
		}
		false
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(layout.folder_select_activity)

		log = DebugLogger(this)

		// Init list view
		listView = findViewById(android.R.id.list)
		listView.onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
			with(view as TextView){
				if (view.text.toString() == resources.getString(string.back_folder)) {
					val back = getBackLocation()
					if (back == null) {
						cancelIntent()
					} else {
						currentPath = back
					}
				} else {
					currentPath += view.text.toString()
				}
				updateListView()
			}
		}

		// Init bottom buttons
		findViewById<BottomNavigationView>(id.navigation)
				.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
	}

	override fun onStart() {
		super.onStart()
		updateListView()
	}

	/**
	 * Update the folder list view
	 */
	private fun updateListView() {
		log.v("Path is: $currentPath")

		val currentFile = File(currentPath)

		val fList = currentFile.listFiles()
				// Directories only
				?.filter { it.isDirectory }
				// Get the file path
				?.map { removeCurrent(it.absolutePath) }
				// Sort it
				?.sortedBy { it.toLowerCase() }
				// Convert to mutable
				?.toMutableList()
				// Default to empty
				?: ArrayList()

		// Add back button as first item
		fList.add(0, resources.getString(string.back_folder))

		folderList = fList.toList()
		log.d("List length: "+folderList.size)

		// Set title
		this.title = LocationHelper.removeRoot(currentPath) + File.separatorChar

		listView.adapter = ArrayAdapter<String>(this, layout.saver_list_item, folderList)

		updateWritable(currentFile.canWrite())
	}

	/**
	 * Updates the menu items based on whether the directory is writable or not.
	 */
	private fun updateWritable(writable: Boolean){
		findViewById<View>(id.navigation_here).visibility = if (writable) View.VISIBLE else View.GONE
		findViewById<View>(id.navigation_read_only).visibility = if (writable) View.GONE else View.VISIBLE
	}

	/**
	 * Remove the current location from the given path.
	 */
	private fun removeCurrent(location: String): String =
		location.replace(currentPath, "")

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item?.itemId){
			android.R.id.home -> {
				// Back button
				onBackPressed()
				return true
			}

		}
		return super.onOptionsItemSelected(item)
	}

	/**
	 * Goes up a directory, unless at the top, then exits
	 */
	override fun onBackPressed() {
		val back = getBackLocation()
		if (back == null || currentPath == LocationHelper.rootLocation) {
			cancelIntent()
		} else {
			currentPath = back
			updateListView()
		}
	}

	/**
	 * Cancels the current activity
	 */
	private fun cancelIntent() {
		val intent = Intent()
		intent.putExtra(FOLDER_SELECTED, currentPath)
		setResult(RESULT_CANCELED, intent)
		finish()
	}

	/**
	 * Returns the back location for the current path
	 */
	private fun getBackLocation(): String? =
		if (currentPath.contains(File.separatorChar)) {
			currentPath.substring(0, currentPath.lastIndexOf(File.separatorChar))
		} else {
			null
		}

}
