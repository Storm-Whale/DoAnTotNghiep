CREATE DATABASE DOANTOTNGHIEP
GO
USE DOANTOTNGHIEP
GO
-- Table for Brands
CREATE TABLE thuong_hieu
(
    id              INT PRIMARY KEY IDENTITY (1,1),
    ten_thuong_hieu NVARCHAR(20),
    trang_thai      INT,
    ngay_tao        DATE,
    ngay_sua        DATE
);

-- Table for Materials
CREATE TABLE chat_lieu
(
    id            INT PRIMARY KEY IDENTITY (1,1),
    ten_chat_lieu NVARCHAR(20),
    trang_thai    INT,
    ngay_tao      DATE,
    ngay_sua      DATE
);

-- Table for Sleeve Styles
CREATE TABLE kieu_tay_ao
(
    id         INT PRIMARY KEY IDENTITY (1,1),
    ten_tay_ao NVARCHAR(20),
    trang_thai INT,
    ngay_tao   DATE,
    ngay_sua   DATE
);

-- Table for Collar Styles
CREATE TABLE kieu_co_ao
(
    id         INT PRIMARY KEY IDENTITY (1,1),
    ten_co_ao  NVARCHAR(20),
    trang_thai INT,
    ngay_tao   DATE,
    ngay_sua   DATE
);

-- Table for Colors
CREATE TABLE mau_sac
(
    id          INT PRIMARY KEY IDENTITY (1,1),
    ten_mau_sac NVARCHAR(20),
    trang_thai  INT,
    ngay_tao    DATE,
    ngay_sua    DATE
);

-- Table for Sizes
CREATE TABLE kich_co
(
    id          INT PRIMARY KEY IDENTITY (1,1),
    ten_kich_co NVARCHAR(20),
    trang_thai  INT,
    ngay_tao    DATE,
    ngay_sua    DATE
);

-- Product Table
CREATE TABLE san_pham
(
    id             INT PRIMARY KEY IDENTITY (1,1),
    ten_san_pham   NVARCHAR(20),
    anh_url        VARCHAR(255),
    id_thuong_hieu INT,
    id_chat_lieu   INT,
    id_tay_ao      INT,
    id_co_ao       INT,
    trang_thai     INT,
    mo_ta          text,
    ngay_tao       DATE,
    ngay_sua       DATE,
    FOREIGN KEY (id_thuong_hieu) REFERENCES thuong_hieu (id),
    FOREIGN KEY (id_chat_lieu) REFERENCES chat_lieu (id),
    FOREIGN KEY (id_tay_ao) REFERENCES kieu_tay_ao (id),
    FOREIGN KEY (id_co_ao) REFERENCES kieu_co_ao (id)
);

-- Product Details Table
CREATE TABLE san_pham_chi_tiet
(
    id          INT PRIMARY KEY IDENTITY (1,1),
    id_san_pham INT,
    id_kich_co  INT,
    id_mau_sac  INT,
    gia         DECIMAL(10, 2),
    so_luong    INT,
    trang_thai  INT,
    ngay_tao    DATE,
    ngay_sua    DATE,
    FOREIGN KEY (id_san_pham) REFERENCES san_pham (id),
    FOREIGN KEY (id_kich_co) REFERENCES kich_co (id),
    FOREIGN KEY (id_mau_sac) REFERENCES mau_sac (id)
);

-- Product Images Table
CREATE TABLE anh_san_pham
(
    id         INT PRIMARY KEY IDENTITY (1,1),
    anh_url    VARCHAR(255),
    id_spct    INT,
    trang_thai INT,
    ngay_tao   DATE,
    ngay_sua   DATE,
    FOREIGN KEY (id_spct) REFERENCES san_pham_chi_tiet (id)
);

-- Discount Voucher Table
CREATE TABLE phieu_giam_gia
(
    id                 INT PRIMARY KEY IDENTITY (1,1),
    ma                 NVARCHAR(20),
    ten_phieu_giam_gia NVARCHAR(20),
    so_luong           INT,
    dieu_kien          DECIMAL(10, 2),
    kieu_giam_gia      INT,
    ngay_bat_dau       DATE,
    ngay_ket_thuc      DATE,
    gia_tri_giam       DECIMAL(10, 2),
    gia_tri_max        DECIMAL(10, 2),
    trang_thai         INT,
    ngay_tao           DATE,
    ngay_sua           DATE
);

-- Payment Methods Table
CREATE TABLE phuong_thuc_thanh_toan
(
    id                     INT PRIMARY KEY IDENTITY (1,1),
    phuong_thuc_thanh_toan VARCHAR(255),
    trang_thai             INT,
    ngay_tao               DATE,
    ngay_sua               DATE
);

-- Role Table
CREATE TABLE vai_tro
(
    id          INT PRIMARY KEY IDENTITY (1,1),
    ten_vai_tro NVARCHAR(20),
    trang_thai  INT,
    ngay_tao    DATE,
    ngay_sua    DATE
);

-- Account Table
CREATE TABLE tai_khoan
(
    id            INT PRIMARY KEY IDENTITY (1,1),
    ten_dang_nhap VARCHAR(255),
    mat_khau      VARCHAR(20),
    trang_thai    INT,
    ngay_tao      DATE,
    ngay_sua      DATE,
);

-- Customer Table
CREATE TABLE khach_hang
(
    id           INT PRIMARY KEY IDENTITY (1,1),
    ten          NVARCHAR(50),
    ngay_sinh    DATE,
    gioi_tinh    INT,
    sdt          VARCHAR(20),
    email        VARCHAR(255),
    anh_url      VARCHAR(255),
    id_tai_khoan INT,
    trang_thai   INT,
    ngay_tao     DATE,
    ngay_sua     DATE,
    FOREIGN KEY (id_tai_khoan) REFERENCES tai_khoan (id)
);

-- Employee Table
CREATE TABLE nhan_vien
(
    id           INT PRIMARY KEY IDENTITY (1,1),
    ten          NVARCHAR(50),
    ngay_sinh    DATE,
    gioi_tinh    INT,
    sdt          VARCHAR(20),
    email        VARCHAR(255),
    anh_url      VARCHAR(255),
    id_tai_khoan INT,
    id_vai_tro   INT,
    trang_thai   INT,
    ngay_tao     DATE,
    ngay_sua     DATE,
    FOREIGN KEY (id_tai_khoan) REFERENCES tai_khoan (id),
    FOREIGN KEY (id_vai_tro) REFERENCES vai_tro (id)
);

-- Address Table
CREATE TABLE dia_chi
(
    id               INT PRIMARY KEY IDENTITY (1,1),
    id_khach_hang    INT,
    xa               NVARCHAR(50),
    huyen            NVARCHAR(50),
    thanh_pho        NVARCHAR(50),
    dia_chi_chi_tiet NVARCHAR(255),
    ngay_tao         DATE,
    ngay_sua         DATE,
    FOREIGN KEY (id_khach_hang) REFERENCES khach_hang (id)
);

-- Invoice Table
CREATE TABLE hoa_don
(
    id                INT PRIMARY KEY IDENTITY (1,1),
    id_khach_hang     INT,
    ten_nguoi_nhan    NVARCHAR(50),
    sdt               VARCHAR(20),
    email_nguoi_nhan  VARCHAR(255),
    id_dia_chi        INT,
    id_phieu_giam_gia INT,
    id_thanh_toan     INT,
    tong_tien         DECIMAL(10, 2),
    ghi_chu           NVARCHAR(255),
    ngay_tao          DATE,
    ngay_sua          DATE,
    id_nguoi_tao      INT,
    trang_thai        INT,
    FOREIGN KEY (id_nguoi_tao) REFERENCES nhan_vien (id),
    FOREIGN KEY (id_khach_hang) REFERENCES khach_hang (id),
    FOREIGN KEY (id_dia_chi) REFERENCES dia_chi (id),
    FOREIGN KEY (id_phieu_giam_gia) REFERENCES phieu_giam_gia (id),
    FOREIGN KEY (id_thanh_toan) REFERENCES phuong_thuc_thanh_toan (id)
);

-- Invoice Details Table
CREATE TABLE hoa_don_chi_tiet
(
    id                   INT PRIMARY KEY IDENTITY (1,1),
    id_hoa_don           INT,
    id_san_pham_chi_tiet INT,
    so_luong             INT,
    gia                  DECIMAL(10, 2),
    ngay_tao             DATE,
    ngay_sua             DATE,
    FOREIGN KEY (id_hoa_don) REFERENCES hoa_don (id),
    FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet (id)
);

-- Shopping Cart Table
CREATE TABLE gio_hang
(
    id            INT PRIMARY KEY IDENTITY (1,1),
    id_khach_hang INT UNIQUE,
    ngay_tao      DATE,
    ngay_sua      DATE,
    FOREIGN KEY (id_khach_hang) REFERENCES khach_hang (id)
);

-- Shopping Cart Products Table
CREATE TABLE san_pham_gio_hang
(
    id          INT PRIMARY KEY IDENTITY (1,1),
    id_gio_hang INT,
    id_spct     INT,
    so_luong    INT,
    ngay_tao    DATE,
    ngay_sua    DATE,
    trang_thai  INT,
    FOREIGN KEY (id_gio_hang) REFERENCES gio_hang (id),
    FOREIGN KEY (id_spct) REFERENCES san_pham_chi_tiet (id)
);

-- Invoice Edit History Table
CREATE TABLE lich_sua_hoa_don
(
    id         INT PRIMARY KEY IDENTITY (1,1),
    id_hoa_don INT,
    tieu_de    NVARCHAR(255),
    trang_thai INT,
    ngay_tao   DATE,
    ngay_sua   DATE,
    FOREIGN KEY (id_hoa_don) REFERENCES hoa_don (id)
);
