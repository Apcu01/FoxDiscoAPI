const axios = require('axios');
const logger = require('../utils/logger');

/**
 * Discord API temel URL
 */
const DISCORD_API_BASE = 'https://discord.com/api/v10';

/**
 * Standart Discord webhook isteğini ilet
 */
const forwardWebhook = async (req, res) => {
  const { webhookId, webhookToken } = req.params;
  const webhookUrl = `${DISCORD_API_BASE}/webhooks/${webhookId}/${webhookToken}`;
  
  try {
    logger.info(`Webhook isteği ${webhookUrl} adresine iletiliyor`);
    
    // Orijinal istekteki sorgu parametrelerini kopyala
    const queryParams = new URLSearchParams(req.query).toString();
    const targetUrl = queryParams ? `${webhookUrl}?${queryParams}` : webhookUrl;
    
    const response = await axios.post(targetUrl, req.body, {
      headers: {
        'Content-Type': 'application/json',
        'User-Agent': 'FoxDiscord-WebhookProxy/1.0'
      }
    });
    
    logger.info(`Webhook başarıyla iletildi, Discord ${response.status} durum koduyla yanıt verdi`);
    
    // Discord'un yanıtını döndür
    return res.status(response.status).json(response.data);
    
  } catch (error) {
    handleAxiosError(error, res);
  }
};

/**
 * Axios hatalarını yönet
 */
const handleAxiosError = (error, res) => {
  if (error.response) {
    // İstek yapıldı ve sunucu 2xx aralığı dışında bir durum koduyla yanıt verdi
    logger.error(`Discord API hatası: ${error.response.status} - ${JSON.stringify(error.response.data)}`);
    return res.status(error.response.status).json({
      status: 'error',
      message: 'Discord API hatası',
      details: error.response.data
    });
  } else if (error.request) {
    // İstek yapıldı ancak yanıt alınamadı
    logger.error(`Discord API'den yanıt alınamadı: ${error.message}`);
    return res.status(502).json({
      status: 'error',
      message: 'Discord API yanıt vermiyor'
    });
  } else {
    // İsteği hazırlarken bir hata oluştu
    logger.error(`İstek hazırlanırken hata oluştu: ${error.message}`);
    return res.status(500).json({
      status: 'error',
      message: 'Webhook işlenirken sunucu hatası oluştu'
    });
  }
};

module.exports = {
  forwardWebhook
}; 