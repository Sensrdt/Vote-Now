const { client } = require('../../server');

// Checking if the phone number already exists or not
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { phn } = req.body;
  const db = client.db('Db');
  db.collection('voters').findOne({ phoneNumber: phn }, (err, user) => {
    if (user === null) {
      console.log(`${phn} Done`);
      res.status(200).send({
        Done: true,
      });
    } else {
      console.log(phn);
      res.status(408).send({
        Done: false,
        Message: 'Number allready exists',
      });
    }
  });
};
