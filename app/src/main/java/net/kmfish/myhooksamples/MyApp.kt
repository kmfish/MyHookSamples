package net.kmfish.myhooksamples

import android.app.Application
import net.kmfish.myhooksamples.hook.ActivityThreadHook

/**
 * Time:2022/2/14 5:55 下午
 * Author: lijun3
 * Description:
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ActivityThreadHook.hookActivityThread()
        var r1 = R.layout.activity_main
    }
}