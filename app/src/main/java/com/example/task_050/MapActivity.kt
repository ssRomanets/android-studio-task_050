package com.example.task_050

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.task_050.databinding.ActivityMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding
    private val startLocation = Point(53.2122, 50.1438)
    private val zoomValue: Float = 16.5f

    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setApiKey(savedInstanceState)
        MapKitFactory.initialize(this)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.map.move(
            CameraPosition(startLocation, zoomValue, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )
        setMarkerInStartLocation()
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return  bitmap
    }

    private fun setMarkerInStartLocation() {
        val marker = createBitmapFromVector(R.drawable.ic_pin_red_png)
        mapObjectCollection = binding.mapView.map.mapObjects
        placemarkMapObject = mapObjectCollection.addPlacemark(
            startLocation,
            ImageProvider.fromBitmap(marker)
        )
        placemarkMapObject.opacity = 1.0f
    }

    private fun setApiKey(savedInstanceState: Bundle?) {
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false
        if (!haveApiKey) MapKitFactory.setApiKey(Utils.MAPKIT_API_KEY)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}