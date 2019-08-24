const { client } = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */

module.exports = (req, res) => {
  const { id } = req.params;

  const { voteCode, orgName } = req.body;
  let voteFound = false;
  const db = client.db('Db');
  db.collection('organisation').findOne({ orgName }, (err, field) => {
    if (field !== null) {
      const { admins, voters } = field;
      // eslint-disable-next-line
      for (const { voteCode: vCode } of admins) {
        if (vCode === voteCode) voteFound = true;
      }

      if (voteFound && voters.includes(id))
        res.status(200).send({
          Status: 200,
          Done: true,
        });
      else
        res.status(404).send({
          Status: 404,
          Done: true,
        });
    }
  });
};
// /:id/list_checking
