package com.yuk.miuihome.view.base

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.view.data.LayoutPair

class TextRV(
    private val textV: TextV = TextV(),
    val title: String? = null,
    private val titleResId: Int? = null,
    private val summary: String? = null,
    private val summaryResId: Int? = null,
    private var arrow: Boolean = false,
    private val onClickListener: View.OnClickListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearContainerV(LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(
                    LinearContainerV(LinearContainerV.VERTICAL, arrayOf(
                        LayoutPair(textV
                            .also { view ->
                                title.let { view.text = it }
                                titleResId?.let { view.resId = it }
                        }.create(context).also { it.setPadding(0, 0, 0, 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                        LayoutPair(TextView(context)
                            .also { view ->
                                summary?.let { view.text = it }
                                summaryResId?.let { view.setText(it) }
                                view.setTextColor(context.getColor(R.color.spinner))
                                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                                view.paint.typeface = Typeface.create(null, 400,false)
                                view.setPadding(0, 0, 0, 0)
                                if (summary == null && summaryResId == null) view.visibility = View.GONE
                            }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
                    )
                ).create(context),LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(
                    ImageView(context)
                        .also { view ->
                            view.background = context.getDrawable(R.drawable.ic_right_arrow)
                            if (!arrow) view.visibility = View.GONE
                              }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL }
                )
            )
        ).create(context).also { view -> onClickListener?.let { view.setOnClickListener(it); view.background = context.getDrawable(R.drawable.ic_click_check) }; view.setPadding(dp2px(25f), dp2px(16f), dp2px(25f), dp2px(16f)) }
    }
}