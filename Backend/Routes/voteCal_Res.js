const { client } = require('../server');

module.exports = (req, res) => {
  const { Admin_id } = req.params;
  const { voteCode } = req.body;

  const db = client.db('Db');
  db.collection('organisation').find();
};
