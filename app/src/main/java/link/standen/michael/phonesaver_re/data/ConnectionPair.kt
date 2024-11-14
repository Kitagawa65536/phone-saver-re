package link.standen.michael.phonesaver_re.data

import java.net.HttpURLConnection

/**
 * A data class for holding a url and connection
 */
data class ConnectionPair(val url: String, val conn: HttpURLConnection? = null)
