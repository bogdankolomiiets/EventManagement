package com.epam.epmrduacmvan

class Constants {
    companion object {
        /* alternative email regex
        *val EMAIL_PATTERN_V1: Regex = Regex("^([\\w.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)\$")
        * */

        // email regex pattern from
        // https://stackoverflow.com/questions/201323/how-to-validate-an-email-address-using-a-regular-expression
        val EMAIL_PATTERN_V2: Regex = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
        const val YOUTUBE_LINK_PATTERN = ("^.*(?:(?:youtu\\.be\\/|v\\/|vi\\/|u\\/\\w\\/|embed\\/)|(?:(?:watch)?\\?v(?:i)?=|\\&v(?:i)?=))([^#\\&\\?]*).*")
        const val EMAIL: String = "EMAIL"
        const val CODE_LENGTH: Int = 4
        const val PASS_CODE: String = "PASS_CODE"
        const val EMPTY_PASSCODE: String = "0000"
        const val USER_TOKEN: String = "USER_TOKEN"
        const val TOKEN_SIGN: String = "Bearer_"
        const val TOKEN_HEADER_NAME: String = "Authorization"
        const val SHARED_PREF: String = "SHARED_PREF"
        const val USER_NEW_PASSCODE: String = "USER_NEW_PASSCODE"
        const val WITHOUT_PASSCODE: Int = -1
        const val NO_INFORMATION: Int = 0
        const val WITH_PASSCODE: Int = 1
        const val BOOL_EXTRA: String = "BOOL_EXTRA"
        const val FEATURED_EVENT_COUNT: Int = 3
        const val CITY: String = "CITY"
        const val CATEGORY: String = "CATEGORY"
        const val EVENT: String = "EVENT"
        const val EVENT_ID: String = "EVENT_ID"
        const val ROUND_ICONS_WIDTH: Int = 90
        const val ROUND_ICONS_HEIGHT: Int = 90
        const val YOUTUBE_API_KEY = "AIzaSyAUTS6Yi_GPCNeaJ0ODCnAX-K7U3YTkHLc"
        const val YOUTUBE_REQUEST_PART = "snippet"
    }
}