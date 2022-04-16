package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.resp.UserRegisterResp
import org.tty.dailyset.dailyset_cloud.intent.UserRegisterIntent
import org.tty.dailyset.dailyset_cloud.mapper.SysEnvMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserMapper
import org.tty.dailyset.dailyset_cloud.service.EncryptProvider

@Service
class UserService {

    @Autowired
    private lateinit var sysEnvMapper: SysEnvMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var encryptProvider: EncryptProvider

    fun register(intent: UserRegisterIntent): Responses<UserRegisterResp> {
        val sysEnv = sysEnvMapper.get()
        val nextUidGenerate = sysEnv.nextUidGenerate
        val encryptPassword = encryptProvider.encrypt(nextUidGenerate.toString(), intent.password)

        val result = userMapper.addUser(nextUidGenerate, intent.nickname, intent.email, encryptPassword, intent.portraitId)
        return if (result >= 0) {
            sysEnvMapper.setUid(nextUidGenerate + 1)
            Responses.ok(data = UserRegisterResp(nextUidGenerate, intent.nickname, intent.email, intent.portraitId))
        } else {
            Responses.fail()
        }
    }







}