package com.yuk.miuihome.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuk.miuihome.R
import com.yuk.miuihome.UpdatesInfo
import com.yuk.miuihome.UpdatesManager
import com.yuk.miuihome.activity.ui.theme.OwnTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarTransparent()
        setContent {
            OwnTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Main()
                }
            }
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
    )
    @Preview(
        name = "Night Mode",
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true
    )

    @Composable
    private fun Main() {
        val coroutineScope = rememberCoroutineScope()
        OwnTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box {
                    Column(
                        Modifier.padding(13.dp, 60.dp, 13.dp, 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            stringResource(R.string.app_name),
                            fontWeight = FontWeight.Bold,
                            fontSize = 35.sp, color = MaterialTheme.colors.onBackground
                        )
                        Text(
                            "Hook for MIUI Launcher",
                            fontSize = 15.sp, color = MaterialTheme.colors.onBackground
                        )
                    }
                }
                Column(
                    Modifier.padding(13.dp, 5.dp, 13.dp, 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.MainActivity1),
                        Modifier.padding(10.dp),
                        fontSize = 14.sp, color = MaterialTheme.colors.onBackground
                    )
                    Row(
                        Modifier.padding(13.dp, 10.dp, 13.dp, 10.dp)
                    ) {
                        ElevatedButton(
                            onClick = {
                                val intent = Intent()
                                intent.component = ComponentName(
                                    "com.miui.home",
                                    "com.miui.home.settings.MiuiHomeSettingActivity"
                                )
                                startActivity(intent)
                            }, contentPadding = PaddingValues(
                                start = 20.dp,
                                top = 15.dp,
                                end = 20.dp,
                                bottom = 15.dp
                            )
                        ) {
                            Text(
                                stringResource(R.string.OpenMiuiHomeSettings),
                                fontSize = 14.sp,
                            )
                        }
                        Spacer(Modifier.size(10.dp))
                        if (!getSP().getBoolean("shouldHide", false)) {
                            IconButtonItem(
                                "com.yuk.miuihome.activity.EntryActivity",
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                true,
                                "com.yuk.miuihome.activity.EntryActivityAlias",
                                R.string.HideAppIcon
                            )
                        } else {
                            IconButtonItem(
                                "com.yuk.miuihome.activity.EntryActivity",
                                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                                false,
                                "com.yuk.miuihome.activity.EntryActivity",
                                R.string.ShowAppIcon
                            )
                        }
                    }
                    Row {
                        Text(
                            stringResource(R.string.State) + " :  ",
                            Modifier.padding(0.dp, 10.dp),
                            fontSize = 14.sp, color = MaterialTheme.colors.onBackground
                        )
                        if (isModuleEnable()) {
                            Text(
                                stringResource(R.string.ModuleEnable),
                                Modifier.padding(0.dp, 10.dp),
                                fontSize = 14.sp,
                                color = Green
                            )
                        } else {
                            Text(
                                stringResource(R.string.ModuleNotEnable),
                                Modifier.padding(0.dp, 10.dp),
                                fontSize = 14.sp,
                                color = Red
                            )
                        }
                    }
                    Column(
                        Modifier.padding(13.dp, 10.dp, 13.dp, 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            stringResource(R.string.MainActivity2),
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    Row {
                        Text(
                            stringResource(R.string.About),
                            Modifier.padding(13.dp, 13.dp, 13.dp, 8.dp),
                            fontSize = 14.sp, color = MaterialTheme.colors.onBackground
                        )
                    }
                    Row(Modifier.padding(13.dp, 5.dp, 13.dp, 5.dp)) {
                        AboutButtonItem(
                            "https://github.com/1767523953/MiuiHome", R.drawable.github, "Github"
                        )
                        Spacer(Modifier.size(10.dp))
                        AboutButtonItem(
                            "https://t.me/MiuiHome_Xposed",
                            R.drawable.telegram, "Telegram"
                        )
                        Spacer(Modifier.size(10.dp))
                        AboutButtonItem(
                            "https://qun.qq.com/qqweb/qunpro/share?_wv=3&_wwv=128&inviteCode=1PHunU&from=246610&biz=ka",
                            R.drawable.qq, "QQ"
                        )
                    }
                    var info: UpdatesInfo? by remember {
                        mutableStateOf(null)
                    }
                    var showUpdatesDialog by remember {
                        mutableStateOf(false)
                    }
                    Row(Modifier.padding(13.dp, 5.dp, 13.dp, 5.dp)) {
                        ElevatedButton(
                            onClick = {
                                coroutineScope.launch {
                                    withContext(Dispatchers.IO) {
                                        val updatesInfo = UpdatesManager.checkUpdates()
                                        if (updatesInfo.error) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(this@MainActivity, R.string.CheckUpdatesError, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        if (updatesInfo.hasUpdates) {
                                            info = updatesInfo
                                            showUpdatesDialog = true
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(this@MainActivity, R.string.CheckUpdatesNotFound, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.CheckUpdates),
                                fontSize = 14.sp
                            )
                        }
                    }
                    if (showUpdatesDialog) {
                        AlertDialog(
                            onDismissRequest = { showUpdatesDialog = false },
                            title = { Text(text = stringResource(id = R.string.CheckUpdatesFound)) },
                            text = {
                                Column {
                                    Text(
                                        text = "${stringResource(id = R.string.app_name)}${info!!.versionName}(${info!!.versionCode})",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${info!!.content}",
                                        fontSize = 14.sp
                                    )
                                }
                            },
                            confirmButton = {
                                Column {
                                    ElevatedButton(onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                        intent.data =
                                            android.net.Uri.parse(info!!.downloadLink)
                                        startActivity(intent)
                                    }) {
                                        Text(text = stringResource(id = R.string.Download))
                                    }
                                    ElevatedButton(onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                        intent.data =
                                            android.net.Uri.parse(info!!.downloadLink!!.replace("github.com", "github.com.cnpmjs.org"))
                                        startActivity(intent)
                                    }) {
                                        Text(text = stringResource(id = R.string.DownloadWithCDN))
                                    }
                                }
                            },
                            dismissButton = {
                                ElevatedButton(onClick = { showUpdatesDialog = false }) {
                                    Text(text = stringResource(id = R.string.Dismiss))
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun IconButtonItem(
        Activity: String,
        Status: Int,
        Status1: Boolean,
        Activity1: String,
        @StringRes textId: Int
    ) {
        ElevatedButton(
            onClick = {
                packageManager.setComponentEnabledSetting(
                    ComponentName(
                        packageName,
                        Activity
                    ),
                    Status,
                    PackageManager.DONT_KILL_APP
                )
                getSP().edit().putBoolean("shouldHide", Status1).apply()
                finish()
                val intent = Intent()
                intent.component =
                    ComponentName(
                        packageName,
                        Activity1
                    )
                startActivity(intent)
            }, contentPadding = PaddingValues(
                start = 20.dp,
                top = 15.dp,
                end = 20.dp,
                bottom = 15.dp
            )
        ) {
            Text(
                stringResource(textId),
                fontSize = 14.sp
            )
        }
    }

    @Composable
    fun AboutButtonItem(Uri: String, @DrawableRes iconId: Int, title: String) {
        ElevatedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data =
                    android.net.Uri.parse(Uri)
                startActivity(intent)
            }, contentPadding = PaddingValues(
                start = 20.dp,
                top = 15.dp,
                end = 20.dp,
                bottom = 15.dp
            )
        ) {
            Icon(
                painterResource(iconId),
                contentDescription = title
            )
        }
    }

    private fun getSP(): SharedPreferences {
        return getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    private fun isModuleEnable(): Boolean {
        return false
    }

    private fun changeStatusBarTransparent() {
        window.statusBarColor = Color.parseColor("#00000000")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}