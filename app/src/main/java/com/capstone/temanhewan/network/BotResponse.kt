package com.capstone.temanhewan.network

import com.capstone.temanhewan.utils.ConsVal.Companion.OPEN_GOOGLE
import com.capstone.temanhewan.utils.ConsVal.Companion.OPEN_SEARCH
import com.capstone.temanhewan.utils.SolveMath
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

	fun basicResponses(_message: String): String {

		val random = (0..2).random()
		val message =_message.lowercase()

		return when {

			//Flips a coin
			message.contains("flip") && message.contains("coin") -> {
				val r = (0..1).random()
				val result = if (r == 0) "heads" else "tails"

				"I flipped a coin and it landed on $result"
			}

			//Math calculations
			message.contains("solve") -> {
				val equation: String? = message.substringAfterLast("solve")
				return try {
					val answer = SolveMath.solveMath(equation ?: "0")
					"$answer"

				} catch (e: Exception) {
					"Sorry, I can't solve that."
				}
			}

			//Hello
			message.contains("hello") -> {
				when (random) {
					0 -> "Hello! Ada yang bisa aku bantu?"
					1 -> "Hai! Aku disini siap membantu kamu!"
					2 -> "Hi! Apa yang bisa aku bantu?"
					else -> "error" }
			}

			//ask help
			message.contains("tolong bantu saya") -> {
				when (random) {
					0 -> "Apa yang terjadi pada hewan mu?"
					1 -> "Gejala apa yang terjadi pada hewan mu?"
					2 -> "Jelaskan pada saya bagaimana keadaan hewan peliharaan mu?"
					else -> "error"
				}
			}
			// Pruritus
			message.contains("anjing saya bulunya rontok sering garuk-garuk dikulitnya ada kerak dan banyak kutu") -> {
				when (random) {
					0 -> "Definisi:\nPruritus adalah rasa gatal atau rasa tidak nyaman pada kulit yang merangsang keinginan untuk menggaruk, menggosok, menjilat, atau menggigit bagian tersebut. Pada kondisi normal, pruritus diperlukan anjing untuk membersihkan tubuh dan menghilangkan ektoparasit serta senyawa atau bahan-bahan yang berbahaya dari permukaan kulit. Sebaliknya, apabila reaksinya berlebihan pruritus dapat menyebabkan perlukaan diri sendiri atau self mutilation yang mengakibat luka lecet pada permukaan kulit.\n\nPenyebab:\nPenyebab penyakit ini adalah parasit, infeksi jamur, alergi karena makanan dan bukan tidak mungkin juga kalau si penderita mengidap penyakit kelenjar, tiroid dan lain-lain. Biasanya terjadi pada jenis peliharaan yang perawatannya kurang higienis. Bisa juga karena kulit kering, suhu udara yang terlalu panas/dingin dan terkena cahaya/sinar/listrik, dll.\n\nPenanganan:\n-Cukur bulu terutama dibagian yang korengan atau meradang\n-Mandikan dengan shampoo anti jamur yang bisa anda beli di petshop atau klinik hewan\n-Lakukan perawatan secara konsisten dan disiplin. Lakukan pembersihan pada tempat makan dan minumnya juga dengan tempat tidur atau kandang anjing anda."
					else -> "error"
				}
			}
			//flu kucing
			message.contains("berat badan kucing turun, kucing bersin-bersin dan tidak mau makan. mata kucing juga membengkak dan berair")  -> {
				when (random) {
					0 -> "Definisi:\nCat scratch disease (CSD) adalah infeksi yang disebabkan oleh bakteri Bartonella henselae, dan spesies Bartonella lainnya yang lebih jarang.\n\nPenyebab:\nKucing dapat terinfeksi B. henselae dari gigitan kutu dan kotoran kutu (kotoran) yang masuk ke dalam lukanya. Dengan menggaruk dan menggigit kutu, kucing mengambil kotoran kutu yang terinfeksi di bawah kuku dan sela-sela giginya.\n\nPenanganan:\n1. Gunakan produk perawatan dan pencegahan kutu yang efektif direkomendasikan dan tersedia dari dokter hewan Anda.\n2. Bicaralah dengan dokter hewan Anda tentang pengujian dan perawatan untuk kucing Anda. Dokter hewan Anda dapat memberi tahu Anda apakah kucing Anda memerlukan pengujian atau perawatan."
					else -> "error"
				}
			}
			//goodbye
			message.contains("Terimakasih") && message.contains ("Ini sangat membantu") && message.contains ("Terimakasih bantuannya")-> {
				when (random) {
					0 -> "Semoga lekas sembuh"
					1 -> "Terima Kasih sudah menggunakan TemanHewan"
					2 -> "Senang bisa membantu kamu"
					else -> "error" }
			}

			//What time is it?
			message.contains("time") && message.contains("?")-> {
				val timeStamp = Timestamp(System.currentTimeMillis())
				val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
				val date = sdf.format(Date(timeStamp.time))

				date.toString()
			}

			//Open Google
			message.contains("open") && message.contains("google")-> {
				OPEN_GOOGLE
			}

			//Search on the internet
			message.contains("search")-> {
				OPEN_SEARCH
			}

			//When the programme doesn't understand...
			else -> {
				when (random) {
					0 -> "Aku gangerti, bisa kamu ulangi?"
					1 -> "Maaf yaa, Coba tolong jelaskan lagi!"
					2 -> "Huhu, kayaknya aku gapaham.. jelaskan lebih detail ya"
					else -> "error"
				}
			}
		}
	}
}