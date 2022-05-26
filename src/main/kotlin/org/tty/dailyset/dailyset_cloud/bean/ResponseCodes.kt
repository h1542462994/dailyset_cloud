/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.bean

object ResponseCodes {
    /**
     * success: 0
     */
    const val success = 0

    /**
     * fail: 1
     */
    const val fail = 1

    /**
     * argError: 2
     */
    const val argError = 2

    /**
     * userNotExist: 101
     */
    const val userNoExists = 101

    /**
     * passwordError: 102
     */
    const val passwordError = 102

    /**
     * tokenError: 103
     */
    const val tokenError = 103

    /**
     * deviceCodeError: 104
     */
    const val deviceCodeError = 104

    /**
     * secretError: 105
     */
    const val secretError = 105

    /**
     * ticketExist
     */
    const val ticketExist = 10010

    /**
     * ticketNotExist
     */
    const val ticketNotExist = 10011

    /**
     * unic server error
     */
    const val unicError = 10012

    /**
     * resource is not authed
     */
    const val resourceNoAuth = 106

    /**
     * resource is readOnly
     */
    const val resourceReadOnly = 107

    /**
     * resource is not exist
     */
    const val resourceNotExist = 108

}