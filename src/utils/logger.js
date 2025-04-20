const winston = require('winston');
const path = require('path');
const fs = require('fs');

// Log dizini yoksa oluştur
const logDir = 'logs';
if (!fs.existsSync(logDir)) {
  fs.mkdirSync(logDir);
}

// Log formatlarını tanımla
const consoleFormat = winston.format.combine(
  winston.format.colorize(),
  winston.format.timestamp(),
  winston.format.printf(
    ({ timestamp, level, message }) => `${timestamp} ${level}: ${message}`
  )
);

const fileFormat = winston.format.combine(
  winston.format.timestamp(),
  winston.format.json()
);

// Logger örneği oluştur
const logger = winston.createLogger({
  level: process.env.LOG_LEVEL || 'info',
  transports: [
    new winston.transports.Console({
      format: consoleFormat
    }),
    new winston.transports.File({
      filename: path.join(logDir, 'error.log'),
      level: 'error',
      format: fileFormat,
      maxsize: 5242880, // 5MB
      maxFiles: 5
    }),
    new winston.transports.File({
      filename: path.join(logDir, 'combined.log'),
      format: fileFormat,
      maxsize: 5242880, // 5MB
      maxFiles: 5
    })
  ],
  exitOnError: false
});

module.exports = logger; 