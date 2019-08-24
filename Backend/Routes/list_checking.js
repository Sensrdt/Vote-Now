const client = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */

module.exports = (req, res) => {
  const { id } = req.params;
  const { voteCode } = req.body;

  const db = client.db('Db');
  db.collection('organisation').findOne();
};
