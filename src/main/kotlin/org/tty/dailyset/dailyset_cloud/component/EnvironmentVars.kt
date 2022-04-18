/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.component

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.get
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.enums.Profile
import javax.annotation.PostConstruct

@Suppress("PropertyName")
@Component
class EnvironmentVars {

    @Autowired
    private lateinit var context: ConfigurableApplicationContext

    private lateinit var _defaultPortraitFileName: String
    private lateinit var _portraitStorage: String
    private lateinit var _encryptSalt: String
    private lateinit var _jwtSecret: String
    private lateinit var _jwtTokenExpireTime: String
    private lateinit var _jwtTokenIssuer: String
    private lateinit var _profile: String

    private val logger = LoggerFactory.getLogger(EnvironmentVars::class.java)

    /**
     * begin uid: 100001
     */
    val beginUid = 100001

    /**
     * defaultPortraitFileName: `dailyset.env.default_portrait`
     */
    val defaultPortraitFileName: String get() = _defaultPortraitFileName

    /**
     * storage to save portrait: `dailyset.env.data.storage.portrait`
     */
    val portraitStorage: String get() = _portraitStorage

    /**
     * encrypt salt for encryption: `dailyset.env.encrypt.salt`
     */
    val encryptSalt: String get() = _encryptSalt

    /**
     * jwt secret: `dailyset.env.jwt.secret`
     */
    val jwtSecret: String get() = _jwtSecret

    val jwtTokenExpireTime: Int get() = _jwtTokenExpireTime.toInt()

    val jwtTokenIssuer: String get() = _jwtTokenIssuer

    val profile: Profile get() = when (_profile) {
        "prod" -> {
            Profile.PROD
        }
        "pre" -> {
            Profile.PRE
        }
        else -> {
            Profile.DEV
        }
    }


    @PostConstruct
    fun init() {
        _defaultPortraitFileName = context.environment["dailyset.env.default_portrait"] ?: ""
        _portraitStorage = context.environment["dailyset.env.data.storage.portrait"] ?: ""
        _encryptSalt = context.environment["dailyset.env.encrypt.salt"] ?: ""
        _jwtSecret = context.environment["dailyset.env.jwt.secret"] ?: ""
        _jwtTokenExpireTime = context.environment["dailyset.env.jwt.token_expire_time"] ?: "43200000"
        _jwtTokenIssuer = context.environment["dailyset.env.jwt.token_issuer"] ?: ""
        _profile = context.environment.activeProfiles.firstOrNull() ?: ""

        if (_defaultPortraitFileName.isEmpty()) {
            logger.error("App/ Environment variable (dailyset.env.default_portrait) is empty.")
        }
        if (_portraitStorage.isEmpty()) {
            logger.error("App/ Environment variable (dailyset.env.data.storage.portrait) is empty.")
        }
        if (_encryptSalt.isEmpty()) {
            logger.error("App/ Environment variable (dailyset.env.encrypt.salt) is empty.")
        }
        if (_jwtSecret.isEmpty()) {
            logger.error("App/ Environment variable (dailyset.env.jwt.secret) is empty.")
        }
    }
}