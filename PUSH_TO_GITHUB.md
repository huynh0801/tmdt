# Hướng Dẫn Push Code Lên GitHub

## ⚠️ Lỗi 403 - Permission Denied

Bạn đang gặp lỗi này vì tài khoản GitHub hiện tại (22130107) không có quyền push lên repository `huynh0801/tmdt.git`.

## 🔧 Giải pháp

### Cách 1: Sử dụng Personal Access Token (Khuyến nghị)

1. **Tạo Personal Access Token:**
   - Vào GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
   - Click **Generate new token (classic)**
   - Chọn scopes: `repo` (full control)
   - Copy token (chỉ hiện 1 lần!)

2. **Cấu hình Git để dùng token:**
   ```bash
   git remote set-url origin https://<TOKEN>@github.com/huynh0801/tmdt.git
   ```
   Thay `<TOKEN>` bằng token vừa tạo.

3. **Push:**
   ```bash
   git push -u origin master
   ```

### Cách 2: Sử dụng SSH Key

1. **Tạo SSH key (nếu chưa có):**
   ```bash
   ssh-keygen -t ed25519 -C "your_email@example.com"
   ```

2. **Copy public key:**
   ```bash
   cat ~/.ssh/id_ed25519.pub
   ```

3. **Thêm vào GitHub:**
   - Vào GitHub → Settings → SSH and GPG keys → New SSH key
   - Paste public key vào

4. **Đổi remote URL sang SSH:**
   ```bash
   git remote set-url origin git@github.com:huynh0801/tmdt.git
   ```

5. **Push:**
   ```bash
   git push -u origin master
   ```

### Cách 3: Đổi Repository Owner

Nếu đây là repository của bạn (tài khoản 22130107):

```bash
git remote set-url origin https://github.com/22130107/tmdt.git
git push -u origin master
```

### Cách 4: Sử dụng GitHub CLI (gh)

1. **Cài đặt GitHub CLI:**
   - Windows: `winget install GitHub.cli`
   - Mac: `brew install gh`

2. **Login:**
   ```bash
   gh auth login
   ```

3. **Push:**
   ```bash
   git push -u origin master
   ```

## 📝 Kiểm tra sau khi push thành công

```bash
git log --oneline -5
git remote -v
```

## 🚀 Sau khi push lên GitHub

1. Vào Railway Dashboard
2. Connect repository
3. Deploy sẽ tự động chạy
4. Kiểm tra logs và URL

## ❓ Troubleshooting

### Lỗi "Authentication failed"
- Token đã hết hạn → Tạo token mới
- Token không có quyền `repo` → Tạo lại với đủ quyền

### Lỗi "Repository not found"
- Kiểm tra tên repository đúng chưa
- Kiểm tra quyền truy cập repository

### Lỗi "Permission denied (publickey)"
- SSH key chưa được thêm vào GitHub
- SSH agent chưa chạy: `eval "$(ssh-agent -s)" && ssh-add ~/.ssh/id_ed25519`
