# Hướng dẫn dùng layout chung

Mọi trang mới đều dùng header/footer chung trong `templates/layout/layout.html`.
Copy khung dưới đây làm điểm bắt đầu cho mỗi trang (đặt trong thư mục template của bạn,
ví dụ `templates/listing/list.html`):

```html
<!DOCTYPE html>
<html lang="vi" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head th:replace="~{layout/layout :: head('Tiêu đề trang')}"></head>
<body class="d-flex flex-column min-vh-100">

<div th:replace="~{layout/layout :: header}"></div>

<main class="container flex-grow-1 py-4">
    <!-- Nội dung trang của bạn ở đây -->
</main>

<div th:replace="~{layout/layout :: footer}"></div>
<th:block th:replace="~{layout/layout :: scripts}"></th:block>
</body>
</html>
```

## Biến có sẵn trong mọi template
- `${currentUser}`: người đang đăng nhập (đối tượng `User`), `null` nếu là khách.
  Ví dụ: `${currentUser.fullName}`, `${currentUser.id}`.

## Kiểm tra quyền trong template
- `sec:authorize="isAuthenticated()"`: chỉ hiện khi đã đăng nhập
- `sec:authorize="!isAuthenticated()"`: chỉ hiện với khách
- `sec:authorize="hasRole('ADMIN')"`: chỉ hiện với admin

## Lấy người dùng hiện tại trong Controller
```java
@GetMapping("/my-listings")
public String myListings(Authentication auth) {
    String email = auth.getName(); // email của người đăng nhập
    ...
}
```

## Phân quyền đường dẫn
Cấu hình trong `SecurityConfig.java` (do Người 1 giữ). Mặc định mọi đường dẫn đều
**yêu cầu đăng nhập**, trừ: `/`, `/listings`, `/listings/{số}`, `/register`, `/login`,
và file tĩnh. `/admin/**` chỉ dành cho ADMIN. Cần mở thêm đường dẫn công khai thì nhắn Người 1.

## Form POST phải dùng `th:action`
Dùng `<form th:action="@{/duong-dan}" method="post">` để Thymeleaf tự thêm token CSRF.
Nếu viết `action="/duong-dan"` thường, form POST sẽ bị lỗi 403.
