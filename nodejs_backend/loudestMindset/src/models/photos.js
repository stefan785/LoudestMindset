const { DataTypes } = require("sequelize");
const sequelize = require("../config/database");

const Photo = sequelize.define("Photo", {
  userId: DataTypes.INTEGER,
  filePath: DataTypes.STRING,
});

module.exports = Photo;
