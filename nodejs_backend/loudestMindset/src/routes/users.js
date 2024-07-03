const express = require("express");
const router = express.Router();
const userController = require("../controllers/userController");

router.post("/createUser", userController.createUser);
router.get("/getUserByName/:userName", userController.getUserByName);
router.get("/getUserById/:userId", userController.getUserById);
router.put("/updateUser", userController.updateUser);
router.delete('/deleteUser/:userId', userController.deleteUser);

module.exports = router;
