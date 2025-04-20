# FoxDiscoAPI

FoxDiscoAPI, Discord'un API'sine webhook isteklerini yönlendirmek için tasarlanmış bir proxy servisidir. Türkiye gibi Discord erişiminin kısıtlandığı bölgelerde, uygulamaların webhook sistemini kullanmaya devam etmelerini sağlar.

## Özellikler

- **Webhook Proxy**: Gelen webhook isteklerini Discord'un API'sine iletir
- **Varsayılan Olarak Güvenli**: Güvenlik başlıkları ve hız sınırlama içerir
- **Kapsamlı Loglama**: İzleme ve sorun giderme için yapılandırılmış loglar
- **Hata Yönetimi**: Stabilite için sağlam hata yönetimi

## Başlarken

### Önkoşullar

- Node.js (v14 veya daha yeni)
- npm

### Kurulum

1. Depoyu klonlayın
   ```bash
   git clone https://github.com/apcu01/foxdisco-api.git
   cd foxdisco-api
   ```

2. Bağımlılıkları yükleyin
   ```bash
   npm install
   ```

3. `.env` dosyasını ayarlayın
   ```bash
   cd .env
   ```

4. Ortamınız için `.env` dosyasını gerektiği gibi düzenleyin

5. Sunucuyu başlatın
   ```bash
   npm start
   ```

## API Kullanımı

### Discord Webhook

```bash
# Discord'a bir webhook iletin
POST /api/webhook/:webhookId/:webhookToken

# curl ile örnek
curl -X POST http://localhost:5678/api/webhook/123456789012345678/webhook-token \
  -H "Content-Type: application/json" \
  -d '{
    "content": "FoxDiscoAPI\'den merhaba!",
    "embeds": [{
      "title": "Embed Başlığı",
      "description": "Bu bir test embed\'idir"
    }]
  }'
```

## Yapılandırma

Uygulama yapılandırma için çevre değişkenlerini kullanır:

| Değişken | Açıklama | Varsayılan |
|----------|-------------|---------|
| `PORT` | Sunucunun dinlediği port | 5678 |
| `NODE_ENV` | Ortam (development/production) | development |
| `LOG_LEVEL` | Loglama seviyesi | info |
| `RATE_LIMIT_MAX` | Zaman aralığında IP başına maksimum istek | 100 |
| `RATE_LIMIT_WINDOW_MS` | Hız sınırı zaman aralığı (ms) | 900000 (15 dk) |

## Güvenlik Konuları

- Servis, kötüye kullanımı önlemek için hız sınırlama içerir
- Hiçbir webhook verisi saklanmaz - istekler doğrudan iletilir
- Helmet aracılığıyla güvenlik başlıkları etkinleştirilir
- Tüm isteklerde girdi doğrulaması yapılır

## Katkıda Bulunma

Katkılarınızı memnuniyetle karşılıyoruz! Lütfen bir Pull Request göndermekten çekinmeyin.

