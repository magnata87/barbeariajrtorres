
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
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private val navy = Color.rgb(7, 12, 24)
    private val panel = Color.rgb(17, 27, 45)
    private val panel2 = Color.rgb(24, 37, 59)
    private val gold = Color.rgb(224, 178, 91)
    private val blue = Color.rgb(40, 135, 230)
    private val white = Color.WHITE
    private val muted = Color.rgb(175, 188, 207)

    private lateinit var root: LinearLayout
    private lateinit var body: LinearLayout

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(navy)
            setPadding(dp(28), dp(14), dp(28), dp(12))
        }

        setContentView(root)
        render()
    }

    private fun render() {
        root.removeAllViews()

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        brand.addView(label("✂  BARBEARIA JUNIOR TORRES", 24f, gold, true))
        brand.addView(label("ESTILO É IDENTIDADE!", 11f, muted, true))

        header.addView(
            brand,
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        val date = SimpleDateFormat(
            "EEEE, dd/MM  •  HH:mm",
            Locale("pt", "BR")
        ).format(Date())

        header.addView(
            label("◉  WI-FI     $date", 14f, white, true)
        )

        root.addView(header)

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, dp(13), 0, dp(13))
        }

        listOf("INÍCIO", "FUTEBOL AO VIVO", "PAGAMENTOS")
            .forEachIndexed { index, title ->

                val item = button(
                    title,
                    if (page == index) gold else panel,
                    if (page == index) Color.BLACK else white
                )

                item.setOnClickListener {
                    page = index
                    render()
                }

                nav.addView(
                    item,
                    LinearLayout.LayoutParams(0, dp(42), 1f).apply {
                        setMargins(dp(4), 0, dp(4), 0)
                    }
                )
            }

        root.addView(nav)

        body = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }

        root.addView(
            body,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        when (page) {
            0 -> home()
            1 -> football()
            else -> payments()
        }

        root.addView(
            label(
                "BARBEARIA JUNIOR TORRES  •  USE AS SETAS E OK DO CONTROLE",
                10f,
                muted,
                false
            )
        )
    }

    private fun home() {

        val welcome = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(2), 0, dp(10))
        }

        welcome.addView(label("BEM-VINDO!", 22f, white, true))
        welcome.addView(
            label("Escolha seu entretenimento", 12f, muted, false)
        )

        body.addView(welcome)

        // ÁREA PRINCIPAL: TV / CINEMA / YOUTUBE

        val main = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        body.addView(
            main,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        // COLUNA TV

        val tvColumn = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(5), dp(5), dp(5), dp(5))
            background = cardBackground(panel)
        }

        tvColumn.addView(label("TV", 20f, gold, true))

        val tv = button("📺\n\nUniTV Free", panel2, white).apply {
            textSize = 19f
            isAllCaps = false
            setOnClickListener {
                openPackage("com.integration.unitvsiptv")
            }
        }

        tvColumn.addView(
            tv,
            LinearLayout.LayoutParams(-1, 0, 1f).apply {
                setMargins(dp(5), dp(8), dp(5), dp(5))
            }
        )

        main.addView(
            tvColumn,
            LinearLayout.LayoutParams(0, -1, 0.9f).apply {
                setMargins(dp(4), 0, dp(8), 0)
            }
        )

        // COLUNA CINEMA

        val cinema = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(7), dp(5), dp(7), dp(5))
            background = cardBackground(panel)
        }

        cinema.addView(
            label("CINEMA  •  FILMES E SÉRIES", 17f, gold, true)
        )

        val apps = listOf(
            Triple("X PLUS VOD", "X Plus VOD", ""),
            Triple("X PLUS EXPRESS", "X Plus Express", ""),
            Triple("VELO PLAY", "Velo Play", ""),
            Triple("NETFLIX", "Netflix", "com.netflix.ninja"),
            Triple("PRIME VIDEO", "Prime Video", "com.amazon.amazonvideo.livingroom"),
            Triple("MAX", "Max", "")
        )

        for (rowIndex in 0..1) {

            val appRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            for (columnIndex in 0..2) {

                val item = apps[rowIndex * 3 + columnIndex]

                val tile = button(item.first, panel2, white).apply {
                    textSize = 13f
                    isAllCaps = false
                    setOnClickListener {
                        if (item.third.isNotBlank()) {
                            openPackage(item.third)
                        } else {
                            toast(
                                "Configure o aplicativo para habilitar este atalho."
                            )
                        }
                    }
                }

                appRow.addView(
                    tile,
                    LinearLayout.LayoutParams(0, 0, 1f).apply {
                        height = dp(65)
                        setMargins(dp(4), dp(5), dp(4), dp(5))
                    }
                )
            }

            cinema.addView(
                appRow,
                LinearLayout.LayoutParams(-1, 0, 1f)
            )
        }

        main.addView(
            cinema,
            LinearLayout.LayoutParams(0, -1, 2.2f).apply {
                setMargins(dp(4), 0, dp(8), 0)
            }
        )

        // YOUTUBE EM DESTAQUE

        val youtube = button(
            "▶\n\nYOUTUBE",
            Color.rgb(25, 35, 55),
            white
        ).apply {
            textSize = 23f
            isAllCaps = false
            background = cardBackground(blue)
            setOnClickListener {
                openPackage("com.google.android.youtube.tv")
            }
        }

        main.addView(
            youtube,
            LinearLayout.LayoutParams(0, -1, 1.05f).apply {
                setMargins(dp(4), 0, dp(4), 0)
            }
        )

        // ÁREA INFERIOR

        val bottom = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, dp(12), 0, 0)
        }

        val wifi = smallCard(
            "◉  WI-FI",
            "Configurações de rede"
        ) {
            try {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            } catch (_: Exception) {
                toast("Configurações de Wi-Fi indisponíveis.")
            }
        }

        val whatsapp = smallCard(
            "WHATSAPP",
            "Agendamentos"
        ) {
            openUrl("https://wa.me/5581988056513")
        }

        val spotify = smallCard(
            "SPOTIFY",
            "Abrir música"
        ) {
            openPackage("com.spotify.tv.android")
        }

        val pix = smallCard(
            "PIX",
            "Pagamentos"
        ) {
            page = 2
            render()
        }

        listOf(wifi, whatsapp, spotify, pix).forEach { card ->
            bottom.addView(
                card,
                LinearLayout.LayoutParams(0, dp(70), 1f).apply {
                    setMargins(dp(4), 0, dp(4), 0)
                }
            )
        }

        body.addView(bottom)
    }

    private fun football() {

        body.addView(
            label("⚽  FUTEBOL AO VIVO", 28f, white, true)
        )

        body.addView(
            label(
                "BRASIL  •  EUROPA  •  AMÉRICAS  •  SELEÇÕES",
                15f,
                gold,
                true
            )
        )

        body.addView(
            card(
                "CENTRAL DO FUTEBOL",
                "Área preparada para jogos ao vivo, próximos jogos, " +
                    "horários de Brasília, placares e canais confirmados."
            ),
            LinearLayout.LayoutParams(-1, dp(125)).apply {
                setMargins(dp(20), dp(20), dp(20), dp(10))
            }
        )

        body.addView(
            label(
                "A programação será exibida quando uma fonte esportiva confiável for conectada.",
                13f,
                muted,
                false
            )
        )
    }

    private fun payments() {

        body.addView(
            label("PAGAMENTOS", 28f, white, true)
        )

        body.addView(
            label("PIX  •  RÁPIDO E SEGURO", 16f, gold, true)
        )

        val name = "JOSENILDO JUNIOR DA SILVA TORRES"
        val key = "81988056513"

        try {
            val payload = buildPixPayload(
                "+5581988056513",
                name,
                "BREJO DA MADRE DE DEUS"
            )

            val matrix = MultiFormatWriter().encode(
                payload,
                BarcodeFormat.QR_CODE,
                360,
                360
            )

            val bitmap = Bitmap.createBitmap(
                360,
                360,
                Bitmap.Config.RGB_565
            )

            for (x in 0 until 360) {
                for (y in 0 until 360) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (matrix[x, y]) Color.BLACK else Color.WHITE
                    )
                }
            }

            val qr = ImageView(this).apply {
                setImageBitmap(bitmap)
                setPadding(dp(8), dp(8), dp(8), dp(8))
                setBackgroundColor(Color.WHITE)
            }

            body.addView(
                qr,
                LinearLayout.LayoutParams(dp(190), dp(190)).apply {
                    setMargins(0, dp(8), 0, dp(8))
                }
            )

        } catch (_: Exception) {
            body.addView(
                label("Não foi possível gerar o QR Code.", 14f, muted, false)
            )
        }

        body.addView(
            label("TITULAR: $name", 15f, white, true)
        )

        body.addView(
            label("CHAVE PIX CELULAR: (81) 98805-6513", 15f, muted, false)
        )

        val copy = button(
            "COPIAR CHAVE PIX",
            gold,
            Color.BLACK
        )

        copy.setOnClickListener {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            clipboard.setPrimaryClip(
                ClipData.newPlainText("Chave Pix", key)
            )

            toast("Chave Pix copiada.")
        }

        body.addView(
            copy,
            LinearLayout.LayoutParams(dp(230), dp(48)).apply {
                setMargins(0, dp(10), 0, dp(8))
            }
        )

        body.addView(
            label(
                "Confira o titular e os dados no aplicativo do banco antes de pagar. QR Code ainda precisa ser validado.",
                11f,
                muted,
                false
            )
        )
    }

    private fun smallCard(
        title: String,
        detail: String,
        action: () -> Unit
    ): LinearLayout {

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(5), dp(5), dp(5), dp(5))
            background = cardBackground(panel)
            isFocusable = true
            isClickable = true
            setOnClickListener { action() }
        }

        card.addView(label(title, 14f, gold, true))
        card.addView(label(detail, 10f, white, false))

        return card
    }

    private fun buildPixPayload(
        key: String,
        name: String,
        city: String
    ): String {

        fun field(id: String, value: String): String {
            return id + value.length.toString().padStart(2, '0') + value
        }

        val merchant =
            field("00", "BR.GOV.BCB.PIX") +
            field("01", key)

        val merchantField = field("26", merchant)
        val additional = field("05", "***")

        val base =
            field("00", "01") +
            field("01", "11") +
            merchantField +
            field("52", "0000") +
            field("53", "986") +
            field("58", "BR") +
            field("59", name.take(25)) +
            field("60", city.take(15)) +
            field("62", additional) +
            "6304"

        return base + crc16(base)
            .toString(16)
            .uppercase(Locale.ROOT)
            .padStart(4, '0')
    }

    private fun crc16(data: String): Int {
        var crc = 0xFFFF

        for (b in data.toByteArray(Charsets.UTF_8)) {
            crc = crc xor ((b.toInt() and 0xFF) shl 8)

            repeat(8) {
                crc = if ((crc and 0x8000) != 0) {
                    ((crc shl 1) xor 0x1021) and 0xFFFF
                } else {
                    (crc shl 1) and 0xFFFF
                }
            }
        }

        return crc
    }

    private fun openPackage(pkg: String) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(pkg)

            if (intent != null) {
                startActivity(intent)
            } else {
                toast("Aplicativo não encontrado na TV.")
            }
        } catch (_: Exception) {
            toast("Não foi possível abrir o aplicativo.")
        }
    }

    private fun openUrl(url: String) {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
        } catch (_: Exception) {
            toast("Não foi possível abrir o link.")
        }
    }

    private fun label(
        text: String,
        size: Float,
        color: Int,
        bold: Boolean
    ): TextView {

        return TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER

            if (bold) {
                setTypeface(typeface, Typeface.BOLD)
            }
        }
    }

    private fun button(
        text: String,
        bg: Int,
        fg: Int
    ): Button {

        return Button(this).apply {
            this.text = text
            setTextColor(fg)
            textSize = 14f
            isAllCaps = false
            gravity = Gravity.CENTER
            isFocusable = true
            isFocusableInTouchMode = true

            background = cardBackground(bg)

            setOnFocusChangeListener { view, focused ->
                view.background = cardBackground(
                    if (focused) gold else bg
                )

                if (view is Button) {
                    view.setTextColor(
                        if (focused) Color.BLACK else fg
                    )
                }
            }
        }
    }

    private fun card(
        title: String,
        detail: String
    ): LinearLayout {

        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(15), dp(10), dp(15), dp(10))
            background = cardBackground(panel)

            addView(label(title, 17f, gold, true))
            addView(label(detail, 13f, white, false))
        }
    }

    private fun cardBackground(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(15).toFloat()

            setStroke(
                dp(2),
                if (color == gold || color == blue) color
                else Color.rgb(55, 75, 105)
            )
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK && page != 0) {
            page = 0
            render()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}
