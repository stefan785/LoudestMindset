const User = require("../models/user");
const Message = require("../models/messages");

exports.addMessage = async (req, res) => {
  const { senderId, receiverId, text } = req.body;
  const apiKey = req.header("apiKey");

  if (!senderId || !receiverId || !text) {
    return res.status(405).send("Invalid input.");
  }

  try {
    const sender = await User.findByPk(senderId);
    if (!sender || apiKey !== sender.password) {
      return res.status(404).send("Sender not found or unauthorized.");
    }

    const message = await Message.create({ senderId, receiverId, text });
    return res.status(200).json({
      id: message.id,
      senderId: message.senderId,
      receiverId: message.receiverId,
      text: message.text,
      timestamp: message.createdAt,
    });
  } catch (error) {
    console.error("Error creating message:", error);
    return res.status(500).send("error");
  }
};


exports.getReceivedMessages = async (req, res) => {
  const userId = req.params.userId;
  const apiKey = req.header("apiKey");

  if (!userId) {
    return res.status(400).send("User ID is required.");
  }

  try {
    const user = await User.findByPk(userId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    const messages = await Message.findAll({
      where: { receiverId: userId },
      order: [["createdAt", "ASC"]],
    });

    if (messages.length === 0) {
      return res.status(404).send("Messages not found.");
    }

    const formattedMessages = messages.map((msg) => ({
      id: msg.id,
      senderId: msg.senderId,
      receiverId: msg.receiverId,
      text: msg.text,
      timestamp: msg.createdAt.toISOString(),
    }));

    res.json(formattedMessages);
  } catch (error) {
    console.error("Error fetching received messages:", error);
    res.status(500).send("error");
  }
};


exports.getSentMessages = async (req, res) => {
  const userId = req.params.userId;
  const apiKey = req.header("apiKey");

  if (!userId) {
    return res.status(400).send("User ID is required.");
  }

  try {
    const user = await User.findByPk(userId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    const messages = await Message.findAll({
      where: { senderId: userId },
      order: [["createdAt", "ASC"]],
    });

    if (messages.length === 0) {
      return res.status(404).send("No sent messages found.");
    }

    const formattedMessages = messages.map((msg) => ({
      id: msg.id,
      senderId: msg.senderId,
      receiverId: msg.receiverId,
      text: msg.text,
      timestamp: msg.createdAt.toISOString(),
    }));

    res.json(formattedMessages);
  } catch (error) {
    console.error("Error fetching sent messages:", error);
    res.status(500).send("error");
  }
};

exports.editMessage = async (req, res) => {
  const { id, senderId, text } = req.body;
  const apiKey = req.header("apiKey");

  if (!id || !senderId || !text) {
    return res.status(405).send("Invalid input.");
  }

  try {
    const user = await User.findByPk(senderId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    const message = await Message.findByPk(id);
    if (!message) {
      return res.status(404).send("Message not found.");
    }

    await message.update({ text });

    res.status(200).json({
      id: message.id,
      senderId: message.senderId,
      receiverId: message.receiverId,
      text: message.text,
      timestamp: message.createdAt,
    });
  } catch (error) {
    console.error("Error editing message:", error);
    res.status(500).send("error");
  }
};

exports.deleteMessage = async (req, res) => {
  const messageId = parseInt(req.params.messageId, 10);
  const apiKey = req.header("apiKey");

  if (isNaN(messageId)) {
    return res.status(400).send("Invalid message ID.");
  }

  try {
    const message = await Message.findByPk(messageId);
    if (!message) {
      return res.status(404).send("Message not found.");
    }

    const user = await User.findByPk(message.senderId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    await message.update({ text: "Message deleted" });
    res.status(200).send("Message deleted.");
  } catch (error) {
    console.error("Error deleting message:", error);
    res.status(500).send("error");
  }
};
