package link.standen.michael.phonesaver.util

import android.content.Context

import java.io.FileNotFoundException
import java.io.IOException

/**
 * A helper class for managing JSON files
 */
object JsonFileHelper {

	private fun getLogger(context: Context) = DebugLogger(context, JsonFileHelper::class.java.simpleName)

	/**
	 * Saves JSON to a file.
	 * @return True if the write was successful, false otherwise.
	 */
	fun saveJsonToFile(context: Context, json: String, filename: String): Boolean {
		try {
			val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
			fos.write(json.toByteArray())
			fos.flush()
			fos.close()
			return true
		} catch (e: IOException) {
			getLogger(context).e("Error writing JSON", e)
		}

		return false
	}

	/**
	 * Loads JSON from a file.
	 */
	fun getJsonFromFile(context: Context, filename: String): String? {
		var json: String? = null

		val log = getLogger(context)

		try {
			val fis = context.openFileInput(filename)

			val stringBuff = StringBuffer("")
			val buff = ByteArray(1024)
			var n = fis.read(buff)
			while (n != -1) {
				stringBuff.append(String(buff, 0, n))
				n = fis.read(buff)
			}
			fis.close()

			json = stringBuff.toString()
		} catch (e: FileNotFoundException) {
			log.e(String.format("JSON file %s not found", filename), e)
		} catch (e: IOException) {
			log.e(String.format("Error reading file %s", filename), e)
		}

		return json
	}
}
