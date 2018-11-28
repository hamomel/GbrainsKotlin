package com.geekbrains.geekbrainskotlin.ui.splash

import com.geekbrains.geekbrainskotlin.R
import com.geekbrains.geekbrainskotlin.ui.base.BaseActivity
import com.geekbrains.geekbrainskotlin.ui.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<Boolean>() {

    override val model: SplashViewModel by viewModel()

    override val layoutRes: Int = R.layout.activity_splash

    override fun onResume() {
        super.onResume()
        model.requestUser()
    }

    override fun renderData(data: Boolean) {
        if (data) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}