package org.tty.dailyset.dailyset_cloud.bean.enums

/**
 * environment profile
 */
enum class Profile {
    /**
     * dev environment
     */
    DEV,

    /**
     * pre environment
     */
    PRE,

    /**
     * production environment
     */
    PROD;

    fun isDev(): Boolean {
        return this == DEV
    }
}
