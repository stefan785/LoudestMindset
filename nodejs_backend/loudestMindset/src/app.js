require("dotenv").config();
const express = require("express");
const fileUpload = require("express-fileupload");
const path = require("path");
const app = express();
const sequelize = require("./config/database");

// Routes
const userRoutes = require("./routes/users");
const photoRoutes = require("./routes/photoRoutes");
const shoutRoutes = require("./routes/shoutRoutes");
const listenRoutes = require("./routes/listenRoutes");
const commentRoutes = require("./routes/commentRoutes");
const messageRoutes = require("./routes/messageRoutes");


app.use(express.json());
app.use(fileUpload());


app.use("/uploads", express.static(path.join(__dirname, "uploads")));

// Base route
app.get("/", (req, res) => {
  res.status(200).send("Loudest Mindset API is running...");
});

// Routes
app.use("/", userRoutes);
app.use("/", photoRoutes);
app.use("/", shoutRoutes);
app.use("/", listenRoutes);
app.use("/", commentRoutes);
app.use("/", messageRoutes);

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
  sequelize.sync({ force: true }) 
    .then(() => {
      console.log("Database synced and tables created/updated");
    })
    .catch((error) => {
      console.error("Failed to sync database:", error);
    });
});
