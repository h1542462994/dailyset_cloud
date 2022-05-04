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
     * ticketExist
     */
    const val ticketExist = 10010

    /**
     * ticketNotExist
     */
    const val ticketNotExist = 10011

}