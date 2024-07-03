const User = require("../models/user");

exports.createUser = async (req, res) => {
  const { name, email, password } = req.body;

  if (!name || !email || !password) {
    return res
      .status(405)
      .send("Invalid input. Name, email, and password are required.");
  }

  try {
    const existingUser = await User.findOne({ where: { email } });
    if (existingUser) {
      return res.status(406).send("Email already taken.");
    }

    const newUser = await User.create({ name, email, password });
    res
      .status(201)
      .json({ id: newUser.id, name: newUser.name, email: newUser.email });
  } catch (error) {
    res.status(500).send("error");
  }
};

exports.getUserByName = async (req, res) => {
  const userName = req.params.userName;

  try {
    const user = await User.findOne({ where: { name: userName } });
    if (!user) {
      return res.status(404).send("User not found");
    }
    res.json({
      id: user.id,
      name: user.name,
      email: user.email,
      status: user.status,
    });
  } catch (error) {
    res.status(500).send("error");
  }
};

exports.getUserById = async (req, res) => {
  const userId = parseInt(req.params.userId, 10);

  if (isNaN(userId)) {
    return res.status(400).send("Invalid user ID format.");
  }

  try {
    const user = await User.findByPk(userId);

    if (!user) {
      return res.status(404).send("User not found.");
    }

    res.json({
      id: user.id,
      name: user.name,
      email: user.email,
      status: user.status,
    });
  } catch (error) {
    res.status(500).send("error");
  }
};

exports.updateUser = async (req, res) => {
  const { id, name, email, password } = req.body;
  const apiKey = req.header("apiKey");

  if (!id || !apiKey) {
    return res.status(400).send("Invalid data");
  }

  try {
    const userToUpdate = await User.findByPk(id);
    if (!userToUpdate || apiKey !== userToUpdate.password) {
      return res.status(404).send("User not found or unauthorized");
    }

    if (name) userToUpdate.name = name;
    if (email) userToUpdate.email = email;
    if (password) userToUpdate.password = password;

    await userToUpdate.save();
    res
      .status(200)
      .json({
        id: userToUpdate.id,
        name: userToUpdate.name,
        email: userToUpdate.email,
      });
  } catch (error) {
    res.status(500).send("error");
  }
};

exports.deleteUser = async (req, res) => {
  const userId = parseInt(req.params.userId, 10);
  const apiKey = req.header("apiKey");

  if (isNaN(userId) || !apiKey) {
    return res.status(400).send("Invalid input");
  }

  try {
    const userToDelete = await User.findByPk(userId);
    if (!userToDelete) {
      return res.status(404).send("User not found");
    }

    if (userToDelete.status === 99) {
      return res.status(403).send("User already deleted");
    }

    const isAuthenticated = apiKey === userToDelete.password;

    userToDelete.name = "deletedUser" + userToDelete.id;
    userToDelete.email = "deletedEmail" + userToDelete.id;
    userToDelete.status = 99;
    await userToDelete.save();

    res.status(200).json({ message: "User deleted" });
  } catch (error) {
    console.error("Error deleting user:", error);
    res.status(500).send("error");
  }
};
