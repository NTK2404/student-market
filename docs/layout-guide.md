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

## Thông báo sau khi redirect
Trong controller: `redirectAttributes.addFlashAttribute("success", "Đã lưu.")` (hoặc `"error"`).
Trong template, đặt dòng này ở đầu `<main>`:
```html
<div th:replace="~{layout/layout :: flash}"></div>
```

## Upload ảnh (dùng chung)
Đã có sẵn `FileStorageService` (package `service.storage`). Ảnh được lưu trong thư mục `uploads/`
(đã bị `.gitignore` bỏ qua) và truy cập qua đường dẫn `/uploads/...`.
```java
private final FileStorageService storage;   // inject qua constructor

String url = storage.storeImage(multipartFile, "listings"); // trả về "/uploads/listings/<uuid>.jpg"
// lưu url vào cột image_url trong database; trong template: th:src="@{${img.imageUrl}}"
storage.delete(url);                         // xóa file khi xóa ảnh
```
Chỉ nhận JPG/PNG/WEBP/GIF, tối đa 2MB mỗi ảnh; sai thì ném `IllegalArgumentException` với thông báo tiếng Việt.
Form upload phải có `enctype="multipart/form-data"` và dùng `th:action`.

## Thông tin người bán + đánh giá (cho Người 2 gắn vào trang chi tiết tin)

Đã có sẵn fragment `seller-card`: hiện ảnh, tên, trường, điểm đánh giá trung bình, số điện thoại/email
(chỉ hiện với người đã đăng nhập) và nút "Đánh giá người bán". Chỉ cần thêm vào template chi tiết tin:

```html
<div th:replace="~{review/fragments :: seller-card(${listing.seller.id}, ${listing.id})}"></div>
```
- Tham số 1: **id người bán**, tham số 2: **id tin đăng**.
- Nếu trang của bạn dùng DTO thì truyền `${dto.sellerId}`, `${dto.id}`. Đừng truyền cả đối tượng `listing.seller`
  (lazy-loading sẽ báo `LazyInitializationException` vì `open-in-view=false`), chỉ cần truyền id.
- Trang hồ sơ công khai của người bán: `/users/{id}` (link: `th:href="@{/users/{id}(id=${sellerId})}"`).
- Link tới form đánh giá: `/reviews/new?listingId=<id tin>`.
- Chỉ đánh giá được tin có trạng thái APPROVED hoặc SOLD, không đánh giá tin của chính mình, mỗi người một lần cho mỗi tin.
