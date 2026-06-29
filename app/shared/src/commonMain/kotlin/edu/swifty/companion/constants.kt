package edu.swifty.companion

const val DOMAIN = "localhost"
const val PROTOCOL = "http"
const val FRONTEND_PORT = 8080
const val BACKEND_PORT = 8081
const val FRONTEND_BASE_URL = "${PROTOCOL}://${DOMAIN}:${FRONTEND_PORT}"
const val BACKEND_BASE_URL = "${PROTOCOL}://${DOMAIN}:${BACKEND_PORT}/api"
const val BACKEND_V1_URL = "$BACKEND_BASE_URL/v1"
const val INTRA_BASE_URL = "https://api.intra.42.fr"
const val INTRA_OAUTH_URL = "$INTRA_BASE_URL/oauth"
const val INTRA_CLIENT_ID = "u-s4t2ud-be87d58d986a7efd3b69129275b526ae0b01a1702433e9d4f592a625e3dab8ff"
const val REDIRECT_URL = "${PROTOCOL}://${DOMAIN}:${BACKEND_V1_URL}/callback"