-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 05, 2026 lúc 05:09 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `student_market`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`id`, `name`, `parent_id`) VALUES
(1, 'Sách & giáo trình', NULL),
(2, 'Đồ điện tử', NULL),
(3, 'Quần áo', NULL),
(4, 'Đồ dùng học tập', NULL),
(5, 'Xe cộ', NULL),
(6, 'Khác', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `favorites`
--

CREATE TABLE `favorites` (
  `created_at` datetime(6) DEFAULT NULL,
  `listing_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `listings`
--

CREATE TABLE `listings` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `item_condition` enum('LIKE_NEW','NEW','USED') NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `price` decimal(12,0) NOT NULL,
  `status` enum('APPROVED','HIDDEN','PENDING','REJECTED','SOLD') NOT NULL,
  `title` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `view_count` int(11) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL,
  `seller_id` bigint(20) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `listings`
--

INSERT INTO `listings` (`id`, `created_at`, `description`, `item_condition`, `location`, `price`, `status`, `title`, `updated_at`, `view_count`, `category_id`, `seller_id`, `image_url`) VALUES
(1, NULL, 'Đĩa chơi xong cất hộp nên đĩa còn mới nguyên, không xước dăm. Cốt truyện siêu cuốn', 'LIKE_NEW', NULL, 600000, 'APPROVED', 'Đĩa game Ghost of Tsushima PS4 (Bản Director\'s Cut)', '2026-11-04 21:27:48.000000', 0, 2, 1, NULL),
(2, '2026-10-13 08:23:58.000000', 'Máy vừa ra mắt, hàng xách tay nguyên seal chưa bóc hộp. Trải nghiệm đồ họa đỉnh cao thế hệ mới.', 'NEW', NULL, 12500000, 'APPROVED', 'Nintendo Switch 2 Mới 100% Nguyên Seal', '2026-11-01 23:23:51.000000', 1, 1, 1, NULL),
(3, '2026-10-13 08:23:58.000000', 'Cần pass lại Laptop Asus cũ. Ngoại hình xước xát nhẹ nhưng máy vẫn chạy cực mượt, cấu hình khỏe phù hợp cho các bạn sinh viên chạy máy ảo làm lab IAA, IAM302 hoặc soạn báo cáo đồ án Word.', 'USED', NULL, 8500000, 'APPROVED', 'Laptop Asus cũ cấu hình cao', '2026-10-19 22:16:39.000000', 0, 1, 1, NULL),
(9, '2026-10-14 19:12:35.000000', 'Có sẵn Savefile với 6 huy hiệu. Shiny Latios', 'USED', NULL, 500000, 'APPROVED', 'Bán đĩa game Pokémon Omega Ruby', '2026-11-04 22:41:42.000000', 1, 1, 1, '512889a0-8b8b-45d1-a725-3de464c471b1_Latios.jpg'),
(12, '2026-11-04 22:16:25.000000', 'dhaidqfbhjdbgfgf', 'NEW', 'KTX @222', 5000000000, 'APPROVED', 'máy tính.lol', '2026-11-04 22:16:33.000000', 0, 1, 1, NULL),
(13, '2026-11-04 22:40:28.000000', '42354365476758', 'NEW', 'KTX @222', 59567766, 'APPROVED', 'Áo + Quần heheheha', '2026-11-04 22:40:34.000000', 0, 3, 1, NULL),
(14, '2026-11-04 23:07:40.000000', 'y871468741', 'USED', 'KTX @222', 43244256, 'APPROVED', 'quần áo . lol', '2026-11-04 23:07:46.000000', 0, 2, 1, NULL),
(15, '2026-11-04 23:48:05.000000', 'ưqrqrwgwrhgg', 'USED', 'KTX @222', 5346576588, 'APPROVED', 'xe mayyyyyyyyyyy', '2026-11-04 23:48:11.000000', 0, 5, 1, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `listing_images`
--

CREATE TABLE `listing_images` (
  `id` bigint(20) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `is_primary` bit(1) DEFAULT NULL,
  `listing_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `messages`
--

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL,
  `content` text NOT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `sent_at` datetime(6) DEFAULT NULL,
  `listing_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `sender_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `expires_at` datetime(6) NOT NULL,
  `token_hash` varchar(64) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `reports`
--

CREATE TABLE `reports` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `reason` varchar(255) NOT NULL,
  `status` enum('DISMISSED','OPEN','RESOLVED') NOT NULL,
  `listing_id` bigint(20) NOT NULL,
  `reporter_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `reports`
--

INSERT INTO `reports` (`id`, `created_at`, `reason`, `status`, `listing_id`, `reporter_id`) VALUES
(1, '2026-10-11 10:00:00.000000', 'Đăng bán hàng giả, hàng kém chất lượng', 'RESOLVED', 1, 0),
(2, '2026-10-11 11:30:00.000000', 'Ngôn từ tục tĩu, xúc phạm người mua', 'RESOLVED', 1, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `reviews`
--

CREATE TABLE `reviews` (
  `id` bigint(20) NOT NULL,
  `comment` text DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `listing_id` bigint(20) NOT NULL,
  `reviewer_id` bigint(20) NOT NULL,
  `seller_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(2, 'ADMIN'),
(1, 'USER');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','LOCKED') NOT NULL,
  `university` varchar(255) DEFAULT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `avatar_url`, `created_at`, `email`, `full_name`, `password_hash`, `phone`, `status`, `university`, `role_id`) VALUES
(1, NULL, '2026-10-11 15:35:14.000000', 'admin@market.local', 'Quản trị viên', '$2a$10$tc6Y539d9jzBgeQ3.XYjOumKDVE2fN1zMRry8yk5NAGFenCLcNkde', NULL, 'ACTIVE', NULL, 2);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsaok720gsu4u2wrgbk10b5n8d` (`parent_id`);

--
-- Chỉ mục cho bảng `favorites`
--
ALTER TABLE `favorites`
  ADD PRIMARY KEY (`listing_id`,`user_id`),
  ADD KEY `FKk7du8b8ewipawnnpg76d55fus` (`user_id`);

--
-- Chỉ mục cho bảng `listings`
--
ALTER TABLE `listings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_listing_status` (`status`),
  ADD KEY `idx_listing_category` (`category_id`),
  ADD KEY `idx_listing_price` (`price`),
  ADD KEY `FKdfglshentvek3cnsr7a4nwh7j` (`seller_id`);

--
-- Chỉ mục cho bảng `listing_images`
--
ALTER TABLE `listing_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKq8bkplrutxrfur5u7pppml04y` (`listing_id`);

--
-- Chỉ mục cho bảng `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKq4pvtu9s5dw5igbibathqr8ea` (`listing_id`),
  ADD KEY `FKt05r0b6n0iis8u7dfna4xdh73` (`receiver_id`),
  ADD KEY `FK4ui4nnwntodh6wjvck53dbk9m` (`sender_id`);

--
-- Chỉ mục cho bảng `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKajre85ybxavf1tt4omkrs5p6g` (`token_hash`),
  ADD KEY `FKk3ndxg5xp6v7wd4gjyusp15gq` (`user_id`);

--
-- Chỉ mục cho bảng `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7sds0ukyd61m6upcrdbt0xwmq` (`listing_id`),
  ADD KEY `FKd3qiw2om5d2oh5xb7fbdcq225` (`reporter_id`);

--
-- Chỉ mục cho bảng `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6oe9atp73j2w9h9wxgqxpxrpa` (`reviewer_id`,`listing_id`),
  ADD KEY `FKef8fmvjeprcadspus2mmbo1aa` (`listing_id`),
  ADD KEY `FKouykkgwv4jl9590w2neamcktb` (`seller_id`);

--
-- Chỉ mục cho bảng `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `listings`
--
ALTER TABLE `listings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `listing_images`
--
ALTER TABLE `listing_images`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `messages`
--
ALTER TABLE `messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `reports`
--
ALTER TABLE `reports`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`);

--
-- Các ràng buộc cho bảng `favorites`
--
ALTER TABLE `favorites`
  ADD CONSTRAINT `FKk7du8b8ewipawnnpg76d55fus` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKoc425aeu56owvlscxwvuwsu4n` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`);

--
-- Các ràng buộc cho bảng `listings`
--
ALTER TABLE `listings`
  ADD CONSTRAINT `FKbyfn7t3i7g0f11pcy5veh088p` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  ADD CONSTRAINT `FKdfglshentvek3cnsr7a4nwh7j` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `listing_images`
--
ALTER TABLE `listing_images`
  ADD CONSTRAINT `FKq8bkplrutxrfur5u7pppml04y` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`);

--
-- Các ràng buộc cho bảng `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `FK4ui4nnwntodh6wjvck53dbk9m` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKq4pvtu9s5dw5igbibathqr8ea` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`),
  ADD CONSTRAINT `FKt05r0b6n0iis8u7dfna4xdh73` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD CONSTRAINT `FKk3ndxg5xp6v7wd4gjyusp15gq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `FK7sds0ukyd61m6upcrdbt0xwmq` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`),
  ADD CONSTRAINT `FKd3qiw2om5d2oh5xb7fbdcq225` FOREIGN KEY (`reporter_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `FKd1isgfajhtdl8mgg29up6mofi` FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKef8fmvjeprcadspus2mmbo1aa` FOREIGN KEY (`listing_id`) REFERENCES `listings` (`id`),
  ADD CONSTRAINT `FKouykkgwv4jl9590w2neamcktb` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
