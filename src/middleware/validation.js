const logger = require('../utils/logger');

/**
 * Webhook URL parametrelerinin doğru formatta olup olmadığını doğrular
 */
const validateWebhookUrl = (req, res, next) => {
  const { webhookId, webhookToken } = req.params;
  
  // Webhook ID'sinin geçerli bir Discord kimliği (numeric ID) olup olmadığını kontrol et
  if (!webhookId || !/^\d+$/.test(webhookId)) {
    logger.warn(`Geçersiz webhook ID formatı: ${webhookId}`);
    return res.status(400).json({
      status: 'error',
      message: 'Geçersiz webhook ID formatı'
    });
  }
  
  // Webhook token'ın mevcut olup olmadığını ve doğru formatta olup olmadığını kontrol et
  if (!webhookToken || !/^[A-Za-z0-9_-]+$/.test(webhookToken)) {
    logger.warn(`Geçersiz webhook token formatı: ${webhookToken}`);
    return res.status(400).json({
      status: 'error',
      message: 'Geçersiz webhook token formatı'
    });
  }
  
  next();
};

module.exports = {
  validateWebhookUrl
}; 