package example.micronaut

import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.context.annotation.Property
import io.micronaut.security.token.generator.TokenGenerator
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator
import io.micronaut.security.token.claims.ClaimsGenerator
import io.micronaut.security.token.jwt.signature.SignatureGeneratorConfiguration
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureGenerator
import io.micronaut.security.token.validator.TokenValidator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

// <1>
@Property(name = 'jwk.primary', value = '{"p":"_OZyH1Mk3wR0oXw1C31t4kWOcaHFB6Njro1cYx52REnPiznn_JTtwvlAMpvV6LVCIZPgKMzdIEMY1gYs1LsO-5IFqWwegXmYJ0iKXbRrZshfWBCzRLK3QK5fER1le1XUBDhtDk7KIW_Xg-SZF4pf_LUEVKMnyUpspGI5F77jlJ8","kty":"RSA","q":"s9wvl7z8vkHQvo9xOUp-z0a2Z7LFBDil2uIjPh1FQzs34gFXH8dQPRox83TuN5d4KzdLPqQNQAfMXU9_KmxihNb_qDQahYugeELmcem04munxXqBdyZqWhWCy5YmujYqn44irwvoTbw6_RkMqjCmINPTPadptlPivsZ6RhKn8zk","d":"ok3wmhOy8NZEHAotnFiH6ecFD6xf_9x33_fMRkqa3_KE8NZM7vmvNgElox2UvcP_2K5E7jOdL2XQdJCTIW3Qlj66yE2a84SYlbvxIc4hDrIog0XNt4FhavvshxxUIfDQo6Q8qXDR5v7nwt6SCopYC3t3KVRdJh08GzKoVxysd7afJjxXxx178gY29uMRqnwxFN1OGnWaiBr-xGKb1frJ6jOI1zvuuCaljZ4aZjc9vOR4y9ZmobgrzkMFnpDAmQZ7MWcVMyodRMOA2dEOckywPhg-dIVNiVIqzJqe5Yg1ilNookjwtqj2TpNU7Z9gPqzYB73PmQ2p5LMDheAPxcOmEQ","e":"AQAB","use":"sig","kid":"e3be37177a7c42bcbadd7cc63715f216","qi":"r--nAtaYPAgJq_8R1-kynpd53E17n-loDUgtVWBCx_RmdORX4Auilv1S83dD1mbcnYCbV_LmxiEjOiz-4gS_E0qVGqakAqQrO1hVUvJa_Y2uftDgwFmuJNGbpRU-K4Td_uUzdm48za8yJCgOdYsWp6PNMCcmQgiInzkR3XYV83I","dp":"oQUcvmMSw8gzdin-IB2xW_MLecAVEgLu0dGBdD6N8HbKZQvub_xm0dAfFtnvvWXDAFwFyhR96i-uXX67Bos_Q9-6KSAE4E0KGmDucDESfPOw-QJREbl0QgOD1gLQfVGtVy6SCR0TR2zNXFWtP7bD3MNoSXdEOr5fI97CGSNaBWM","alg":"RS256","dq":"DM-WJDy10-dkMu6MpgQEXEcxHtnA5rgSODD7SaVUFaHWLSbjScQslu2SuUCO5y7GxG0_0spklzb2-356FE98BPI7a4Oqj_COEYLSXzLCS45XeN1s80utL5Vwp4eeYo0RJCQ_nDBA76iEmxp5qHWmn5f25-FQykfXUrdYZj1V8SE","n":"sa6m2i-iNvj6ZSTdSHZaBrnv6DId4AqAXhOyl0yA5fNWYe6r51h24SXqk7DsGYHHh74ii74tP1lTpmy6RD67tCK-tbN-d6yc4Z6FfM8R83v2QZUfaAixgHGtw0n2toqsiHf6EloDV-B8q4GYyKDD6cLecoaIuTmMBTY3kts59U2t9W10YoLGsmFqLSz8qNF5HkahzB6_--2DiBfVGUKAXHC-SICGZCi-8efOetv6pt9vFiWEgwU_DgjRNYzLFt1SEmbGFUU4kbjQ7tNTMkHfzfwcT6qLt4kVKy2FNYsEMk24keWtCvW_RyO_fisZc0W9smX7WtYjEXhcAjDeqHgEZw"}')
@Property(name = 'jwk.secondary', value = '{"p":"4qJ9RNlu6SuDT_MLArfzimvKEwmet_j12Z9EQeb5nMjZIOHTcWw__duebUytfWwxsRHhtSVXeMt-EryQAOulm2p1bfiVuparq93z9P5cPnb0oArFaw3eFNFEmX5U-lY8PzUTTsFxO4aVQYAKXD6DP7p5uPzuwpHFuNc71nNIXZE","kty":"RSA","q":"v4OhkWMbS_nq77HFanwZAT_obfJuQfOFOQBORL4ATAHGUXm2y4YqLNExZs7Wj1MA_6ya6Y00s2JBM7fWq_fPe4d9xo5aGrPdcp0G8W21kkfh9vuVPlHVQTgSP7FQ9qahvXxNwK_11yNr3p1HBmScJ5mHlMBpIJsFcvHA-uXe0Ps","d":"EunrjnQ1-jJPSCrt2L94PUpDrakup8a4pXys52YSkJY-W6XidM0roOS6kr06P3G6VQgc6AL_BkvTQ_XS0oXHbXVprDQ5Syam5p9oxHBhhW_vSqIMgUOfm28uyB3Mtw9rBxdUxW3yElHioaR8a-exYhhyVXb1QEhxL_rcnthmhAkM2NcHi2UnxGKFTsC0abQ2MuQc1OAuW5veDiIF2hfdC41qE0_d8vB6FDWbblgUpbwB6uSZaViPs15Buq2oX9dCCw54-PgzkfehDt7lyqgupktbV1psnVVhL86shzt4QFnhd3k7VpFbjCNFtiJTrufV-XBWT0pl2w3VR9wrHJ1bYQ","e":"AQAB","use":"sig","kid":"0794e938379540dc8eaa559508524a79","qi":"jy-TNyXVy_44_n4KGAwIbZO2C4r6uNWuEdehBfQKkPhiP90myG1KZVfOoKNOK9bCv2mvZJcBz4c1ArElgpuSCV4-KFac1ZzQo_ic5aoIej8Qa80y2ogc-_Yv6_ZLHC1S76M-lm4jayk2-rvuBpy2pUvHbW6Srhs_szwz7ZfSkLg","dp":"ApqdV9ortRAj7Ro8ySY17SQ56SgWI8T_hiWXUi6GNa_1FrShik8VGSSZ2GWmJKfGlmM_NaadL60e4LY77VbHy1ZYzQ-rIL60cEAXmnwFsU4Kl4AoLoe1QoX5BM53yXyOKqfAdgow898i_eKru82YEnZhCagWUjP8kpgefuNKNJE","alg":"RS256","dq":"bFF78WoXh0pMCdQHL2oPDnjh8kWa_OxKHmpA2nqIWnTqgSyRKd2xPvX2tgooqpmsx-8NEymNdCQPcrv4y_z2OgzxI3tiFRZEGs4bnjOJ7bmAYZv71mqcbi3TjHiyrT6j3jNPGrurFUpweVGFWWVQOMmKOKT3ELz9QPzhREb9Vj8","n":"qYvDpV8DRU5hx9eXpE4Ms8nUXicEwrxUUz5gb5gkXpIeY82mqfQKKCP6PSFnkKYtRFTOUSm9cgGGfOd7O4NFsIsxLwXCj34X7ORr19eXKBLvG3bZJLxqRlbYuQshDMkQOui1sDBxvYnj5p4iHne6l2btH5grHOCShUWG-bKps5Y8bKNHod1pIOOBabVCmn3sUVUkZw8nyXkQqZbv-c8x6z0TEfhNOPOIt2AmmlNgrE_8g7-dnCvqfJnhv0c7qkOJzsb7OMmvVwsQNiM59D6uaWZr-vdANo6NggiZmCKUS3tpUvdXW7ec9WMPJWhrVEkRcbWXQnZ_C7pXFrz7rLeNKw"}')
@MicronautTest(startApplication = false)
class ValidateSecondarySignedJwtSpec extends Specification {

    @Inject
    ClaimsGenerator claimsGenerator

    @Inject
    JwkConfiguration jwkConfiguration

    @Inject
    TokenValidator tokenValidator

    void canValidateAJWebTokenSignedWithTheSecondaryJsonWebKey() {

        when:
        SignatureGeneratorConfiguration signatureGeneratorConfiguration = new RSASignatureGenerator(new MockSignatureGenerator(jwkConfiguration))
        TokenGenerator tokenGenerator = new JwtTokenGenerator(signatureGeneratorConfiguration, null, claimsGenerator)

        Optional<String> jwtOptional = tokenGenerator.generateToken(sub: 'sergio')

        then:
        jwtOptional.isPresent()

        when:
        String jwtString = jwtOptional.get()
        JWTParser.parse(jwtString)

        then:
        noExceptionThrown()

        when:
        JWT jwt = JWTParser.parse(jwtString)

        then:
        jwt instanceof SignedJWT

        when:
        tokenValidator.validateToken(jwtString, null)

        then:
        noExceptionThrown()
    }

    static class MockSignatureGenerator extends AbstractRSAGeneratorSignatureConfiguration {
        MockSignatureGenerator(JwkConfiguration jwkConfiguration) {
            super(jwkConfiguration.secondary)
        }
    }
}
