package com.yuk.miuihome.activity

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuk.miuihome.R
import kotlin.concurrent.thread

class EntryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarTransparent()
        setContent {
            MessageCard()
        }
        thread {
            Thread.sleep(1000)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true
    )
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )

    @Composable
    private fun MessageCard() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.size(width = 100.dp, height = 100.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.miuihome),
                    contentDescription = "miuihome"
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .padding(0.dp, 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.app_name), fontSize = 28.sp)
                Text("Hook for MIUI Launcher", fontSize = 12.sp)
            }
        }
    }

    private fun changeStatusBarTransparent() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = option
        window.statusBarColor = Color.parseColor("#00000000")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}
