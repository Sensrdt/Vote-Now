const { client } = require('../server');

/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { id: ID } = req.params;
  const response = [];
  const { candidates, orgName } = req.body;
  // console.log(candidates);
  const db = client.db('Db');
  db.collection('organisation').findOne({ orgName }, (err, field) => {
    console.log('ajsdk');
    if (field !== null) {
      const { admins } = field;
      let found = false;
      for (const i in admins)
        if (admins[i].id === ID) {
          found = true;
          const obj = admins[i];
          if (obj.status !== 'N') {
            // res.send({ Error: 'Ongoing' });
            response.push({ Error: 'Ongoing' });
            return;
          }
          if (obj.candidates !== null) {
            // res.send({ Error: 'Already set' });
            response.push({ Error: 'Already set' });
            return;
          }
          db.collection('organisation').updateOne(
            { orgName },
            { $set: { candidates: candidates.split(','), count: 0 } }
          );
        }
      if (!found) response.push({ Error: 'Not found' }); // res.send({ Error: 'Not found' });
      res.write({});
    }
  });
  res.send(response);
};
