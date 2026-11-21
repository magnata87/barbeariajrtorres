package com.juniortorres.launcher

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import java.util.Locale

class MainActivity : Activity() {
    private val navy = Color.rgb(8, 13, 24)
    private val panel = Color.rgb(19, 29, 48)
    private val gold = Color.rgb(220, 174, 83)
    private val white = Color.WHITE
    private val muted = Color.rgb(180, 190, 207)
    private lateinit var root: LinearLayout
    private lateinit var body: LinearLayout
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(navy); setPadding(34, 20, 34, 22) }
        setContentView(root)
        render()
    }

    private fun render() {
        root.removeAllViews()
        val header = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val brand = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        brand.addView(label("✂  BARBEARIA JUNIOR TORRES", 25f, gold, true))
        brand.addView(label("Estilo é identidade!", 13f, muted, false))
        header.addView(brand, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(label("◉  WI-FI     ${java.text.SimpleDateFormat("HH:mm", Locale("pt", "BR")).format(java.util.Date())}", 16f, white, true))
        root.addView(header)

        val nav = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; setPadding(0, 18, 0, 18) }
        listOf("INÍCIO", "FUTEBOL AO VIVO", "PAGAMENTOS").forEachIndexed { i, title ->
            val b = button(title, if (page == i) gold else panel, if (page == i) Color.BLACK else white)
            b.setOnClickListener { page = i; render() }
            nav.addView(b, LinearLayout.LayoutParams(0, 48, 1f).apply { setMargins(0, 0, 10, 0) })
        }
        root.addView(nav)
        body = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(10, 12, 10, 10) }
        root.addView(body, LinearLayout.LayoutParams(-1, 0, 1f))
        when (page) { 0 -> home(); 1 -> football(); else -> payments() }
        root.addView(label("Barbearia Junior Torres  •  Use as setas e OK do controle remoto", 11f, muted, false).apply { gravity = Gravity.CENTER })
    }

    private fun home() {
        body.addView(label("BEM-VINDO!", 27f, white, true))
        body.addView(label("Escolha um aplicativo", 15f, muted, false))
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setPadding(0, 25, 0, 12) }
        val apps = listOf(
            Triple("📺\nTV", "UniTV", "com.integration.unitvsiptv"),
            Triple("▶\nYouTube", "YouTube", "com.google.android.youtube.tv"),
            Triple("X\nPLUS VOD", "X Plus VOD", ""),
            Triple("X\nPLUS EXPRESS", "X Plus Express", ""),
            Triple("▶\nVELO PLAY", "Velo Play", ""),
            Triple("N\nNetflix", "Netflix", "com.netflix.ninja"),
            Triple("prime\nvideo", "Prime Video", "com.amazon.amazonvideo.livingroom"),
            Triple("max", "Max", "")
        )
        apps.forEach { item ->
            val tile = button(item.first, panel, white).apply { textSize = 17f; isAllCaps = false; setOnClickListener { if (item.third.isNotBlank()) openPackage(item.third) else toast("Instale/configure o aplicativo para habilitar este atalho.") } }
            row.addView(tile, LinearLayout.LayoutParams(0, 118, 1f).apply { setMargins(5, 0, 5, 0) })
        }
        body.addView(row)
        val info = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setPadding(0, 20, 0, 0) }
        val contact = card("WHATSAPP DA BARBEARIA", "(81) 98805-6513\nAgendamentos e informações")
        info.addView(contact, LinearLayout.LayoutParams(0, 104, 1f).apply { setMargins(5, 0, 8, 0) })
        val spotify = card("SPOTIFY", "Player de música\nAbra o Spotify instalado na TV para ouvir")
        info.addView(spotify, LinearLayout.LayoutParams(0, 104, 1f).apply { setMargins(8, 0, 5, 0) })
        body.addView(info)
    }

    private fun football() {
        body.addView(label("⚽  FUTEBOL AO VIVO", 29f, white, true))
        body.addView(label("Brasil • Europa • Américas • Seleções", 16f, gold, true))
        body.addView(label("A tela está preparada, mas a programação e os canais precisam de uma fonte de dados esportiva confiável.", 17f, muted, false).apply { gravity = Gravity.CENTER; setPadding(40, 28, 40, 14) })
        body.addView(card("SEM TRANSMISSÕES INVENTADAS", "Quando a fonte estiver conectada, serão exibidos jogos ao vivo, próximos jogos, horário de Brasília, placar e canal/plataforma confirmados."), LinearLayout.LayoutParams(-1, 130))
    }

    private fun payments() {
        body.addView(label("PAGAMENTOS", 29f, white, true))
        body.addView(label("PIX • Rápido e seguro", 16f, gold, true))
        val name = "JOSENILDO JUNIOR DA SILVA TORRES"
        val key = "(81) 98805-6513"
        val pixPayload = buildPixPayload("81988056513", name, "BREJO DA MADRE DE DEUS")
        try {
            val matrix = MultiFormatWriter().encode(pixPayload, BarcodeFormat.QR_CODE, 360, 360)
            val bmp = Bitmap.createBitmap(360, 360, Bitmap.Config.RGB_565)
            for (x in 0 until 360) for (y in 0 until 360) bmp.setPixel(x, y, if (matrix[x, y]) Color.BLACK else Color.WHITE)
            val qr = ImageView(this).apply { setImageBitmap(bmp); setPadding(10, 10, 10, 10); setBackgroundColor(Color.WHITE); adjustViewBounds = true }
            body.addView(qr, LinearLayout.LayoutParams(230, 230).apply { setMargins(0, 14, 0, 10) })
        } catch (_: Exception) { body.addView(label("Não foi possível gerar o QR Code.", 14f, muted, false)) }
        body.addView(label("Titular: $name", 16f, white, true))
        body.addView(label("Chave Pix celular: $key", 16f, muted, false))
        val copy = button("COPIAR CHAVE PIX", gold, Color.BLACK)
        copy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Chave Pix", "81988056513"))
            toast("Chave Pix copiada")
        }
        body.addView(copy, LinearLayout.LayoutParams(250, 52).apply { setMargins(0, 14, 0, 0) })
        body.addView(label("Confira o nome e o valor no aplicativo do seu banco antes de confirmar.", 12f, muted, false).apply { setPadding(0, 10, 0, 0) })
    }

    private fun buildPixPayload(key: String, name: String, city: String): String {
        fun field(id: String, value: String): String = id + value.length.toString().padStart(2, '0') + value
        val merchant = field("00", "BR.GOV.BCB.PIX") + field("01", key)
        val merchantField = field("26", merchant)
        val additional = field("05", "***")
        val base = field("00", "01") + field("01", "11") + merchantField + field("52", "0000") + field("53", "986") + field("58", "BR") + field("59", name.take(25)) + field("60", city.take(15)) + field("62", additional) + "6304"
        return base + crc16(base).toString(16).uppercase(Locale.ROOT).padStart(4, '0')
    }

    private fun crc16(data: String): Int {
        var crc = 0xFFFF
        for (b in data.toByteArray(Charsets.UTF_8)) {
            crc = crc xor ((b.toInt() and 0xFF) shl 8)
            repeat(8) { crc = if ((crc and 0x8000) != 0) ((crc shl 1) xor 0x1021) and 0xFFFF else (crc shl 1) and 0xFFFF }
        }
        return crc
    }

    private fun openPackage(pkg: String) {
        val intent = packageManager.getLaunchIntentForPackage(pkg)
        if (intent != null) startActivity(intent) else toast("Aplicativo não encontrado. Verifique se está instalado na TV.")
    }

    private fun label(text: String, size: Float, color: Int, bold: Boolean) = TextView(this).apply {
        this.text = text; textSize = size; setTextColor(color); gravity = Gravity.CENTER
        if (bold) setTypeface(typeface, Typeface.BOLD)
    }

    private fun button(text: String, bg: Int, fg: Int) = Button(this).apply {
        this.text = text; setTextColor(fg); textSize = 14f; isAllCaps = true
        background = GradientDrawable().apply { setColor(bg); cornerRadius = 14f; setStroke(2, if (bg == gold) gold else Color.rgb(55, 72, 98)) }
        isFocusable = true; isFocusableInTouchMode = true
    }

    private fun card(title: String, detail: String) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(16, 10, 16, 10)
        background = GradientDrawable().apply { setColor(panel); cornerRadius = 18f; setStroke(1, Color.rgb(55, 72, 98)) }
        addView(label(title, 16f, gold, true)); addView(label(detail, 13f, white, false))
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (page != 0) { page = 0; render(); return true }
        }
        return super.onKeyDown(keyCode, event)
    }
}
