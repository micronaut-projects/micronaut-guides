package example.micronaut

interface UserState {
    String getUsername();

    String getPassword();

    boolean isEnabled();

    boolean isAccountExpired();

    boolean isAccountLocked();

    boolean isPasswordExpired();
}