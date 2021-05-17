package vn.teko.cart.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SettingActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        findViewById<RadioGroup>(R.id.grTerminal).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.tmPv -> {
                    LocalData.apply {
                        channel = "phongvu"
                        terminal = "phongvu"
                    }
                }
                R.id.tmVnshop -> {
                    LocalData.apply {
                        channel = "vnshop"
                        terminal = "vnshop"
                    }
                }
                R.id.tmSoiBien -> {
                    LocalData.apply {
                        channel = "vnshop_online"
                        terminal = "sbn_desktop_hn"
                    }
                }
                else -> {
                    LocalData.apply {
                        channel = "vnshop_online"
                        terminal = "vnshop_app"
                    }
                }
            }
        }

        findViewById<RadioGroup>(R.id.grSeller).setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.slPv) {
                LocalData.apply {
                    sellerIds = "1"
                }
            } else {
                LocalData.apply {
                    sellerIds = "2"
                }
            }
        }

        findViewById<TextInputEditText>(R.id.tvChannelType).doAfterTextChanged {
            LocalData.apply { channelType = it.toString() }
        }

        findViewById<TextInputEditText>(R.id.tvChannelId).doAfterTextChanged {
            LocalData.apply { channelId = 6 }
        }

        findViewById<MaterialButton>(R.id.goToHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        LocalData.let {
            when (it.terminal) {
                "vnshop_app" -> {
                    findViewById<RadioGroup>(R.id.grTerminal).check(R.id.tmVnshopApp)
                }
                "vnshop" -> {
                    findViewById<RadioGroup>(R.id.grTerminal).check(R.id.tmVnshop)
                }
                "sbn_desktop_hn" -> {
                    findViewById<RadioGroup>(R.id.grTerminal).check(R.id.tmSoiBien)
                }
                else -> {
                    findViewById<RadioGroup>(R.id.grTerminal).check(R.id.tmPv)
                }
            }

            if (it.sellerIds == "1") {
                findViewById<RadioGroup>(R.id.grSeller).check(R.id.slPv)
            } else {
                findViewById<RadioGroup>(R.id.grSeller).check(R.id.slVnshop)
            }
        }
    }
}