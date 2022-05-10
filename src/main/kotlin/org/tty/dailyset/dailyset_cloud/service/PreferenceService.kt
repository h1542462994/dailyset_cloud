@file:Suppress("PropertyName")

package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.tty.dailyset.dailyset_cloud.bean.enums.PreferenceName
import org.tty.dailyset.dailyset_cloud.mapper.PreferenceMapper

@Service
class PreferenceService {

    @Autowired
    private lateinit var preferenceMapper: PreferenceMapper

    @Suppress("SameParameterValue")
    private fun getValueOrDefault(preferenceName: PreferenceName): String {
        val default = preferenceName.defaultValue
        val preference = preferenceMapper.getPreference(preferenceName.value)
        val useDefault = preference == null || preference.useDefault
        return checkNotNull(if (useDefault) default else preference?.value)
    }

    @Suppress("SameParameterValue")
    private fun setValue(preferenceName: PreferenceName, value: String): Boolean {
        val preference = preferenceMapper.getPreference(preferenceName.value)
        val result: Int = if (preference == null) {
            preferenceMapper.addPreference(preferenceName.value, false, value)
        } else {
            preferenceMapper.setPreference(preferenceName.value, value)
        }
        return result > 0
    }

    var nextUidGenerate: Int = 0
        get() = getValueOrDefault(PreferenceName.NEXT_UID_GENERATE).toInt()
        set(value) {
            field = value
            setValue(PreferenceName.NEXT_UID_GENERATE, value.toString())
        }



}