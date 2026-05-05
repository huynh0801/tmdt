# Hướng Dẫn Cấu Hình Database

## 1. Cấu Hình Local Development

### Cách 1: Sử dụng giá trị mặc định trong code
Code đã có sẵn giá trị mặc định cho local:
- URL: `jdbc:mysql://localhost:3306/dataweb`
- User: `root`
- Password: `12345`

Không cần làm gì thêm nếu MySQL local của bạn khớp với cấu hình này.

### Cách 2: Sử dụng Environment Variables
Nếu muốn thay đổi cấu hình, set environment variables:

**Windows (PowerShell):**
```powershell
$env:DATABASE_URL="jdbc:mysql://localhost:3306/dataweb?useUnicode=true&characterEncoding=UTF-8"
$env:DB_USER="root"
$env:DB_PASSWORD="12345"
```

**Windows (CMD):**
```cmd
set DATABASE_URL=jdbc:mysql://localhost:3306/dataweb?useUnicode=true&characterEncoding=UTF-8
set DB_USER=root
set DB_PASSWORD=12345
```

**Linux/Mac:**
```bash
export DATABASE_URL="jdbc:mysql://localhost:3306/dataweb?useUnicode=true&characterEncoding=UTF-8"
export DB_USER="root"
export DB_PASSWORD="12345"
```

## 2. Cấu Hình Railway Production

### Thông tin kết nối Railway của bạn:
- **Host (Internal):** `mysql.railway.internal`
- **Port:** `3306`
- **Database:** `railway`
- **User:** `root`
- **Password:** `DVZXNgMAdizqdRfusRaGzuctvaQCQCJG`

### Cách 1: Set Environment Variables trên Railway Dashboard
Vào Railway Dashboard → Your Project → Variables, thêm:
```
DATABASE_URL=jdbc:mysql://mysql.railway.internal:3306/railway?useUnicode=true&characterEncoding=UTF-8
DB_USER=root
DB_PASSWORD=DVZXNgMAdizqdRfusRaGzuctvaQCQCJG
```

### Cách 2: Sử dụng Public URL (nếu cần truy cập từ bên ngoài)
```
DATABASE_URL=jdbc:mysql://switchback.proxy.rlwy.net:31438/railway?useUnicode=true&characterEncoding=UTF-8
DB_USER=root
DB_PASSWORD=DVZXNgMAdizqdRfusRaGzuctvaQCQCJG
```

## 3. Cấu Hình trong IntelliJ IDEA

### Run Configuration:
1. Mở **Run → Edit Configurations**
2. Chọn configuration của bạn (Tomcat/Application)
3. Vào tab **Configuration** hoặc **Environment Variables**
4. Thêm environment variables:
   ```
   DATABASE_URL=jdbc:mysql://localhost:3306/dataweb?useUnicode=true&characterEncoding=UTF-8
   DB_USER=root
   DB_PASSWORD=12345
   ```

## 4. Test Kết Nối

Chạy class `DBConnect.main()` để test:
```bash
./gradlew run
```

Hoặc trong IntelliJ: Right-click `DBConnect.java` → Run 'DBConnect.main()'

## 5. Lưu Ý Bảo Mật

⚠️ **QUAN TRỌNG:**
- File `.env` chứa thông tin nhạy cảm đã được thêm vào `.gitignore`
- **KHÔNG BAO GIỜ** commit file `.env` hoặc thông tin database thật lên Git
- Chỉ commit file `.env.example` (không chứa thông tin thật)
- Trên production (Railway), sử dụng Environment Variables của platform

## 6. Troubleshooting

### Lỗi "Access denied"
- Kiểm tra username/password
- Đảm bảo MySQL đang chạy
- Kiểm tra user có quyền truy cập database

### Lỗi "Unknown database"
- Tạo database: `CREATE DATABASE dataweb;`
- Hoặc đổi tên database trong URL

### Lỗi "Communications link failure"
- Kiểm tra MySQL service đang chạy
- Kiểm tra port 3306 không bị block
- Kiểm tra firewall

### Railway connection timeout
- Sử dụng internal URL (`mysql.railway.internal`) khi deploy trên Railway
- Sử dụng public URL (`switchback.proxy.rlwy.net:31438`) khi test từ local
