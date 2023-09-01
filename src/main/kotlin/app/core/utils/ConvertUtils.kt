package app.core.utils

fun csvToJson(csvString: String): String {
	val lines = csvString.trim().split("\n")
	val header = lines[0].split(",")

	val jsonArray = StringBuilder("[\n")

	for (i in 1 until lines.size) {
		val values = lines[i].split(",")
		val jsonObject = StringBuilder("\t{\n")

		for (j in header.indices) {
			jsonObject.append("\t\t\"${header[j]}\": \"${values[j]}\"")

			if (j < header.size - 1) {
				jsonObject.append(",")
			}

			jsonObject.append("\n")
		}

		jsonObject.append("\t}")

		if (i < lines.size - 1) {
			jsonObject.append(",")
		}

		jsonObject.append("\n")
		jsonArray.append(jsonObject)
	}

	jsonArray.append("]")

	return jsonArray.toString()
}
