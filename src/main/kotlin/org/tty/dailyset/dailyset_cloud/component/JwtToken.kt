/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.component

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.UserJwtDTO
import org.tty.dailyset.dailyset_cloud.bean.entity.User
import org.tty.dailyset.dailyset_cloud.bean.entity.UserActivity
import org.tty.dailyset.dailyset_cloud.component.EnvironmentVars
import java.lang.NullPointerException
import java.util.*

@Component
class JwtToken {
    @Autowired
    private lateinit var environmentVars: EnvironmentVars



    fun verify(token: String): UserJwtDTO? {
        try {
            val decodedJWT = JWT.require(Algorithm.HMAC256(environmentVars.jwtSecret)).build().verify(token)
            val issuedAt: Date = decodedJWT.issuedAt
            val expiredAt: Date = decodedJWT.expiresAt
            val audience = decodedJWT.audience
            val deviceCode = decodedJWT.getClaim(DEVICE_CODE).asString()
            if (audience.isEmpty()) {
                return null
            }
            val uid = audience[0]
            if (expiredAt.before(Date())) {
                return UserJwtDTO(uid.toInt(), expiredAt, issuedAt, false, deviceCode)
            }
            return UserJwtDTO(uid.toInt(), expiredAt, issuedAt, true, deviceCode)
        } catch (e: JWTDecodeException) {
            return null
        } catch (e: NullPointerException) {
            return null
        } catch (e: JWTVerificationException) {
            return null
        }
    }

    fun sign(user: User, userActivity: UserActivity): String {
        return JWT.create()
            .withExpiresAt(Date(System.currentTimeMillis().plus(environmentVars.jwtTokenExpireTime)))
            .withAudience(user.uid.toString())
            .withIssuer(environmentVars.jwtTokenIssuer)
            .withIssuedAt(Date())
            .withClaim(DEVICE_CODE, userActivity.deviceCode)
            .sign(Algorithm.HMAC256(environmentVars.jwtSecret))
    }

    companion object {
        const val DEVICE_CODE = "deviceCode"
    }
}