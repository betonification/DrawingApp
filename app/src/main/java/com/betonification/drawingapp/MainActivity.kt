package com.betonification.drawingapp

import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore

class MainActivity : AppCompatActivity() {

    private var mImageButtonCurrentPaint: ImageButton? = null
    private var brushLastClicked = true
    private var eraserLastClicked = true
    private var brushNeverClicked = true
    private var eraserNeverClicked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onFullscreen()

        drawingView.setSizeForBrush(drawingView.lastPaintBrushSize)

        mImageButtonCurrentPaint = llPaintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageResource(R.drawable.pallet_selected)

        ibBrush.setOnClickListener(){
            drawingView.setColor(drawingView.lastColor)
            drawingView.setSizeForBrush(drawingView.lastPaintBrushSize)
            if (brushNeverClicked){
                showBrushSizeChooserDialog(ibBrush)
                brushNeverClicked = false
            }else if (brushLastClicked) {
                showBrushSizeChooserDialog(ibBrush)
            }
            brushLastClicked = true
            eraserLastClicked = false
        }
        ibClear.setOnClickListener(){
            drawingView.onClear()
        }
        ibEraser.setOnClickListener(){
            drawingView.setColor("#FFFFFF")
            drawingView.setSizeForBrush(drawingView.lastEraserSize)
            if (eraserNeverClicked){
                showBrushSizeChooserDialog(ibEraser)
                eraserNeverClicked = false
            }else if (eraserLastClicked) {
                showBrushSizeChooserDialog(ibEraser)
            }
            brushLastClicked = false
            eraserLastClicked = true
        }
        ibGallery.setOnClickListener(){
            if (!isReadStorageAllowed()){
                requestStoragePermission()
            }else{
                val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhotoIntent, GALLERY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            iv_background.setImageURI(data?.data)
        }
        onFullscreen()
    }

    private fun showBrushSizeChooserDialog(view: View){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size:")
        val smallBtn = brushDialog.ibSmallBrush
        smallBtn.setOnClickListener{
            drawingView.setSizeForBrush(10.toFloat())
            if(view == ibBrush) {
                drawingView.lastPaintBrushSize = 10.toFloat()
            }else{
                drawingView.lastEraserSize = 10.toFloat()
            }
            brushDialog.dismiss()
            onFullscreen()
        }
        val mediumBtn = brushDialog.ibMediumBrush
        mediumBtn.setOnClickListener{
            drawingView.setSizeForBrush(20.toFloat())
            if(view == ibBrush) {
                drawingView.lastPaintBrushSize = 20.toFloat()
            }else{
                drawingView.lastEraserSize = 20.toFloat()
            }
            brushDialog.dismiss()
            onFullscreen()
        }
        val largeBtn = brushDialog.ibLargeBrush
        largeBtn.setOnClickListener{
            drawingView.setSizeForBrush(30.toFloat())
            if(view == ibBrush) {
                drawingView.lastPaintBrushSize = 30.toFloat()
            }else{
                drawingView.lastEraserSize = 30.toFloat()
            }
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
            drawingView.lastColor = view.tag.toString()
            mImageButtonCurrentPaint = view
    }

    private fun onFullscreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())){
            Toast.makeText(this, "Permission is needed in order to add a background.", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted. You can add background now.", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Oops, you just denied the permission.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isReadStorageAllowed(): Boolean{
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            return (result == PackageManager.PERMISSION_GRANTED)
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }

}