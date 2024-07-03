const express = require("express");
const router = express.Router();
const shoutController = require("../controllers/shoutController");

router.post("/createShout", shoutController.addShout);
router.get("/getShouts/:userId", shoutController.getShoutsByUserId);
router.put('/editShout', shoutController.editShout);
router.delete('/deleteShout/:shoutId', shoutController.deleteShout);

module.exports = router;
