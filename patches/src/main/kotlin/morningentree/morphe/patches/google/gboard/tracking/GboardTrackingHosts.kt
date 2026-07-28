package morningentree.morphe.patches.google.gboard.tracking

/**
 * Curated blocklist of tracking / analytics / ads hosts, baked into the repo so the Gboard
 * tracking-blocker patch needs no user-supplied file. Edit freely — one host per line, `#` comments
 * allowed. Wildcard matching means "example.com" also covers "sub.example.com".
 */
internal const val GBOARD_TRACKING_HOSTS = """
    federatedcompute-pa.googleapis.com

    # --- Firebase / Google analytics & measurement ---
    app-measurement.com
    firebaselogging-pa.googleapis.com
    firebaselogging.googleapis.com
    google-analytics.com
    ssl.google-analytics.com
    www.google-analytics.com
    analytics.google.com
    firebase-settings.crashlytics.com
    crashlyticsreports-pa.googleapis.com

    # --- Google ads ---
    googleads.g.doubleclick.net
    ad.doubleclick.net
    pagead2.googlesyndication.com
    www.googleadservices.com
    admob.com
    admob-gmats.uc.r.appspot.com

    # --- Third-party analytics (rarely in Gboard; kept for completeness) ---
    api.mixpanel.com
    graph.facebook.com
"""
