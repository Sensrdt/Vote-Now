const { client } = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { id: ID } = req.params;
  const { candidates, orgName } = req.body;
  const db = client.db('Db');
  db.collection('organisation').findOne({ orgName }, (err, field) => {
    if (field !== null) {
      const { admins } = field;
      let found = false;
      for (const i in admins)
        if (admins[i].id === ID) {
          found = true;
          const obj = admins[i];
          if (obj.status !== 'N') {
            res.send({ Error: 'Ongoing' });
            return;
          }
          if (obj.candidates !== null) {
            res.send({ Error: 'Already set' });
            return;
          }
          db.collection('organisation').updateOne(
            { orgName },
            { $set: { candidates: candidates.split(',') } }
          );
        }
      if (!found) res.send({ Error: 'Not found' });
      res.send({});
    }
  });
  // res.send({});
};
