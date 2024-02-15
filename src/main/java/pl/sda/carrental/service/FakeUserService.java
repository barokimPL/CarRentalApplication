package pl.sda.carrental.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.sda.carrental.model.dataTransfer.rest.FakeUser;
import pl.sda.carrental.model.dataTransfer.rest.FakeUserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FakeUserService {
        private final WebClient webclient;

    public FakeUserService() {
        this.webclient = WebClient.builder().baseUrl("https://dummyjson.com/users").build();
    }

    public Mono<FakeUser> getFakeUser(int id) {
        return this.webclient.get().uri("/" + id).retrieve().bodyToMono(FakeUser.class);
    }

    public Flux<FakeUserResponse> getFakeUsersResponse() {
        return this.webclient.method(HttpMethod.GET)
                .retrieve()
                .bodyToFlux(FakeUserResponse.class);
    }

    // Map only the users array from the response
    public Flux<FakeUser> getFakeUsers() {
        return getFakeUsersResponse().flatMapIterable(FakeUserResponse::getUsers);
    }
}
