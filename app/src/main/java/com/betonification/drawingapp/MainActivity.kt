package com.betonification.drawingapp

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private var mImageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onFullscreen()

        drawingView.setSizeForBrush(10.toFloat())

        mImageButtonCurrentPaint = llPaintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageResource(R.drawable.pallet_selected)

        ibBrush.setOnClickListener(){
            showBrushSizeChooserDialog()
        }
        ibClear.setOnClickListener(){
            drawingView.onClear()
        }
        ibEraser.setOnClickListener(){
            showBrushSizeChooserDialog()
            drawingView.setColor("#FFFFFF")
        }
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size:")
        val smallBtn = brushDialog.ibSmallBrush
        smallBtn.setOnClickListener{
            drawingView.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
            onFullscreen()
        }
        val mediumBtn = brushDialog.ibMediumBrush
        mediumBtn.setOnClickListener{
            drawingView.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
            onFullscreen()
        }
        val largeBtn = brushDialog.ibLargeBrush
        largeBtn.setOnClickListener{
            drawingView.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
            onFullscreen()
        }
        brushDialog.show()
        brushDialog.setOnCancelListener(){
            onFullscreen()
        }
    }

    fun paintClicked(view: View){
            mImageButtonCurrentPaint!!.setImageResource(R.drawable.pallet_normal)
            (view as ImageButton).setImageResource(R.drawable.pallet_selected)
            drawingView.setColor(view.tag.toString())
            mImageButtonCurrentPaint = view
    }

    fun onFullscreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}