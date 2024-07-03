const User = require("../models/user");
const Shout = require("../models/shouts");

exports.addShout = async (req, res) => {
  const apiKey = req.header("apiKey");
  const { userId, text } = req.body;

  if (!userId || !text) {
    return res.status(405).send("Invalid input.");
  }

  try {
    const user = await User.findByPk(userId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    const newShout = await Shout.create({ userId, text });
    res.status(200).json({
      id: newShout.id,
      userId: newShout.userId,
      text: newShout.text,
      timestamp: newShout.updatedAt,
    });
  } catch (error) {
    console.error("Error creating shout:", error);
    res.status(500).send("An error occurred.");
  }
};

exports.getShoutsByUserId = async (req, res) => {
  const userId = parseInt(req.params.userId, 10);

  if (isNaN(userId)) {
    return res.status(400).send("Invalid user ID.");
  }

  try {
    const shouts = await Shout.findAll({
      where: { userId: userId },
      order: [["createdAt", "ASC"]],
    });
    if (!shouts.length) {
      return res.status(404).send("Shouts not found.");
    }
    res
      .status(200)
      .json(
        shouts.map((shout) => ({
          id: shout.id,
          userId: shout.userId,
          text: shout.text,
          timestamp: shout.createdAt.toISOString(),
        }))
      );
  } catch (error) {
    console.error("Error getting shouts:", error);
    res.status(500).send("error");
  }
};

exports.editShout = async (req, res) => {
  const { id, userId, text } = req.body;
  const apiKey = req.header("apiKey");

  if (!id || !userId || !text || !apiKey) {
    return res.status(405).send("Invalid input.");
  }

  try {
    const user = await User.findByPk(userId);
    const shout = await Shout.findByPk(id);
    if (
      !user ||
      !shout ||
      apiKey !== user.password ||
      shout.userId !== userId
    ) {
      return res.status(404).send("User or shout not found or unauthorized.");
    }

    await shout.update({ text });
    res
      .status(200)
      .json({
        id: shout.id,
        userId: shout.userId,
        text: shout.text,
        timestamp: shout.updatedAt,
      });
  } catch (error) {
    console.error("Error editing shout:", error);
    res.status(500).send("error");
  }
};

exports.deleteShout = async (req, res) => {
  const shoutId = req.params.shoutId;
  const apiKey = req.header("apiKey");

  if (!shoutId || !apiKey) {
    return res.status(400).send("Invalid shout ID or missing apiKey.");
  }

  try {
    const shout = await Shout.findByPk(shoutId);
    if (!shout) {
      return res.status(404).send("Shout not found.");
    }

    const user = await User.findByPk(shout.userId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    await shout.update({ text: "Shout deleted" });
    res.status(200).send("Shout deleted.");
  } catch (error) {
    console.error("Error deleting shout:", error);
    res.status(500).send("error");
  }
};
