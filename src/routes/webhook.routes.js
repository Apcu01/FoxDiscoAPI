const express = require('express');
const router = express.Router();
const webhookController = require('../controllers/webhook.controller');
const { validateWebhookUrl } = require('../middleware/validation');

/**
 * @route POST /api/webhook/:webhookId/:webhookToken
 * @description Discord'a webhook isteğini ilet
 * @access Genel
 */
router.post('/:webhookId/:webhookToken', validateWebhookUrl, webhookController.forwardWebhook);

module.exports = router; 