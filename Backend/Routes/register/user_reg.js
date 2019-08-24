const { client } = require('../../server');

/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { name, phn, passw } = req.body;
  const inser_t = {
    name,
    phoneNumber: phn,
    password: passw,
    voteCount: 0,
    id: `id-${phn}-vote(now)`,
  };
  res.send(inser_t);
  console.log(inser_t);
  const db = client.db('Db');
  db.collection('voters').findOne({ phoneNumber: phn }, (err, user) => {
    if (user === null) {
      db.collection('voters').insertOne(inser_t, error => {
        if (error) console.log('Error');
        else console.log('Done');
      });
    } else {
      res.status(408).send({
        Message: 'Please Retry',
      });
    }
  });
};
