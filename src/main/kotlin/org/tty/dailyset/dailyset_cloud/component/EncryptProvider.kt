package org.tty.dailyset.dailyset_cloud.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.util.byte2Hex
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Component
class EncryptProvider {
    @Autowired
    private lateinit var environmentVars: EnvironmentVars

    private fun sha256(text: String): String {
        val messageDigest: MessageDigest
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(text.toByteArray(StandardCharsets.UTF_8))
            return byte2Hex(messageDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun encrypt(uid: String, password: String): String {
        return sha256(uid + environmentVars.encryptSalt + password)
    }


}