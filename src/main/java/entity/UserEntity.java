package entity;

import java.time.LocalDateTime;

public class UserEntity {
	private Integer userId;
	private String username;
	private String password;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// デフォルトコンストラクタ
	public UserEntity() {
	}

	// コンストラクタ
	public UserEntity(Integer userId, String username, String email) {
		this.userId = userId;
		this.username = username;
		this.email = email;
	}

	// Getter/Setter
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", username=" + username + ", email=" + email + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
}