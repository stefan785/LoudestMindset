const User = require("../models/user");
const Listening = require("../models/listen");


exports.listenToUser = async (req, res) => {
  const { senderId, receiverId } = req.body;
  const apiKey = req.header("apiKey");

  if (!senderId || !receiverId) {
    return res.status(405).send("Invalid input.");
  }

  try {
    const receiver = await User.findByPk(receiverId);
    if (!receiver || apiKey !== receiver.password) {
      return res.status(404).send("Receiver not found or unauthorized.");
    }

    const sender = await User.findByPk(senderId);
    if (!sender) {
      return res.status(404).send("Sender not found.");
    }

    const existingEntry = await Listening.findOne({
      where: { senderId, receiverId },
    });
    if (existingEntry) {
      return res.status(409).send("Already listening to this user.");
    }

    const listening = await Listening.create({ senderId, receiverId });

    res.status(200).json({
      id: listening.id,
      senderId: listening.senderId,
      receiverId: listening.receiverId,
    });
  } catch (error) {
    console.error("Error in listenToUser:", error);
    res.status(500).send("error");
  }
};

exports.getListeningByUserId = async (req, res) => {
  const userId = parseInt(req.params.userId, 10);

  if (isNaN(userId)) {
    return res.status(400).send("Invalid user ID.");
  }

  try {
    const listeningData = await Listening.findAll({
      where: { receiverId: userId },
    });

    if (!listeningData.length) {
      return res.status(404).send("Listening data not found.");
    }

    const transformedData = listeningData.map((listen) => ({
      id: listen.id,
      senderId: listen.senderId,
      receiverId: listen.receiverId,
    }));

    res.status(200).json(transformedData);
  } catch (error) {
    console.error("Error in getListeningByUserId:", error);
    res.status(500).send("error");
  }
};

exports.unlistenToUser = async (req, res) => {
  const { senderId, receiverId } = req.body;
  const apiKey = req.header("apiKey");

  if (!senderId || !receiverId) {
    return res.status(405).send("Invalid input.");
  }

  try {
    const receiver = await User.findByPk(receiverId);
    if (!receiver || apiKey !== receiver.password) {
      return res.status(404).send("Receiver not found or unauthorized.");
    }

    const listening = await Listening.findOne({
      where: { senderId, receiverId },
    });
    if (!listening) {
      return res.status(404).send("Listening relationship not found.");
    }

    await listening.destroy();
    res.status(200).send("Unlistening to user successful.");
  } catch (error) {
    console.error("Error in unlistenToUser:", error);
    res.status(500).send("error");
  }
};
