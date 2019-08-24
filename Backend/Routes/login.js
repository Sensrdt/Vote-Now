const { client } = require('../server');
// Login Users
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { phn, passw } = req.body;
  const db = client.db('Db');
  db.collection('voters').findOne({ phoneNumber: phn }, (err, user) => {
    if (user != null) {
      if (user.phoneNumber === phn && user.password === passw) {
        console.log(user.id);
        res.status(200).send({
          Done: true,
          ID: user.id,
          Name: user.name,
          Phone: user.phoneNumber,
        });
      } else
        res.status(400).send({
          Done: false,
        });
    } else
      res.status(404).send({
        Done: false,
      });
  });
};
