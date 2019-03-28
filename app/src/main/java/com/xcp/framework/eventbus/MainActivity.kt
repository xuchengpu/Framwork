package com.xcp.framework.eventbus

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.xcp.framework.R
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tv_content)
        MyEventBus.getDefault().register(this)
        textView!!.setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
    fun setText(messageBean: MessageBean) {
        Log.e("TAG", "setText==" + messageBean.message)
        textView!!.text = messageBean.message
    }

    override fun onDestroy() {
        super.onDestroy()
        MyEventBus.getDefault().unRegister(this)
    }
}
