const fs = require("fs");
const path = require("path");
const User = require("../models/user");
const Photo = require("../models/photos");

exports.uploadPhoto = async (req, res) => {
  const userId = parseInt(req.params.userId, 10);
  const apiKey = req.header("apiKey");

  if (isNaN(userId) || !apiKey) {
    return res.status(400).send("Invalid input.");
  }

  try {
    const userToUpdate = await User.findByPk(userId);
    if (!userToUpdate || apiKey !== userToUpdate.password) {
      return res.status(403).send("User not found or unauthorized.");
    }

    if (!req.files || Object.keys(req.files).length === 0) {
      return res.status(400).send("No files were uploaded.");
    }

    const uploadedPhoto = req.files.photo;
    const timestamp = new Date().getTime();
    const subDirectory = path.join(
      __dirname,
      "..",
      "uploadedPhotos",
      userId.toString(),
      timestamp.toString()
    );

    fs.mkdirSync(subDirectory, { recursive: true });
    const filePath = path.join(subDirectory, uploadedPhoto.name);
    uploadedPhoto.mv(filePath, async (err) => {
      if (err) {
        console.error("Error moving the file:", err);
        return res.status(500).send("Could not upload the photo.");
      }

      const newPhoto = await Photo.create({
        userId: userId,
        filePath: `/uploadedPhotos/${userId}/${timestamp}/${uploadedPhoto.name}`,
      });

      res
        .status(200)
        .json({
          id: newPhoto.id,
          userId: newPhoto.userId,
          url: newPhoto.filePath,
        });
    });
  } catch (error) {
    console.error("Error uploading photo:", error);
    res.status(500).send("error");
  }
};

exports.getPhoto = async (req, res) => {
  const id = parseInt(req.params.id, 10);

  if (isNaN(id)) {
    return res.status(400).send("Invalid photo ID.");
  }

  try {
    const photo = await Photo.findByPk(id);
    if (!photo) {
      return res.status(404).send("Photo not found.");
    }

    const photoUrl = `${req.protocol}://${req.get("host")}${photo.filePath}`;
    res.status(200).json({ id: photo.id, userId: photo.userId, url: photoUrl });
  } catch (error) {
    console.error("Error retrieving photo:", error);
    res.status(500).send("error");
  }
};

exports.getAllPhotos = async (req, res) => {
  const userId = parseInt(req.params.userId, 10);

  if (isNaN(userId)) {
    return res.status(400).send("Invalid user ID.");
  }

  try {
    const photos = await Photo.findAll({ where: { userId } });
    if (!photos.length) {
      return res.status(404).send("Photos not found.");
    }

    res
      .status(200)
      .json(
        photos.map((photo) => ({
          id: photo.id,
          userId: photo.userId,
          url: photo.filePath,
        }))
      );
  } catch (error) {
    console.error("Error retrieving photos:", error);
    res.status(500).send("error");
  }
};

exports.deletePhoto = async (req, res) => {
  const photoId = parseInt(req.params.photoId, 10);
  const apiKey = req.header("apiKey");

  if (isNaN(photoId) || !apiKey) {
    return res.status(400).send("Invalid input.");
  }

  try {
    const photo = await Photo.findByPk(photoId);
    if (!photo) {
      return res.status(404).send("Photo not found.");
    }

    const user = await User.findByPk(photo.userId);
    if (!user || apiKey !== user.password) {
      return res.status(403).send("User not found or unauthorized.");
    }

    const uploadPath = path.join(__dirname, "..", photo.filePath);
    fs.unlink(uploadPath, async (err) => {
      if (err) {
        console.error("Error deleting file:", err);
        return res.status(500).send("Failed to delete photo file.");
      }

      await photo.destroy();
      res.status(200).send("Photo deleted.");
    });
  } catch (error) {
    console.error("Error deleting photo:", error);
    res.status(500).send("error");
  }
};
