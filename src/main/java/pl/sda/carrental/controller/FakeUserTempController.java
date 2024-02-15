package pl.sda.carrental.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sda.carrental.service.FakeUserService;

@Controller
@Profile("dev")
public class FakeUserTempController {
    private final FakeUserService fakeUserService;

    public FakeUserTempController(FakeUserService fakeUserService) {
        this.fakeUserService = fakeUserService;
    }

    @GetMapping("/dev/fake-users")
    @ResponseBody
    public String fakeUsersTest() {
        StringBuilder sb = new StringBuilder();
        fakeUserService.getFakeUsers()
                .doOnNext(user -> sb.append(user.toString()))
                .blockLast();

        return sb.toString();
    }
}
