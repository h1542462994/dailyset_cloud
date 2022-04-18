/**
 * create at 2022/4/17
 *
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.auth

/**
 * used on controller function, api can be visited by anonymous.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Anonymous()
