package net.kmfish.myhooksamples.hook

import android.os.Build
import android.os.Handler
import android.os.Message
import net.kmfish.myhooksamples.util.MatrixLog
import java.lang.reflect.Field

/**
 * Time:2022/2/14 5:29 下午
 * Author: lijun3
 * Description:
 */
class ActivityThreadHook {


    companion object {
        const val TAG = "ActivityThreadHook"

        /**
         * hook 主线程里的mH 主handler，替换handler的mCallback对象，来监听到消息分发
         */
        fun hookActivityThread() {
            try {
                val forName = Class.forName("android.app.ActivityThread")
                val field: Field = forName.getDeclaredField("sCurrentActivityThread")
                field.isAccessible = true
                val activityThreadValue: Any? = field.get(forName)
                val mH: Field = forName.getDeclaredField("mH")
                mH.isAccessible = true
                val handler: Any = mH.get(activityThreadValue)
                val handlerClass: Class<*>? = handler.javaClass.superclass
                if (null != handlerClass) {
                    val callbackField: Field = handlerClass.getDeclaredField("mCallback")
                    callbackField.isAccessible = true
                    val originalCallback: Handler.Callback? = callbackField.get(handler) as Handler.Callback?
                    val callback = HackCallback(originalCallback)
                    callbackField.set(handler, callback)
                }
                MatrixLog.i(
                    TAG,
                    "hook system handler completed. SDK_INT:%s",
                    Build.VERSION.SDK_INT
                )
            } catch (e: Exception) {
                MatrixLog.e(TAG, "hook system handler err! %s", e.cause.toString())
            }
        }
    }

    class HackCallback(private val mOriginalCallback: Handler.Callback?) : Handler.Callback {

//        private val int LAUNCH_ACTIVITY = 100;
//        private val int CREATE_SERVICE = 114;
//        private val int RELAUNCH_ACTIVITY = 126;
//        private val int RECEIVER = 113;
//        private val int EXECUTE_TRANSACTION = 159; // for Android 9.0
//        private val isCreated = false;
//        private val hasPrint = Integer . MAX_VALUE;

//        private static final int SERIVCE_ARGS = 115;
//        private static final int STOP_SERVICE = 116;
//        private static final int STOP_ACTIVITY_SHOW = 103;
//        private static final int STOP_ACTIVITY_HIDE = 104;
//        private static final int SLEEPING = 137;

        override fun handleMessage(msg: Message): Boolean {
            MatrixLog.i(TAG, "HackHandleMsg: $msg")
            return mOriginalCallback?.handleMessage(msg) ?: false
        }
    }
}