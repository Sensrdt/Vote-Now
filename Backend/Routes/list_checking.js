const { client } = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */

module.exports = (req, res) => {
  const { id } = req.params;

  const { voteCode, orgName } = req.body;
  let found = false;
  const db = client.db('Db');
  db.collection('organisation').findOne({ orgName }, (err, field) => {
    if (field !== null) {
      const { admins } = field;
      // eslint-disable-next-line
      for (const i in admins) {
        if (admins[i].voteCode === voteCode) found = true;
        console.log(admins[i]);
      }
      if (!found) res.status(404).send({});
      else res.status(200).send({});
    }
  });
};
// /:id/list_checking
