# 🚀 Quick Start Guide

## ✅ Tình trạng hiện tại

- ✅ Code đã sẵn sàng
- ✅ Build thành công
- ✅ Cấu hình deployment đã xong
- ⏳ Cần push lên GitHub

## 📤 Push lên GitHub (Bắt buộc)

### Cách 1: Tự động với script
```bash
./push.sh
```

### Cách 2: Thủ công

**Nếu bạn có Personal Access Token:**
```bash
git remote set-url origin https://<YOUR_TOKEN>@github.com/huynh0801/tmdt.git
git push -u origin master
```

**Nếu bạn dùng SSH:**
```bash
git remote set-url origin git@github.com:huynh0801/tmdt.git
git push -u origin master
```

**Tạo Personal Access Token:**
1. Vào: https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Chọn scope: `repo`
4. Copy token và dùng ở lệnh trên

## 🚂 Deploy lên Railway

### Bước 1: Push code lên GitHub (bước trên)

### Bước 2: Kết nối Railway với GitHub
1. Vào: https://railway.app
2. Click **New Project**
3. Chọn **Deploy from GitHub repo**
4. Chọn repository: `huynh0801/tmdt`

### Bước 3: Cấu hình Environment Variables
Trong Railway Dashboard → Variables, thêm:

```env
DATABASE_URL=jdbc:mysql://mysql.railway.internal:3306/railway?useUnicode=true&characterEncoding=UTF-8
DB_USER=root
DB_PASSWORD=DVZXNgMAdizqdRfusRaGzuctvaQCQCJG
PORT=8080
```

### Bước 4: Deploy
Railway sẽ tự động:
- ✅ Detect Java project
- ✅ Run `./gradlew clean build -x test`
- ✅ Start với `java -jar build/libs/webapp-1.0-SNAPSHOT.jar`

### Bước 5: Kiểm tra
- Xem logs trong tab **Deployments**
- Click vào URL để truy cập ứng dụng

## 🧪 Test Local (Optional)

### Chạy với Gradle:
```bash
./gradlew run
```

### Hoặc chạy JAR trực tiếp:
```bash
./gradlew clean build -x test
java -jar build/libs/webapp-1.0-SNAPSHOT.jar
```

Truy cập: http://localhost:8080

## 📚 Tài liệu chi tiết

- `PUSH_TO_GITHUB.md` - Hướng dẫn push chi tiết
- `DEPLOYMENT.md` - Hướng dẫn deploy chi tiết
- `DATABASE_CONFIG.md` - Cấu hình database

## ❓ Troubleshooting

### Lỗi push: "Permission denied"
→ Xem `PUSH_TO_GITHUB.md` để cấu hình authentication

### Lỗi build trên Railway
→ Kiểm tra logs, thường do thiếu environment variables

### Lỗi database connection
→ Xem `DATABASE_CONFIG.md` để cấu hình đúng

## 📞 Cần giúp đỡ?

1. Kiểm tra logs trong Railway Dashboard
2. Xem các file hướng dẫn chi tiết
3. Kiểm tra GitHub Issues của project
