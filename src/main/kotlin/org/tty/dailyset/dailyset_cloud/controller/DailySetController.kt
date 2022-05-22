package org.tty.dailyset.dailyset_cloud.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.UserState
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.bean.req.DailySetSubmitReq
import org.tty.dailyset.dailyset_cloud.bean.req.DailySetUpdateReq
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.service.DailySetService

@RestController
class DailySetController {

    @Autowired
    private lateinit var dailySetService: DailySetService

    @Autowired
    private lateinit var intentFactory: IntentFactory

    @PostMapping("/dailyset/info")
    suspend fun dailySetInfo(userState: UserState?): Responses<List<DailySet>> {
        requireNotNull(userState)
        requireNotNull(userState.user)

        return dailySetService.getDailysetInfos(userState.user.uid)
    }

    @PostMapping("/dailyset/update")
    suspend fun dailySetUpdate(userState: UserState?, @RequestBody dailysetUpdateReq: DailySetUpdateReq): Responses<DailySetUpdateResult> {
        requireNotNull(userState)
        requireNotNull(userState.user)

        val intent = intentFactory.createDailySetUpdateIntent(userState.user.uid, dailysetUpdateReq)
        return Responses.ok(data = dailySetService.getUpdates(intent))
    }

    @PostMapping("/dailyset/submit")
    suspend fun dailySetSubmit(userState: UserState?, @RequestBody dailySetSubmitReq: DailySetSubmitReq): Responses<Unit> {
        requireNotNull(userState)
        requireNotNull(userState.user)

        val intent = intentFactory.createDailySetSubmitIntent(userState.user.uid, dailySetSubmitReq)
        return dailySetService.submitLocalChanges(intent)
    }


}