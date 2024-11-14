package link.standen.michael.phonesaver_re.util

import android.annotation.SuppressLint
import android.app.Activity
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import link.standen.michael.phonesaver_re.R

/**
 * A helper class for managing dialog pop ups
 */
object DialogHelper {

	/**
	 * Shows the support dialog
	 */
	@SuppressLint("InflateParams")
	fun showSupportDialog(activity: Activity) = AlertDialog.Builder(activity).apply {
		val view = activity.layoutInflater.inflate(R.layout.support_dialog, null)
		setView(view)
		setPositiveButton(android.R.string.ok) { dialog, _ ->
			dialog.cancel()
		}
		val pop = create()

		// Linkify
		view.findViewById<TextView>(R.id.support_link).apply {
			Linkify.addLinks(this, Linkify.ALL)
			movementMethod = LinkMovementMethod.getInstance()
		}
		pop.show()
	}
}
