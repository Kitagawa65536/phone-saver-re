package link.standen.michael.phonesaver.activity

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import link.standen.michael.phonesaver.R

/**
 * A Preference Activity that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 */
class SettingsActivity: AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		setContentView(R.layout.settings_activity)
	}

	class PhoneSaverPreferenceFragment: PreferenceFragmentCompat() {
		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			setHasOptionsMenu(true)
		}

		override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
			setPreferencesFromResource(R.xml.preferences, rootKey)

			// Bind the summaries of file exists preferences to their value summary.
			val fileExistsPref = findPreference<Preference>("file_exists")
			fileExistsPref!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
				updateListPreferenceSummary(R.array.pref_list_description_file_exists, preference, newValue.toString())
			}
			updateListPreferenceSummary(R.array.pref_list_description_file_exists, fileExistsPref)

			// Bind the summaries of log to user preferences to their value summary.
			val logToUserPref = findPreference<Preference>("log_to_user")
			logToUserPref!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
				updateListPreferenceSummary(R.array.pref_list_description_log_to_user, preference, newValue.toString())
			}
			updateListPreferenceSummary(R.array.pref_list_description_log_to_user, logToUserPref)

			// Block the location select feature by API level
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
				with (findPreference<Preference>("location_select")){
					this?.isEnabled = false
					this?.setTitle(R.string.pref_title_location_select_unavailable)
					this?.setSummary(R.string.pref_description_location_select_unavailable)
				}
			}
		}

		override fun onOptionsItemSelected(item: MenuItem): Boolean {
			val id = item.itemId
			if (id == android.R.id.home) {
				activity?.onBackPressed()
				return true
			}
			return super.onOptionsItemSelected(item)
		}

		private fun updateListPreferenceSummary(descriptionArray: Int, preference: Preference, newValue: String? = null): Boolean {
			with (preference as ListPreference) {
				val index = findIndexOfValue(newValue?: value.toString())
				// Set the summary to reflect the new value.
				summary = if (index >= 0) context.resources.getStringArray(descriptionArray)[index] else null
			}
			return true
		}
	}
}
