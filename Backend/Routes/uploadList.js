const { client } = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { list, org } = req.body;
  const db = client.db('Db');

  db.collection('organisation').updateOne(
    { orgName: org },
    { $set: { voters: list.split(',') } },
    error => {
      if (error) res.send(error);
      else res.send('Uploaded');
    }
  );
};
