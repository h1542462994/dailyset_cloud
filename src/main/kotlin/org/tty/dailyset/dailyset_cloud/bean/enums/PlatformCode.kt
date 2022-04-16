package org.tty.dailyset.dailyset_cloud.bean.enums

enum class PlatformCode(
    val code: Int
) {
    /**
     * portable device, like phone, watch..
     */
    PORTABLE(0),

    /**
     * pad device, like ipad, uwp device..
     */
    PAD(1),

    /**
     * pc device, like pc..
     */
    PC(2);

    companion object {
        fun of(code: Int): PlatformCode {
            return values().first { it.code == code }
        }
    }
}