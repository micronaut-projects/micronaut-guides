package example.micronaut

interface AuthoritiesFetcher {
    List<String> findAuthoritiesByUsername(String username)
}
