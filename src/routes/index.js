const express = require('express');
const webhookRoutes = require('./webhook.routes');
const router = express.Router();

// Ana API endpointi
router.get('/', (req, res) => {
  res.json({
    name: 'FoxDiscord API',
    version: '1.0.0',
    status: 'active'
  });
});

// Webhook rotaları
router.use('/webhook', webhookRoutes);

module.exports = router; 