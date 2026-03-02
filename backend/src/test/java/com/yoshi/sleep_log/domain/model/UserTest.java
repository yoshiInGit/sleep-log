package com.yoshi.sleep_log.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    @Test
    @DisplayName("コンストラクタで正しくフィールドが初期化されること")
    void constructor_ShouldInitializeFields() {
        // Arrange
        String name = "テストユーザー";
        String email = "test@example.com";
        String passwordHash = "hashed_password";

        // Act
        User user = new User(name, email, passwordHash);

        // Assert
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
    }

    @Nested
    @DisplayName("changeNameメソッドのテスト")
    class ChangeNameTest {

        @Test
        @DisplayName("有効な名前を渡すと名前が変更されること")
        void changeName_WithValidName_ShouldUpdateName() {
            // Arrange
            User user = new User("旧名", "test@example.com", "hash");
            String newName = "新名";

            // Act
            user.changeName(newName);

            // Assert
            assertEquals(newName, user.getName());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { " ", "  ", "\t", "\n" })
        @DisplayName("無効な名前（null, 空文字, 空白のみ）を渡すと例外がスローされること")
        void changeName_WithInvalidName_ShouldThrowException(String invalidName) {
            // Arrange
            User user = new User("名前", "test@example.com", "hash");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                user.changeName(invalidName);
            });
            assertEquals("Name cannot be empty", exception.getMessage());
        }
    }
}
