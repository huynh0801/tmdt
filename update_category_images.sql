-- ============================================
-- Cập nhật hình ảnh cho các danh mục nội thất gia dụng
-- Ngày tạo: 05/05/2026
-- ============================================

SET NAMES utf8mb4;

-- Cập nhật tên và hình ảnh cho các danh mục nội thất gia dụng
UPDATE categories SET 
    CategoryName = 'Đồ Dụng Nhà Bếp',
    Description = 'Các sản phẩm dụng cụ nhà bếp tiện ích',
    Image = 'https://images.unsplash.com/photo-1556911220-bff31c812dba?w=400&h=300&fit=crop'
WHERE CategoryID = 1;

UPDATE categories SET 
    CategoryName = 'Bảo quản thực phẩm',
    Description = 'Hộp đựng, túi bảo quản thực phẩm',
    Image = 'https://images.unsplash.com/photo-1584308972272-9e4e7685e80f?w=400&h=300&fit=crop'
WHERE CategoryID = 2;

UPDATE categories SET 
    CategoryName = 'Dao, kéo, thớt',
    Description = 'Dụng cụ cắt gọt nhà bếp',
    Image = 'https://images.unsplash.com/photo-1593618998160-e34014e67546?w=400&h=300&fit=crop'
WHERE CategoryID = 3;

UPDATE categories SET 
    CategoryName = 'Chén, đĩa',
    Description = 'Bộ chén đĩa, bát, ly các loại',
    Image = 'https://images.unsplash.com/photo-1578500494198-246f612d3b3d?w=400&h=300&fit=crop'
WHERE CategoryID = 4;

UPDATE categories SET 
    CategoryName = 'Vệ sinh bếp',
    Description = 'Dụng cụ vệ sinh nhà bếp',
    Image = 'https://images.unsplash.com/photo-1563453392212-326f5e854473?w=400&h=300&fit=crop'
WHERE CategoryID = 5;

UPDATE categories SET 
    CategoryName = 'Dụng cụ nấu nướng',
    Description = 'Nồi, chảo, xoong các loại',
    Image = 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop'
WHERE CategoryID = 6;

UPDATE categories SET 
    CategoryName = 'Thiết bị nhà bếp',
    Description = 'Máy xay, máy ép, máy nướng',
    Image = 'https://images.unsplash.com/photo-1585515320310-259814833e62?w=400&h=300&fit=crop'
WHERE CategoryID = 7;

UPDATE categories SET 
    CategoryName = 'Thiết bị khử trùng',
    Description = 'Máy khử trùng, tiệt trùng',
    Image = 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=400&h=300&fit=crop'
WHERE CategoryID = 8;

UPDATE categories SET 
    CategoryName = 'Gia vị',
    Description = 'Hũ đựng gia vị, kệ gia vị',
    Image = 'https://images.unsplash.com/photo-1596040033229-a0b3b83b2e4d?w=400&h=300&fit=crop'
WHERE CategoryID = 9;

-- Thêm danh mục mới nếu cần
-- INSERT INTO categories (CategoryID, CategoryName, Description, DisplayOrder, Image) 
-- VALUES (10, 'Đồ trang trí', 'Đồ trang trí nội thất', 10, 'https://images.unsplash.com/photo-1513694203232-719a280e022f?w=400&h=300&fit=crop');

SELECT * FROM categories ORDER BY DisplayOrder ASC;
