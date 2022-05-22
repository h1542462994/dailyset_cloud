/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.service.*

/**
 * *intent* is the wrapped data transferred to the *service*
 */
interface RequestIntent

/**
 * *UserIntent* is all user related *intent* to the [UserService]
 * @see UserService
 */
interface UserIntent: RequestIntent




