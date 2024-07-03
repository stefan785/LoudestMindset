const { DataTypes } = require("sequelize");
const sequelize = require("../config/database");

const Listening = sequelize.define("Listening", {
  senderId: {
    type: DataTypes.INTEGER,
    allowNull: false,
  },
  receiverId: {
    type: DataTypes.INTEGER,
    allowNull: false,
  },
});

module.exports = Listening;
