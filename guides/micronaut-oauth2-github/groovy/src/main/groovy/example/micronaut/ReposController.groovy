/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpHeaderValues
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

import static io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper.ACCESS_TOKEN_KEY

@CompileStatic
@Controller('/repos') // <1>
class ReposController {

    public static final String CREATED = 'created'
    public static final String DESC = 'desc'
    public static final String REPOS = 'repos'

    private final GithubApiClient githubApiClient

    ReposController(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient
    }

    @Secured(SecurityRule.IS_AUTHENTICATED) // <2>
    @View('repos') // <3>
    @Get // <4>
    Map<String, Object> index(Authentication authentication) {
        List<GithubRepo> repos = githubApiClient.repos(CREATED, DESC, authorizationValue(authentication))  // <5>
        return [(REPOS): repos] as Map
    }

    private String authorizationValue(Authentication authentication) {
        Object claim = authentication.attributes[ACCESS_TOKEN_KEY]  // <6>
        if (claim instanceof String) {
            return HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER + ' ' + claim
        }
        return null
    }
}
