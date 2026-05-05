# Hướng Dẫn Deploy

## 🚀 Deploy lên Railway

### Bước 1: Chuẩn bị
1. Đảm bảo code đã được push lên GitHub
2. Đã có tài khoản Railway (https://railway.app)
3. Đã tạo MySQL database trên Railway

### Bước 2: Tạo Project trên Railway
1. Vào Railway Dashboard
2. Click **New Project**
3. Chọn **Deploy from GitHub repo**
4. Chọn repository của bạn

### Bước 3: Cấu hình Environment Variables
Vào tab **Variables** và thêm:

```
DATABASE_URL=jdbc:mysql://mysql.railway.internal:3306/railway?useUnicode=true&characterEncoding=UTF-8
DB_USER=root
DB_PASSWORD=DVZXNgMAdizqdRfusRaGzuctvaQCQCJG
PORT=8080
JAVA_OPTS=-Xmx512m -Xms256m
```

### Bước 4: Deploy
Railway sẽ tự động:
1. Detect Java project
2. Chạy `./gradlew clean build -x test`
3. Start server với `java -jar build/libs/webapp-1.0-SNAPSHOT.jar`

### Bước 5: Kiểm tra
- Xem logs trong tab **Deployments**
- Truy cập URL được Railway cung cấp

## 🐳 Deploy với Docker (Optional)

### Tạo Dockerfile:
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/webapp-1.0-SNAPSHOT.jar"]
```

### Build và Run:
```bash
docker build -t webapp .
docker run -p 8080:8080 \
  -e DATABASE_URL="jdbc:mysql://host:3306/db" \
  -e DB_USER="root" \
  -e DB_PASSWORD="password" \
  webapp
```

## 🔧 Chạy Local với Embedded Tomcat

### Cách 1: Sử dụng Gradle
```bash
./gradlew run
```

### Cách 2: Build JAR và chạy
```bash
./gradlew clean build
java -jar build/libs/webapp-1.0-SNAPSHOT.jar
```

### Cách 3: Trong IntelliJ IDEA
1. Right-click `Main.java`
2. Chọn **Run 'Main.main()'**

## 📝 Troubleshooting

### Lỗi "Main class not found"
- Kiểm tra file `Main.java` tồn tại trong `src/main/java/vn/edu/hcmuaf/fit/`
- Rebuild project: `./gradlew clean build`

### Lỗi "Port already in use"
- Đổi port: `java -Dserver.port=9090 -jar build/libs/webapp-1.0-SNAPSHOT.jar`
- Hoặc kill process đang dùng port 8080

### Lỗi Database Connection
- Kiểm tra environment variables
- Xem file `DATABASE_CONFIG.md` để biết chi tiết

### Railway Build Failed
- Kiểm tra logs trong Railway Dashboard
- Đảm bảo `gradlew` có quyền execute: `git update-index --chmod=+x gradlew`
- Kiểm tra `gradle-wrapper.jar` đã được commit

### Out of Memory
- Tăng heap size: `JAVA_OPTS=-Xmx1024m -Xms512m`
- Trong Railway, upgrade plan để có nhiều RAM hơn

## 🔍 Kiểm tra Health

Sau khi deploy, test các endpoint:
- Homepage: `https://your-app.railway.app/`
- Health check: `https://your-app.railway.app/api/health` (nếu có)

## 📊 Monitoring

### Railway Dashboard
- CPU/Memory usage
- Request logs
- Error logs
- Deployment history

### Application Logs
Xem logs trong Railway hoặc:
```bash
railway logs
```
