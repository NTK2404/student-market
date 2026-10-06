# Student Market (Chợ đồ cũ sinh viên)

Spring Boot 3 + Thymeleaf + Spring Security + JPA + MySQL.

## Chạy thử
1. Cài JDK 17+, Maven, MySQL.
2. Sửa `spring.datasource.username/password` trong `src/main/resources/application.properties`.
3. `mvn spring-boot:run`, rồi mở http://localhost:8080
4. Tài khoản admin mẫu: `admin@market.local` / `admin123`

## Cấu trúc
- `entity/`      các bảng (khớp ERD)
- `enums/`       trạng thái (UserStatus, ListingStatus, ...)
- `repository/`  Spring Data JPA
- `config/`      Security, nạp dữ liệu khởi tạo
- `controller/`  nhận request, trả view
- (sẽ thêm) `service/`, `dto/`

## Quy ước Git
Mỗi người một nhánh `feature/<ten>`, merge vào `main` qua pull request.
