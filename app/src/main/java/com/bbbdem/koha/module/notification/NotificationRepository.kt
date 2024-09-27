package com.bbbdem.koha.module.notification

import com.bbbdem.koha.module.notification.model.NotificationRequestModel
import com.bbbdem.koha.network.RetrofitInstanceForGlobal


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class NotificationRepository {
    suspend fun getNotification(notificationRequestModel: NotificationRequestModel) = RetrofitInstanceForGlobal.apiService?.getNotification(notificationRequestModel)
}