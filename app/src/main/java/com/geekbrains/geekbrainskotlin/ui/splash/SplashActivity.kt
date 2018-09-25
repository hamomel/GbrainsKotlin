package com.geekbrains.geekbrainskotlin.ui.splash

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Handler
import com.firebase.ui.auth.AuthUI
import com.geekbrains.geekbrainskotlin.R
import com.geekbrains.geekbrainskotlin.ui.base.BaseActivity
import com.geekbrains.geekbrainskotlin.ui.main.MainActivity

private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override val layoutRes: Int = R.layout.activity_splash

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf{ it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}