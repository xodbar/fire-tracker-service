package app.core.ai

import app.useCase.AnalyseRequest
import app.useCase.AnalyzeResponse
import app.useCase.AnalyzeResponseElement
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import kotlin.random.Random

fun interface AiPredictionService {
	fun analyzeFireData(request: AnalyseRequest): AnalyzeResponse
}

@Service
@ConditionalOnProperty(value = ["app.services.ai.mocked"], havingValue = "false")
class AiPredictionServiceImpl : AiPredictionService {

	override fun analyzeFireData(request: AnalyseRequest): AnalyzeResponse {
		TODO("do something")
	}
}

@Service
@ConditionalOnProperty(value = ["app.services.ai.mocked"], havingValue = "true")
class AiPredictionServiceMock : AiPredictionService {

	override fun analyzeFireData(request: AnalyseRequest): AnalyzeResponse {
		val data = mutableListOf<AnalyzeResponseElement>()
		request.data.forEach { analyseElement ->
			val randomRate = Random.nextInt(0, 10)
			val randomRadius = Random.nextInt(0, 100)

			data.add(AnalyzeResponseElement(analyseElement.latitude, analyseElement.longitude, randomRate, randomRadius))
		}

		return AnalyzeResponse(data)
	}
}
