package link.standen.michael.phonesaver_re.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView

import link.standen.michael.phonesaver_re.R
import java.util.Locale
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import link.standen.michael.phonesaver_re.util.DebugLogger

/**
 * Credits activity.
 */
class CreditsActivity : AppCompatActivity() {

	private val defaultLocale = Locale("en").language

	private lateinit var log: DebugLogger

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.credits_activity)

		log = DebugLogger(this)

		// Version
		try {
			val versionName = packageManager.getPackageInfo(packageName, 0).versionName
			findViewById<TextView>(R.id.credits_version).text = resources.getString(R.string.credits_version, versionName)
		} catch (e: PackageManager.NameNotFoundException) {
			log.e("Unable to get package version", e)
		}

		// Linkify
		findViewById<TextView>(R.id.credits_creator).movementMethod = LinkMovementMethod.getInstance()
		findViewById<TextView>(R.id.credits_content1).movementMethod = LinkMovementMethod.getInstance()
		findViewById<TextView>(R.id.credits_content2).movementMethod = LinkMovementMethod.getInstance()
		findViewById<TextView>(R.id.credits_content3).movementMethod = LinkMovementMethod.getInstance()
		if (getCurrentLocale().language == defaultLocale){
			// English, hide the translator info
			findViewById<TextView>(R.id.credits_content_translator).visibility = View.GONE
		} else {
			findViewById<TextView>(R.id.credits_content_translator).movementMethod = LinkMovementMethod.getInstance()
		}
	}

	/**
	 * A version safe way to get the currently applied locale.
	 */
	private fun getCurrentLocale(): Locale =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			this.resources.configuration.locales.get(0)
		} else {
			@Suppress("DEPRECATION")
			this.resources.configuration.locale
		}
}
