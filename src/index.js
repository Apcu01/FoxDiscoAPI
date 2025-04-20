require('dotenv').config();
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');
const rateLimit = require('express-rate-limit');
const logger = require('./utils/logger');
const routes = require('./routes');

const app = express();
const PORT = process.env.PORT || 5678;

// Güvenlik ara yazılımı
app.use(helmet());
app.use(cors());

// Hız sınırlaması
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 dakika
  max: 100, // her IP için 15 dakikalık pencerede 100 istek sınırı
  standardHeaders: true,
  legacyHeaders: false,
  message: 'Bu IP adresinden çok fazla istek geldi, lütfen daha sonra tekrar deneyin'
});
app.use(limiter);

// İstek günlükleme
app.use(morgan('combined', { stream: { write: message => logger.info(message.trim()) } }));

// JSON isteklerini ayrıştır
app.use(express.json({ limit: '1mb' }));

// Rotalar
app.use('/api', routes);

// Hata işleme
app.use((err, req, res, next) => {
  logger.error(`${err.status || 500} - ${err.message} - ${req.originalUrl} - ${req.method} - ${req.ip}`);
  
  res.status(err.status || 500).json({
    status: 'error',
    message: process.env.NODE_ENV === 'production' ? 'Sunucu hatası' : err.message
  });
});

// Sunucuyu başlat
app.listen(PORT, () => {
  logger.info(`Sunucu ${PORT} portunda çalışıyor`);
});

module.exports = app; // Test amaçlı 