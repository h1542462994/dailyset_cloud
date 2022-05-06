package org.tty.dailyset.dailyset_cloud.http

import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.http.req.DailySetInfosReqUnic
import org.tty.dailyset.dailyset_cloud.http.req.DailySetUpdateReqUnic
import org.tty.dailyset.dailyset_cloud.http.resp.DailySetUpdateRawResult
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * retrofit api.
 */
interface DailySetUnicApi {
    @POST("/dailyset/info")
    suspend fun dailySetInfos(@Body dailySetInfoReqUnic: DailySetInfosReqUnic): Responses<List<DailySet>>

    @POST("/dailyset/update")
    suspend fun dailySetUpdate(@Body dailySetUpdateReqUnic: DailySetUpdateReqUnic): Responses<DailySetUpdateRawResult>
}