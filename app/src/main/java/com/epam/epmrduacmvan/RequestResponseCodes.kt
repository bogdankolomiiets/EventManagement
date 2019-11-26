package com.epam.epmrduacmvan

class RequestResponseCodes {
    companion object {
        const val CODE_SENT_OK = 2000
        const val RESPONSE_BODY_TO_JSON_FAIL = 2001
        const val PASSCODE_REMOVED = 2002
        const val PASSCODE_SET = 2003
        const val EMAIL_VERIFIED = 2004
        const val OK_200 = 200
        const val BAD_REQUEST_400 = 400
        const val NOT_FOUND_404 = 404
        const val INTERNAL_SERVER_ERROR_500 = 500
    }
}