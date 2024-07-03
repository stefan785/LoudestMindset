const User = require("../models/user");
const Comment = require("../models/comments");
const Shout = require("../models/shouts");


exports.addComment = async (req, res) => {
  const { userId, shoutId, text } = req.body;
  const apiKey = req.header("apiKey");

  if (!userId || !shoutId || !text) {
    return res.status(405).send("Invalid input.");
  }

  try {
    const user = await User.findByPk(userId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    const shoutExists = await Shout.findByPk(shoutId);
    if (!shoutExists) {
      return res.status(404).send("Shout not found.");
    }

    const newComment = await Comment.create({
      userId,
      shoutId,
      text,
    });

    const response = {
      id: newComment.id,
      userId: newComment.userId,
      shoutId: newComment.shoutId,
      text: newComment.text,
      timestamp: newComment.createdAt,
    };

    res.status(200).json(response);
  } catch (error) {
    console.error("Error adding comment:", error);
    res.status(500).send("error");
  }
};

exports.getCommentsByShoutId = async (req, res) => {
  const shoutId = parseInt(req.params.shoutId, 10);

  if (isNaN(shoutId)) {
    return res.status(400).send("Invalid shout ID.");
  }

  try {
    const comments = await Comment.findAll({
      where: { shoutId: shoutId },
    });

    if (comments.length === 0) {
      return res.status(404).send("Comments not found.");
    }

    const transformedComments = comments.map((comment) => {
      return {
        id: comment.id,
        userId: comment.userId,
        shoutId: comment.shoutId,
        text: comment.text,
        timestamp: comment.createdAt,
      };
    });

    res.status(200).json(transformedComments);
  } catch (error) {
    console.error("Error fetching comments:", error);
    res.status(500).send("error");
  }
};

exports.editComment = async (req, res) => {
  const { userId, shoutId, text } = req.body;
  const apiKey = req.header("apiKey");

  if (!userId || !shoutId || !text) {
    return res.status(400).send("Missing required fields.");
  }

  if (!apiKey) {
    return res.status(401).send("API key required.");
  }

  try {
    const user = await User.findByPk(userId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    const comment = await Comment.findOne({
      where: { userId: userId, shoutId: shoutId },
    });
    if (!comment) {
      return res.status(404).send("Comment not found.");
    }

    if (comment.userId !== userId) {
      return res.status(403).send("Permission denied. Can only edit your own comments.");
    }

    await comment.update({ text });
    const updatedComment = await comment.reload();

    res.status(200).json({
      id: updatedComment.id,
      userId: updatedComment.userId,
      shoutId: updatedComment.shoutId,
      text: updatedComment.text,
      timestamp: updatedComment.updatedAt,
    });
  } catch (error) {
    console.error("Error editing comment:", error);
    res.status(500).send("error");
  }
};

exports.deleteComment = async (req, res) => {
  const commentId = parseInt(req.params.commentId, 10);
  const apiKey = req.header("apiKey");

  if (isNaN(commentId)) {
    return res.status(400).send("Invalid comment ID.");
  }

  if (!apiKey) {
    return res.status(403).send("API key required.");
  }

  try {
    const comment = await Comment.findByPk(commentId);
    if (!comment) {
      return res.status(404).send("Comment not found.");
    }

    const user = await User.findByPk(comment.userId);
    if (!user || apiKey !== user.password) {
      return res.status(404).send("User not found or unauthorized.");
    }

    await comment.update({ text: "Comment deleted" });

    res.status(200).send("Comment deleted.");
  } catch (error) {
    console.error("Error marking comment as deleted:", error);
    res.status(500).send("error");
  }
};
