# 訂單管理系統

這是一個基於Spring Boot的訂單管理系統，提供會員管理、產品管理和訂單處理功能。

## 功能特點

### 會員管理
- 會員註冊與管理
- 會員資料查詢（按ID、用戶名、電子郵件等）
- 會員資料更新與刪除

### 產品管理
- 產品創建與管理
- 產品資料查詢（按ID、名稱、價格範圍、庫存等）
- 產品資料更新與刪除

### 訂單管理
- 訂單創建與處理
- 訂單狀態更新
- 訂單查詢（按ID、訂單編號、會員、日期範圍、產品名稱等）
- 訂單取消與刪除

## 技術與框架

- **後端框架**: Spring Boot
- **資料庫**: H2 Database (內存資料庫)
- **ORM框架**: Spring Data JPA
- **API文檔**: SpringDoc OpenAPI (Swagger UI)
- **構建工具**: Maven
- **錯誤處理**: 自定義全局異常處理

## 快速開始

### 前置條件

- JDK 21
- Maven

### 運行應用
   - API接口: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui/index.html
   - H2資料庫控制台: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:testdb`
     - 用戶名: `sa`
     - 密碼: (空)

## API接口

### 會員API

- `POST /api/members` - 創建會員
- `PUT /api/members/{id}` - 更新會員
- `GET /api/members/{id}` - 根據ID查詢會員
- `GET /api/members/page` - 分頁查詢所有會員
  - 參數: `page`, `size`, `sortBy`, `direction`
- `GET /api/members/search/username` - 根據用戶名模糊查詢會員（分頁）
  - 參數: `username`, `page`, `size`
- `GET /api/members/search/email` - 根據郵箱模糊查詢會員（分頁）
  - 參數: `email`, `page`, `size`
- `DELETE /api/members/{id}` - 刪除會員
- `GET /api/members/check/username` - 檢查用戶名是否存在
  - 參數: `username`
- `GET /api/members/check/email` - 檢查郵箱是否存在
  - 參數: `email`

### 產品API

- `POST /api/products` - 創建產品
- `PUT /api/products/{id}` - 更新產品
- `GET /api/products/{id}` - 根據ID查詢產品
- `GET /api/products` - 查詢所有產品（分頁）
  - 參數: `page`, `size`, `sortBy`, `direction`
- `GET /api/products/search/name` - 根據產品名稱模糊查詢產品（分頁）
  - 參數: `name`, `page`, `size`
- `GET /api/products/search/price` - 根據價格範圍查詢產品（分頁）
  - 參數: `minPrice`, `maxPrice`, `page`, `size`
- `DELETE /api/products/{id}` - 刪除產品

### 訂單API

- `POST /api/orders` - 創建訂單
- `GET /api/orders/{id}` - 根據ID查詢訂單
- `GET /api/orders/number/{orderNumber}` - 根據訂單編號查詢訂單
- `GET /api/orders/member/{memberId}` - 根據會員ID查詢訂單（分頁）
  - 參數: `page`, `size`, `sortBy`, `sortDir`
- `GET /api/orders/product` - 根據產品名稱查詢訂單（分頁）
  - 參數: `productName`, `page`, `size`, `sortBy`, `sortDir`
- `GET /api/orders/date-range` - 根據日期範圍查詢訂單（分頁）
  - 參數: `startDate`, `endDate`, `page`, `size`, `sortBy`, `sortDir`
- `GET /api/orders/search` - 綜合查詢訂單（分頁）
  - 參數: `orderNumber`, `productName`, `startDate`, `endDate`, `page`, `size`, `sortBy`, `sortDir`
- `PUT /api/orders/{id}/status` - 更新訂單狀態
  - 參數: `status`
- `PUT /api/orders/{id}/cancel` - 取消訂單
- `DELETE /api/orders/{id}` - 刪除訂單
- `GET /api/orders/stats/members` - 統計訂單數量大於指定數量的會員
  - 參數: `count`

## 資料初始化

應用啟動時會自動初始化一些測試資料，包括：

- 3個測試會員
- 5個測試產品
- 3個測試訂單

## 開發指南

### 項目結構

```
src/main/java/com/example/demo/
├── config/           # 配置類
├── controller/       # API控制器
├── exception/        # 自定義異常和全局異常處理
├── bean/             # 傳輸對象類
│   └── model/        # 實體類
│   └── request/      # 請求類
│   └── response/     # 回應類
├── repository/       # 資料訪問層
├── service/          # 業務邏輯層
│   └── impl/         # 業務邏輯實現
└── DemoApplication.java  # 應用入口
```

### 錯誤處理機制

本項目實現了自定義的全局異常處理機制，主要包括：

- **GlobalExceptionHandler**: 使用`@RestControllerAdvice`註解的全局異常處理器
- **ErrorResponse**: 標準化的錯誤返回模型
- **ValidationErrorResponse**: 參數驗證錯誤的返回模型
- **自定義異常類**:
  - BusinessException: 業務邏輯錯誤
  - ResourceNotFoundException: 資源未找到異常
  - InsufficientStockException: 庫存不足異常

