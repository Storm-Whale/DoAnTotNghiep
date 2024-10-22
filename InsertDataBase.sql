INSERT INTO kich_co ( ten_kich_co, trang_thai, ngay_tao, ngay_sua )
VALUES
	( 'S', 1, '2023-11-23', '2023-11-23' ),
	( 'M', 1, '2023-11-23', '2023-11-23' ),
	( 'L', 1, '2023-11-23', '2023-11-23' ),
	( 'XL', 1, '2023-11-23', '2023-11-23' ),
	( 'XXL', 1, '2023-11-23', '2023-11-23' );
	
INSERT INTO mau_sac ( ten_mau_sac, trang_thai, ngay_tao, ngay_sua )
VALUES
	( N'Đỏ', 1, '2023-11-23', '2023-11-23' ),
	( N'Xanh', 1, '2023-11-23', '2023-11-23' ),
	( N'Vàng', 1, '2023-11-23', '2023-11-23' ),
	( N'Tím', 1, '2023-11-23', '2023-11-23' ),
	( N'Đen', 1, '2023-11-23', '2023-11-23' );
INSERT INTO chat_lieu ( ten_chat_lieu, trang_thai, ngay_tao, ngay_sua )
VALUES
	( N'Cotton', 1, '2023-11-23', '2023-11-23' ),
	( N'Polyester', 1, '2023-11-23', '2023-11-23' ),
	( N'Len', 1, '2023-11-23', '2023-11-23' ),
	( N'Da', 1, '2023-11-23', '2023-11-23' ),
	( N'Lụa', 1, '2023-11-23', '2023-11-23' );
INSERT INTO kieu_tay_ao ( ten_tay_ao, trang_thai, ngay_tao, ngay_sua )
VALUES
	( N'Tay ngắn', 1, '2023-11-23', '2023-11-23' ),
	( N'Tay dài', 1, '2023-11-23', '2023-11-23' ),
	( N'Tay lỡ', 1, '2023-11-23', '2023-11-23' ),
	( N'Tay cánh tiên', 1, '2023-11-23', '2023-11-23' ),
	( N'Tay trùm', 1, '2023-11-23', '2023-11-23' );
INSERT INTO thuong_hieu ( ten_thuong_hieu, trang_thai, ngay_tao, ngay_sua )
VALUES
	( N'Vietjet Collection', 1, '2023-11-23', '2023-11-23' ),
	( N'Ivan Trần', 1, '2023-11-23', '2023-11-23' ),
	( N'Lien Fashion', 1, '2023-11-23', '2023-11-23' ),
	( N'Oh!Mia', 1, '2023-11-23', '2023-11-23' ),
	( N'Thái Tuấn', 1, '2023-11-23', '2023-11-23' );
INSERT INTO kieu_co_ao ( ten_co_ao, trang_thai, ngay_tao, ngay_sua )
VALUES
	( N'Cổ tròn', 1, '2023-11-23', '2023-11-23' ),
	( N'Cổ trụ', 1, '2023-11-23', '2023-11-23' ),
	( N'Cổ tim', 1, '2023-11-23', '2023-11-23' ),
	( N'Cổ thuyền', 1, '2023-11-23', '2023-11-23' ),
	( N'Cổ sơ mi', 1, '2023-11-23', '2023-11-23' );
INSERT INTO san_pham ( ten_san_pham, anh_url, id_thuong_hieu, id_chat_lieu, id_tay_ao, id_co_ao, trang_thai, mo_ta, ngay_tao, ngay_sua )
VALUES
	( N'Áo Thun S', 'ao1', 1, 1, 1, 1, 1, N'Áo thun size S, màu Đỏ, chất liệu Cotton, tay ngắn, cổ tròn', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun M', 'ao2', 2, 2, 2, 2, 1, N'Áo thun size M, màu Xanh, chất liệu Polyester, tay dài, cổ trụ', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun L', 'ao3', 3, 3, 3, 3, 1, N'Áo thun size L, màu Vàng, chất liệu Len, tay lỡ, cổ tim', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XL', 'ao4', 4, 4, 4, 4, 1, N'Áo thun size XL, màu Tím, chất liệu Da, tay cánh tiên, cổ thuyền', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XXL', 'ao5', 5, 5, 5, 5, 1, N'Áo thun size XXL, màu Đen, chất liệu Lụa, tay trùm, cổ sơ mi', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun S', 'ao6', 1, 1, 1, 1, 1, N'Áo thun size S, màu Đỏ, chất liệu Cotton, tay ngắn, cổ tròn', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun M', 'ao7', 2, 2, 2, 2, 1, N'Áo thun size M, màu Xanh, chất liệu Polyester, tay dài, cổ trụ', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun L', 'ao8', 3, 3, 3, 3, 1, N'Áo thun size L, màu Vàng, chất liệu Len, tay lỡ, cổ tim', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XL', 'ao9', 4, 4, 4, 4, 1, N'Áo thun size XL, màu Tím, chất liệu Da, tay cánh tiên, cổ thuyền', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XXL', 'ao10', 5, 5, 5, 5, 1, N'Áo thun size XXL, màu Đen, chất liệu Lụa, tay trùm, cổ sơ mi', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun S', 'ao11', 1, 1, 1, 1, 1, N'Áo thun size S, màu Đỏ, chất liệu Cotton, tay ngắn, cổ tròn', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun M', 'ao12', 2, 2, 2, 2, 1, N'Áo thun size M, màu Xanh, chất liệu Polyester, tay dài, cổ trụ', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun L', 'ao13', 3, 3, 3, 3, 1, N'Áo thun size L, màu Vàng, chất liệu Len, tay lỡ, cổ tim', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XL', 'ao14', 4, 4, 4, 4, 1, N'Áo thun size XL, màu Tím, chất liệu Da, tay cánh tiên, cổ thuyền', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XXL', 'ao15', 5, 5, 5, 5, 1, N'Áo thun size XXL, màu Đen, chất liệu Lụa, tay trùm, cổ sơ mi', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun S', 'ao16', 1, 1, 1, 1, 1, N'Áo thun size S, màu Đỏ, chất liệu Cotton, tay ngắn, cổ tròn', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun M', 'ao17', 2, 2, 2, 2, 1, N'Áo thun size M, màu Xanh, chất liệu Polyester, tay dài, cổ trụ', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun L', 'ao18', 3, 3, 3, 3, 1, N'Áo thun size L, màu Vàng, chất liệu Len, tay lỡ, cổ tim', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XL', 'ao19', 4, 4, 4, 4, 1, N'Áo thun size XL, màu Tím, chất liệu Da, tay cánh tiên, cổ thuyền', '2023-11-23', '2023-11-23' ),
	( N'Áo Thun XXL', 'ao20', 5, 5, 5, 5, 1, N'Áo thun size XXL, màu Đen, chất liệu Lụa, tay trùm, cổ sơ mi', '2023-11-23', '2023-11-23' );
INSERT INTO san_pham_chi_tiet ( id_san_pham, id_kich_co, id_mau_sac, gia, so_luong, trang_thai, ngay_tao, ngay_sua )
VALUES
	( 7, 1, 1, 100000, 10, 1, '2023-11-23', '2023-11-23' ),
	( 3, 2, 2, 120000, 15, 1, '2023-11-23', '2023-11-23' ),
	( 12, 3, 3, 110000, 20, 1, '2023-11-23', '2023-11-23' ),
	( 1, 4, 4, 130000, 25, 1, '2023-11-23', '2023-11-23' ),
	( 9, 5, 5, 150000, 30, 1, '2023-11-23', '2023-11-23' ),
	( 5, 1, 2, 100000, 5, 1, '2023-11-23', '2023-11-23' ),
	( 14, 2, 3, 120000, 15, 1, '2023-11-23', '2023-11-23' ),
	( 17, 3, 4, 110000, 20, 1, '2023-11-23', '2023-11-23' ),
	( 8, 4, 5, 130000, 25, 1, '2023-11-23', '2023-11-23' ),
	( 6, 5, 1, 150000, 30, 1, '2023-11-23', '2023-11-23' ),
	( 2, 1, 1, 100000, 12, 1, '2023-11-23', '2023-11-23' ),
	( 18, 2, 2, 120000, 18, 1, '2023-11-23', '2023-11-23' ),
	( 11, 3, 3, 110000, 22, 1, '2023-11-23', '2023-11-23' ),
	( 4, 4, 4, 130000, 27, 1, '2023-11-23', '2023-11-23' ),
	( 19, 5, 5, 150000, 32, 1, '2023-11-23', '2023-11-23' ),
	( 13, 1, 2, 100000, 8, 1, '2023-11-23', '2023-11-23' ),
	( 15, 2, 3, 120000, 12, 1, '2023-11-23', '2023-11-23' ),
	( 10, 3, 4, 110000, 18, 1, '2023-11-23', '2023-11-23' ),
	( 20, 4, 5, 130000, 22, 1, '2023-11-23', '2023-11-23' ),
	( 16, 5, 1, 150000, 30, 1, '2023-11-23', '2023-11-23' ),
	( 1, 1, 1, 100000, 10, 1, '2023-11-23', '2023-11-23' ),
	( 3, 2, 2, 120000, 15, 1, '2023-11-23', '2023-11-23' ),
	( 19, 3, 3, 110000, 20, 1, '2023-11-23', '2023-11-23' ),
	( 4, 4, 4, 130000, 25, 1, '2023-11-23', '2023-11-23' ),
	( 5, 5, 5, 150000, 30, 1, '2023-11-23', '2023-11-23' ),
	( 6, 1, 2, 100000, 5, 1, '2023-11-23', '2023-11-23' ),
	( 14, 2, 3, 120000, 15, 1, '2023-11-23', '2023-11-23' ),
	( 11, 3, 4, 110000, 20, 1, '2023-11-23', '2023-11-23' ),
	( 17, 4, 5, 130000, 25, 1, '2023-11-23', '2023-11-23' ),
	( 9, 5, 1, 150000, 30, 1, '2023-11-23', '2023-11-23' ),
	( 8, 1, 1, 100000, 12, 1, '2023-11-23', '2023-11-23' ),
	( 15, 2, 2, 120000, 18, 1, '2023-11-23', '2023-11-23' ),
	( 12, 3, 3, 110000, 22, 1, '2023-11-23', '2023-11-23' ),
	( 3, 4, 4, 130000, 27, 1, '2023-11-23', '2023-11-23' ),
	( 19, 5, 5, 150000, 32, 1, '2023-11-23', '2023-11-23' ),
	( 2, 1, 2, 100000, 8, 1, '2023-11-23', '2023-11-23' ),
	( 16, 2, 3, 120000, 12, 1, '2023-11-23', '2023-11-23' ),
	( 4, 3, 4, 110000, 18, 1, '2023-11-23', '2023-11-23' ),
	( 7, 4, 5, 130000, 22, 1, '2023-11-23', '2023-11-23' ),
	( 1, 5, 1, 150000, 30, 1, '2023-11-23', '2023-11-23' ),
	( 3, 1, 1, 100000, 10, 1, '2023-11-23', '2023-11-23' ),
	( 18, 2, 2, 120000, 15, 1, '2023-11-23', '2023-11-23' ),
	( 13, 3, 3, 110000, 20, 1, '2023-11-23', '2023-11-23' ),
	( 2, 4, 4, 130000, 25, 1, '2023-11-23', '2023-11-23' ),
	( 19, 5, 5, 150000, 30, 1, '2023-11-23', '2023-11-23' ),
	( 4, 1, 2, 100000, 5, 1, '2023-11-23', '2023-11-23' ),
	( 11, 2, 3, 120000, 15, 1, '2023-11-23', '2023-11-23' ),
	( 8, 3, 4, 110000, 20, 1, '2023-11-23', '2023-11-23' ),
	( 5, 4, 5, 130000, 25, 1, '2023-11-23', '2023-11-23' ),
	( 7, 5, 1, 150000, 30, 1, '2023-11-23', '2023-11-23' );
INSERT INTO anh_san_pham ( anh_url, id_spct, trang_thai, ngay_tao, ngay_sua )
VALUES
	( 'ao1.png', 10, 1, '2023-11-23', '2023-11-23' ),
	( 'ao2.png', 25, 1, '2023-11-23', '2023-11-23' ),
	( 'ao3.png', 30, 1, '2023-11-23', '2023-11-23' ),
	( 'ao4.png', 5, 1, '2023-11-23', '2023-11-23' ),
	( 'anh5.png', 15, 1, '2023-11-23', '2023-11-23' ),
	( 'ao6.png', 22, 1, '2023-11-23', '2023-11-23' ),
	( 'ao7.png', 8, 1, '2023-11-23', '2023-11-23' ),
	( 'ao8.png', 28, 1, '2023-11-23', '2023-11-23' ),
	( 'ao9.png', 33, 1, '2023-11-23', '2023-11-23' ),
	( 'ao10.png', 12, 1, '2023-11-23', '2023-11-23' ),
	( 'ao11.png', 19, 1, '2023-11-23', '2023-11-23' ),
	( 'ao12.png', 7, 1, '2023-11-23', '2023-11-23' ),
	( 'ao13.png', 14, 1, '2023-11-23', '2023-11-23' ),
	( 'ao14.png', 20, 1, '2023-11-23', '2023-11-23' ),
	( 'ao15.png', 18, 1, '2023-11-23', '2023-11-23' ),
	( 'ao16.png', 26, 1, '2023-11-23', '2023-11-23' ),
	( 'ao17.png', 3, 1, '2023-11-23', '2023-11-23' ),
	( 'ao18.png', 29, 1, '2023-11-23', '2023-11-23' ),
	( 'ao19.png', 4, 1, '2023-11-23', '2023-11-23' ),
	( 'ao20.png', 6, 1, '2023-11-23', '2023-11-23' ),
	( 'ao21.png', 31, 1, '2023-11-23', '2023-11-23' ),
	( 'ao22.png', 1, 1, '2023-11-23', '2023-11-23' ),
	( 'ao23.png', 13, 1, '2023-11-23', '2023-11-23' ),
	( 'ao24.png', 17, 1, '2023-11-23', '2023-11-23' ),
	( 'ao25.png', 9, 1, '2023-11-23', '2023-11-23' ),
	( 'ao26.png', 11, 1, '2023-11-23', '2023-11-23' ),
	( 'ao27.png', 32, 1, '2023-11-23', '2023-11-23' ),
	( 'ao28.png', 16, 1, '2023-11-23', '2023-11-23' ),
	( 'ao29.png', 27, 1, '2023-11-23', '2023-11-23' ),
	( 'ao30.png', 24, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 2, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 23, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 34, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 35, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 36, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 37, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 38, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 39, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 40, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 41, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 42, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 43, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 44, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 45, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 46, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 47, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 48, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 49, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 50, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 5, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 12, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 18, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 30, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 24, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 2, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 36, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 45, 1, '2023-11-23', '2023-11-23' ),
	( 'anh1.jpg', 3, 1, '2023-11-23', '2023-11-23' ),
	( 'anh2.jpg', 14, 1, '2023-11-23', '2023-11-23' ),
	( 'anh3.jpg', 29, 1, '2023-11-23', '2023-11-23' );

	-- Insert into phieu_giam_gia (Discount Voucher)
INSERT INTO phieu_giam_gia (ma, ten_phieu_giam_gia, so_luong, dieu_kien, kieu_giam_gia, ngay_bat_dau, ngay_ket_thuc, gia_tri_giam, gia_tri_max, trang_thai, ngay_tao, ngay_sua)
VALUES
    ('DISCOUNT10', N'Giảm 10%', 100, 500000, 1, '2023-11-01', '2023-12-01', 50000, 100000, 1, '2023-11-23', '2023-11-23'),
    ('DISCOUNT20', N'Giảm 20%', 50, 1000000, 1, '2023-11-01', '2023-12-01', 100000, 200000, 1, '2023-11-23', '2023-11-23'),
    ('FREESHIP', N'Miễn phí vận chuyển', 200, 0, 2, '2023-11-01', '2023-12-01', 0, 50000, 1, '2023-11-23', '2023-11-23'),
    ('SALE50', N'Giảm 50%', 30, 2000000, 1, '2023-11-01', '2023-12-01', 100000, 300000, 1, '2023-11-23', '2023-11-23'),
    ('NEWYEAR2024', N'Giảm đầu năm 2024', 150, 300000, 1, '2024-01-01', '2024-01-31', 20000, 100000, 1, '2023-11-23', '2023-11-23');

-- Insert into phuong_thuc_thanh_toan (Payment Methods)
INSERT INTO phuong_thuc_thanh_toan (phuong_thuc_thanh_toan, trang_thai, ngay_tao, ngay_sua)
VALUES
    (N'Thẻ tín dụng', 1, '2023-11-23', '2023-11-23'),
    (N'Thanh toán khi nhận hàng', 1, '2023-11-23', '2023-11-23'),
    (N'Ví điện tử', 1, '2023-11-23', '2023-11-23'),
    (N'Thẻ ghi nợ', 1, '2023-11-23', '2023-11-23'),
    (N'Chuyển khoản ngân hàng', 1, '2023-11-23', '2023-11-23');

-- Insert into vai_tro (Roles)
INSERT INTO vai_tro (ten_vai_tro, trang_thai, ngay_tao, ngay_sua)
VALUES
    (N'Quản lý', 1, '2023-11-23', '2023-11-23'),
    (N'Nhân viên', 1, '2023-11-23', '2023-11-23'),
    (N'Khách hàng', 1, '2023-11-23', '2023-11-23'),
    (N'Kế toán', 1, '2023-11-23', '2023-11-23'),
    (N'Trưởng phòng', 1, '2023-11-23', '2023-11-23');

-- Insert into tai_khoan (Accounts)
INSERT INTO tai_khoan (ten_dang_nhap, mat_khau, trang_thai, ngay_tao, ngay_sua)
VALUES
    ('admin', 'admin123', 1, '2023-11-23', '2023-11-23'),
    ('nhanvien01', 'nv12345', 1, '2023-11-23', '2023-11-23'),
    ('nhanvien02', 'nv54321', 1, '2023-11-23', '2023-11-23'),
    ('khachhang01', 'kh123456', 1, '2023-11-23', '2023-11-23'),
    ('khachhang02', 'kh654321', 1, '2023-11-23', '2023-11-23');

-- Insert into khach_hang (Customers)
INSERT INTO khach_hang (ten, ngay_sinh, gioi_tinh, sdt, email, anh_url, id_tai_khoan, trang_thai, ngay_tao, ngay_sua)
VALUES
    (N'Trần Văn A', '1990-01-01', 1, '0901234567', 'a@gmail.com', 'a.png', 4, 1, '2023-11-23', '2023-11-23'),
    (N'Nguyễn Thị B', '1992-05-05', 0, '0902345678', 'b@gmail.com', 'b.png', 5, 1, '2023-11-23', '2023-11-23'),
    (N'Lê Văn C', '1988-08-08', 1, '0903456789', 'c@gmail.com', 'c.png', 4, 1, '2023-11-23', '2023-11-23'),
    (N'Phạm Thị D', '1995-10-10', 0, '0904567890', 'd@gmail.com', 'd.png', 5, 1, '2023-11-23', '2023-11-23'),
    (N'Hoàng Văn E', '1993-12-12', 1, '0905678901', 'e@gmail.com', 'e.png', 4, 1, '2023-11-23', '2023-11-23');

-- Insert into nhan_vien (Employees)
INSERT INTO nhan_vien (ten, ngay_sinh, gioi_tinh, sdt, email, anh_url, id_tai_khoan, id_vai_tro, trang_thai, ngay_tao, ngay_sua)
VALUES
    (N'Nguyễn Văn F', '1985-03-03', 1, '0906789012', 'f@gmail.com', 'f.png', 1, 1, 1, '2023-11-23', '2023-11-23'),
    (N'Trần Thị G', '1990-10-10', 0, '0907890123', 'g@gmail.com', 'g.png', 2, 2, 1, '2023-11-23', '2023-11-23'),
    (N'Phan Văn H', '1992-07-07', 1, '0908901234', 'h@gmail.com', 'h.png', 3, 1, 1, '2023-11-23', '2023-11-23'),
    (N'Bùi Thị I', '1988-05-05', 0, '0909012345', 'i@gmail.com', 'i.png', 4, 2, 1, '2023-11-23', '2023-11-23'),
    (N'Tran Văn J', '1995-12-12', 1, '0910123456', 'j@gmail.com', 'j.png', 5, 1, 1, '2023-11-23', '2023-11-23');

	INSERT INTO hoa_don_chi_tiet (id_hoa_don, id_san_pham_chi_tiet, so_luong, gia)
VALUES
(1, 1, 2, 500.00),
(1, 2, 1, 1000.00),
(2, 3, 3, 600.00),
(2, 4, 2, 700.00),
(3, 1, 1, 1200.00),
(3, 3, 2, 400.00),
(4, 5, 4, 450.00),
(5, 2, 2, 1100.00);


INSERT INTO dia_chi (id_khach_hang, xa, huyen, thanh_pho, dia_chi_chi_tiet)
VALUES
(1, N'Xã Long Hưng', N'Quận 1', N'Hải Phòng', N'Số 702 đường Phan Văn Trị'),
(2, N'Phường 1', N'Huyện Củ Chi', N'Đà Nẵng', N'Số 509 đường Phan Văn Trị'),
(3, N'Xã Phú Mỹ', N'Quận 2', N'Hà Nội', N'Số 378 đường Trường Chinh'),
(4, N'Phường 2', N'Huyện Củ Chi', N'Đà Nẵng', N'Số 817 đường Lê Lợi'),
(5, N'Phường 3', N'Quận 2', N'Cần Thơ', N'Số 581 đường Trường Chinh');
-- Insert into gio_hang
INSERT INTO gio_hang (id_khach_hang, ngay_tao, ngay_sua)
VALUES (1, GETDATE(), GETDATE()),
       (2, GETDATE(), GETDATE()),
       (3, GETDATE(), GETDATE()),
       (4, GETDATE(), GETDATE()),
       (5, GETDATE(), GETDATE());

-- Insert into san_pham_gio_hang
INSERT INTO san_pham_gio_hang (id_gio_hang, id_spct, so_luong, ngay_tao, ngay_sua)
VALUES (1, 1, 3, GETDATE(), GETDATE()), -- Giỏ hàng 1, Sản phẩm 1, Số lượng 3
       (1, 2, 1, GETDATE(), GETDATE()), -- Giỏ hàng 1, Sản phẩm 2, Số lượng 1
       (2, 3, 2, GETDATE(), GETDATE()), -- Giỏ hàng 2, Sản phẩm 3, Số lượng 2
       (3, 1, 5, GETDATE(), GETDATE()), -- Giỏ hàng 3, Sản phẩm 1, Số lượng 5
       (4, 4, 1, GETDATE(), GETDATE()); -- Giỏ hàng 4, Sản phẩm 4, Số lượng 1

-- Insert into lich_sua_hoa_don
INSERT INTO lich_sua_hoa_don (id_hoa_don, tieu_de, trang_thai, ngay_tao, ngay_sua)
VALUES (1, N'Cập nhật địa chỉ giao hàng', 1, GETDATE(), GETDATE()),  -- Hoá đơn 1, trạng thái 1
       (1, N'Xác nhận thanh toán', 2, GETDATE(), GETDATE()),         -- Hoá đơn 1, trạng thái 2
       (2, N'Giao hàng thành công', 3, GETDATE(), GETDATE()),        -- Hoá đơn 2, trạng thái 3
       (3, N'Huỷ đơn hàng', 4, GETDATE(), GETDATE()),                -- Hoá đơn 3, trạng thái 4
       (4, N'Đang xử lý', 1, GETDATE(), GETDATE());                  -- Hoá đơn 4, trạng thái 1


INSERT INTO hoa_don (id_khach_hang, ten_nguoi_nhan, sdt, email_nguoi_nhan, id_dia_chi, id_phieu_giam_gia, id_thanh_toan, tong_tien, ghi_chu, id_nguoi_tao, trang_thai)
VALUES 
(1, N'Nguyen Van A', '0123456789', 'nguyenvana@example.com', 1, 1, 1, 1500.00, N'Ghi chú 1', 1, 1),
(2, N'Tran Thi B', '0987654321', 'tranthib@example.com', 2, 2, 2, 2000.00, N'Ghi chú 2', 2, 2),
(3, N'Le Van C', '0234567890', 'levanc@example.com', 3, 3, 3, 1200.00, N'Ghi chú 3', 1, 1),
(4, N'Pham Van D', '0345678901', 'phamvand@example.com', 4, 1, 2, 1800.00, N'Ghi chú 4', 2, 2),
(5, N'Dang Thi E', '0456789012', 'dangthie@example.com', 5, 2, 3, 2200.00, N'Ghi chú 5', 1, 1);