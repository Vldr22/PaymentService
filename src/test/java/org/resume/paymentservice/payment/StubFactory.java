package org.resume.paymentservice.payment;

import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

final class StubFactory {

    private StubFactory() {
    }

    /**
     * Регистрирует WireMock-stub для POST-запроса.
     */
    static void stubPost(String path, int status, String fixtureFile) {
        WireMock.stubFor(post(urlEqualTo(path))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(fixtureFile)));
    }

    /**
     * Регистрирует WireMock-stub для GET-запроса.
     */
    static void stubGet(String path, int status, String fixtureFile) {
        WireMock.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(fixtureFile)));
    }
}
