import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * FoxDiscoAPI Discord Webhook örneği
 * Bu sınıf, FoxDiscoAPI üzerinden Discord webhook isteklerini göndermeyi gösterir
 */
public class DiscordWebhook {
    private final String url;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private final List<EmbedObject> embeds = new ArrayList<>();

    /**
     * Discord webhook URL'sini alır. FoxDiscoAPI'yi kullanmak için,
     * Discord URL'sini FoxDiscoAPI URL'si ile değiştirin:
     * 
     * Discord URL: https://discord.com/api/webhooks/123456789012345678/token
     * FoxDiscoAPI URL: http://yourserver.com:5678/api/webhook/123456789012345678/token
     * 
     * @param url Webhook URL'si
     */
    public DiscordWebhook(String url) {
        this.url = url;
    }

    /**
     * Webhook içeriğini ayarlar
     * @param content Webhook mesaj içeriği
     * @return DiscordWebhook
     */
    public DiscordWebhook setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Webhook kullanıcı adını ayarlar
     * @param username Webhook kullanıcı adı
     * @return DiscordWebhook
     */
    public DiscordWebhook setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Webhook avatar URL'sini ayarlar
     * @param avatarUrl Avatar URL
     * @return DiscordWebhook
     */
    public DiscordWebhook setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    /**
     * Text-to-Speech (TTS) seçeneğini ayarlar
     * @param tts TTS etkin mi?
     * @return DiscordWebhook
     */
    public DiscordWebhook setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    /**
     * Webhook'a bir embed ekler
     * @param embed Eklenecek embed
     * @return DiscordWebhook
     */
    public DiscordWebhook addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
        return this;
    }

    /**
     * Webhook isteğini Discord'a (veya FoxDiscoAPI'ye) gönderir
     * @throws IOException HTTP hatası durumunda
     */
    public void execute() throws IOException {
        // JSON verisini oluştur
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        if (content != null) {
            json.append("\"content\":\"").append(escape(content)).append("\",");
        }
        
        if (username != null) {
            json.append("\"username\":\"").append(escape(username)).append("\",");
        }
        
        if (avatarUrl != null) {
            json.append("\"avatar_url\":\"").append(escape(avatarUrl)).append("\",");
        }
        
        if (tts) {
            json.append("\"tts\":true,");
        }

        if (!embeds.isEmpty()) {
            json.append("\"embeds\":[");
            for (EmbedObject embed : embeds) {
                json.append(embed.toJson()).append(",");
            }
            json.deleteCharAt(json.length() - 1); // Son virgülü kaldır
            json.append("]");
        } else if (json.charAt(json.length() - 1) == ',') {
            json.deleteCharAt(json.length() - 1); // Son virgülü kaldır
        }
        
        json.append("}");

        // HTTP bağlantısı oluştur
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "FoxDiscoAPI-Webhook-Example");
        connection.setDoOutput(true);

        // JSON verisini gönder
        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.toString().getBytes(StandardCharsets.UTF_8));
        }

        // Yanıtı kontrol et
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Webhook başarıyla gönderildi!");
        } else {
            throw new IOException("HTTP Hata: " + responseCode + " " + connection.getResponseMessage());
        }
        
        connection.disconnect();
    }
    
    /**
     * Özel karakterleri JSON için kaçış (escape) yapar
     * @param text Kaçış yapılacak metin
     * @return Kaçış yapılmış metin
     */
    private String escape(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Discord embed nesnesi
     */
    public static class EmbedObject {
        private String title;
        private String description;
        private String url;
        private String timestamp;
        private Integer color;
        private Footer footer;
        private Image image;
        private Thumbnail thumbnail;
        private Author author;
        private final List<Field> fields = new ArrayList<>();

        /**
         * Embed başlığını ayarlar
         * @param title Başlık
         * @return EmbedObject
         */
        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Embed açıklamasını ayarlar
         * @param description Açıklama
         * @return EmbedObject
         */
        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Embed URL'sini ayarlar
         * @param url URL
         * @return EmbedObject
         */
        public EmbedObject setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Embed zaman damgasını ayarlar (ISO8601 formatında)
         * @param timestamp Zaman damgası
         * @return EmbedObject
         */
        public EmbedObject setTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Embed rengini ayarlar (Onaltılık renk kodu)
         * @param color Renk (örn: #5865F2 veya 5865F2)
         * @return EmbedObject
         */
        public EmbedObject setColor(String color) {
            // Hex renk kodunu (örn: #5865F2) integer'a dönüştür
            if (color.startsWith("#")) {
                color = color.substring(1);
            }
            this.color = Integer.parseInt(color, 16);
            return this;
        }

        /**
         * Embed alt bilgisini ayarlar
         * @param text Alt bilgi metni
         * @param iconUrl Alt bilgi ikon URL'si (opsiyonel)
         * @return EmbedObject
         */
        public EmbedObject setFooter(String text, String iconUrl) {
            this.footer = new Footer(text, iconUrl);
            return this;
        }

        /**
         * Embed resmi ekler
         * @param url Resim URL'si
         * @return EmbedObject
         */
        public EmbedObject setImage(String url) {
            this.image = new Image(url);
            return this;
        }

        /**
         * Embed küçük resmi ekler
         * @param url Küçük resim URL'si
         * @return EmbedObject
         */
        public EmbedObject setThumbnail(String url) {
            this.thumbnail = new Thumbnail(url);
            return this;
        }

        /**
         * Embed yazarını ayarlar
         * @param name Yazar adı
         * @param url Yazar URL'si (opsiyonel)
         * @param iconUrl Yazar ikon URL'si (opsiyonel)
         * @return EmbedObject
         */
        public EmbedObject setAuthor(String name, String url, String iconUrl) {
            this.author = new Author(name, url, iconUrl);
            return this;
        }

        /**
         * Embed alanı ekler
         * @param name Alan adı
         * @param value Alan değeri
         * @param inline Alan satırda mı? (yan yana)
         * @return EmbedObject
         */
        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        /**
         * Embed nesnesini JSON'a dönüştürür
         * @return JSON dizesi
         */
        private String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            
            if (title != null) {
                json.append("\"title\":\"").append(escape(title)).append("\",");
            }
            
            if (description != null) {
                json.append("\"description\":\"").append(escape(description)).append("\",");
            }
            
            if (url != null) {
                json.append("\"url\":\"").append(escape(url)).append("\",");
            }
            
            if (timestamp != null) {
                json.append("\"timestamp\":\"").append(escape(timestamp)).append("\",");
            }
            
            if (color != null) {
                json.append("\"color\":").append(color).append(",");
            }
            
            if (footer != null) {
                json.append("\"footer\":").append(footer.toJson()).append(",");
            }
            
            if (image != null) {
                json.append("\"image\":").append(image.toJson()).append(",");
            }
            
            if (thumbnail != null) {
                json.append("\"thumbnail\":").append(thumbnail.toJson()).append(",");
            }
            
            if (author != null) {
                json.append("\"author\":").append(author.toJson()).append(",");
            }
            
            if (!fields.isEmpty()) {
                json.append("\"fields\":[");
                for (Field field : fields) {
                    json.append(field.toJson()).append(",");
                }
                json.deleteCharAt(json.length() - 1); // Son virgülü kaldır
                json.append("],");
            }
            
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1); // Son virgülü kaldır
            }
            
            json.append("}");
            return json.toString();
        }
        
        /**
         * Özel karakterleri JSON için kaçış (escape) yapar
         * @param text Kaçış yapılacak metin
         * @return Kaçış yapılmış metin
         */
        private String escape(String text) {
            if (text == null) {
                return "";
            }
            return text.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
        }

        private static class Footer {
            private final String text;
            private final String iconUrl;

            private Footer(String text, String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }
            
            private String toJson() {
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"text\":\"").append(escape(text)).append("\"");
                if (iconUrl != null) {
                    json.append(",\"icon_url\":\"").append(escape(iconUrl)).append("\"");
                }
                json.append("}");
                return json.toString();
            }
            
            private String escape(String text) {
                if (text == null) {
                    return "";
                }
                return text.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
            }
        }

        private static class Image {
            private final String url;

            private Image(String url) {
                this.url = url;
            }
            
            private String toJson() {
                return "{\"url\":\"" + escape(url) + "\"}";
            }
            
            private String escape(String text) {
                if (text == null) {
                    return "";
                }
                return text.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
            }
        }

        private static class Thumbnail {
            private final String url;

            private Thumbnail(String url) {
                this.url = url;
            }
            
            private String toJson() {
                return "{\"url\":\"" + escape(url) + "\"}";
            }
            
            private String escape(String text) {
                if (text == null) {
                    return "";
                }
                return text.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
            }
        }

        private static class Author {
            private final String name;
            private final String url;
            private final String iconUrl;

            private Author(String name, String url, String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }
            
            private String toJson() {
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"name\":\"").append(escape(name)).append("\"");
                if (url != null) {
                    json.append(",\"url\":\"").append(escape(url)).append("\"");
                }
                if (iconUrl != null) {
                    json.append(",\"icon_url\":\"").append(escape(iconUrl)).append("\"");
                }
                json.append("}");
                return json.toString();
            }
            
            private String escape(String text) {
                if (text == null) {
                    return "";
                }
                return text.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
            }
        }

        private static class Field {
            private final String name;
            private final String value;
            private final boolean inline;

            private Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }
            
            private String toJson() {
                return "{\"name\":\"" + escape(name) + "\",\"value\":\"" + escape(value) + "\",\"inline\":" + inline + "}";
            }
            
            private String escape(String text) {
                if (text == null) {
                    return "";
                }
                return text.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
            }
        }
    }
    
    /**
     * Örnek kullanım
     */
    public static void main(String[] args) {
        try {
            // FoxDiscoAPI webhook URL'si
            String webhookUrl = "http://minefox.net:5678/api/webhook/123456789012345678/token";
            
            // Webhook nesnesi oluştur
            DiscordWebhook webhook = new DiscordWebhook(webhookUrl);
            
            // Basit webhook
            webhook.setContent("Bu basit bir webhook mesajıdır.");
            webhook.setUsername("FoxDiscoAPI Örnek");
            webhook.setAvatarUrl("https://minefox.net/avatar.png");
            
            // Embed oluştur (opsiyonel)
            EmbedObject embed = new EmbedObject()
                    .setTitle("Webhook Örneği")
                    .setDescription("Bu bir embed açıklamasıdır.\nBu bir yeni satırdır.")
                    .setColor("#5865F2")
                    .setUrl("https://github.com/apcu01/foxdisco-api")
                    .setFooter("FoxDiscoAPI", "https://minefox.net/footer-icon.png")
                    .setImage("https://minefox.net/image.png")
                    .setThumbnail("https://minefox.net/thumbnail.png")
                    .setAuthor("FoxDiscoAPI Takımı", "https://github.com/apcu01", "https://minefox.net/author-icon.png")
                    .addField("Alan 1", "Değer 1", true)
                    .addField("Alan 2", "Değer 2", true)
                    .addField("Alan 3", "Değer 3", false);
            
            // Embed'i webhook'a ekle
            webhook.addEmbed(embed);
            
            // Webhook'u gönder
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 