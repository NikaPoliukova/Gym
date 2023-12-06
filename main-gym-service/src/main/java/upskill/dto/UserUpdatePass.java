package upskill.dto;

public record UserUpdatePass(
    String username,
    String oldPassword,
    String newPassword
) {
}
