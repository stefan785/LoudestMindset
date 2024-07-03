const { DataTypes } = require("sequelize");
const sequelize = require("../config/database");

const Shout = sequelize.define("Shout", {
  userId: DataTypes.INTEGER,
  text: DataTypes.STRING,
});

module.exports = Shout;
