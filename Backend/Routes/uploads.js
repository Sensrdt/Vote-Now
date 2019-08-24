const { client } = require('../server');

// Upload images for verification
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  console.log(req.params.id);
  const { photo, id } = req.body;
  const db = client.db('Db');
  db.collection('voters').updateOne(
    { id: req.params.id },
    { $set: { id, photo } },
    err => {
      if (err) throw err;
    }
  );
  // fs.writeFile(
  //   `./images/${req.params.id}.jpg`,
  //   Buffer.from(photo, 'base64'),
  //   e => {
  //     console.log(e);
  //   }
  // );
  // eslint-disable-next-line no-unused-vars
  const image = Buffer.from(photo, 'base64');
  // eslint-disable-next-line no-unused-vars
  const card = Buffer.from(id, 'base64');
  // use image and card to compare face in future
  res.send({});
};
