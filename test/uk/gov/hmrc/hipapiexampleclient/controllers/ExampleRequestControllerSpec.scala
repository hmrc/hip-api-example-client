/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.hipapiexampleclient.controllers

import com.github.tomakehurst.wiremock.client.WireMock.{status => _, _}
import org.scalatest.OptionValues
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.must.Matchers
import play.api.http.ContentTypes
import play.api.{Application, Configuration}
import play.api.http.Status.OK
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.test.{HttpClientV2Support, WireMockSupport}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

class ExampleRequestControllerSpec extends AsyncFreeSpec
  with Matchers
  with WireMockSupport
  with HttpClientV2Support
  with OptionValues {

  "exampleRequest" - {
    "must place the correct request to EMS" in {
      val responseBody =
        """
          |{
          |  "uuid": "0B808CA8-40DF-4804-879B-CC9096F4600B",
          |  "requestNino": "TX961421",
          |  "matchingOutcome": "Identified a person",
          |  "addressSource": "Enterprise Matching",
          |  "matchedEntity": {}
          |}
          |""".stripMargin

      stubFor(
        post(urlEqualTo("/ems/v1/person-address-match"))
          .withHeader(ACCEPT, equalTo(ContentTypes.JSON))
          .withHeader(CONTENT_TYPE, equalTo(ContentTypes.JSON))
          .withHeader(AUTHORIZATION, equalTo("Basic dGVzdC1lbXMtY2xpZW50LWlkOnRlc3QtZW1zLXNlY3JldA=="))
          .withRequestBody(
            equalToJson(ExampleRequestController.personAddressMatchRequestBody)
          )
          .willReturn(
            aResponse()
              .withBody(responseBody)
          )
      )

      val application = buildApplication()
      running(application) {
        val request = FakeRequest(GET, routes.ExampleRequestController.exampleRequest.url)
        val result = route(application, request).value

        status(result) mustBe OK
        contentAsString(result) mustBe responseBody
      }
    }
  }

  private def buildApplication(): Application = {
    val servicesConfig = new ServicesConfig(
      Configuration.from(Map(
        "microservice.services.ems.protocol" -> "http",
        "microservice.services.ems.host" -> wireMockHost,
        "microservice.services.ems.port" -> wireMockPort,
        "microservice.services.ems.clientId" -> "test-ems-client-id",
        "microservice.services.ems.secret" -> "test-ems-secret"
      ))
    )

    new GuiceApplicationBuilder()
      .overrides(
        bind[ServicesConfig].toInstance(servicesConfig),
        bind[HttpClientV2].toInstance(httpClientV2)
      )
      .build()
  }

}
