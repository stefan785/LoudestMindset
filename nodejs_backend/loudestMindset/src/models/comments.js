const { DataTypes } = require("sequelize");
const sequelize = require("../config/database");

const Comment = sequelize.define("Comment", {
  userId: DataTypes.INTEGER,
  shoutId: DataTypes.INTEGER,
  text: DataTypes.STRING,
});

module.exports = Comment;
