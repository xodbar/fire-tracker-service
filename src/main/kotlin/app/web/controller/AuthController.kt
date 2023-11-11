package app.web.controller

import app.useCase.IssueTokenInput
import app.useCase.IssueTokenUseCase
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val issueTokenUseCase: IssueTokenUseCase
) {

    @PostMapping("/issue-token")
    @ResponseBody
    fun issueToken(@RequestBody input: IssueTokenInput) =
        issueTokenUseCase.handle(input)
}
