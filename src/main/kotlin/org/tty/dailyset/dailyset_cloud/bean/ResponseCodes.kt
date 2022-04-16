package org.tty.dailyset.dailyset_cloud.bean

object ResponseCodes {
    /**
     * success: 0
     */
    val ResponseCodes.success: Int get() = 0

    /**
     * fail: 1
     */
    val ResponseCodes.fail: Int get() = 1

    /**
     * argError: 2
     */
    val ResponseCodes.argError: Int get() = 2

    /**
     * userNotExist: 101
     */
    val ResponseCodes.userNoExist: Int get() = 101

    /**
     * passwordError: 102
     */
    val ResponseCodes.passwordError: Int get() = 102

    /**
     * tokenError: 103
     */
    val ResponseCodes.tokenError: Int get() = 103

    /**
     * deviceCodeError: 104
     */
    val ResponseCodes.deviceCodeError: Int get() = 104
}