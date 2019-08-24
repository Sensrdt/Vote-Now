const ids = require('short-id');
const { client } = require('../../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { id } = req.params;
  const { orgName } = req.body;

  const db = client.db('Db');

  db.collection('admin').findOne({ Admin_Id: id }, (err, field) => {
    if (err) console.log(err);
    let { Organisation } = field;
    if (Organisation === null || Organisation === undefined) Organisation = [];
    Organisation.push({ Name: orgName });
    db.collection('admin').updateOne(
      { Admin_Id: id },
      { $set: { Organisation } },
      e => {
        if (err) console.log(e);
      }
    );
  });
  db.collection('organisation').findOne({ orgName }, (err, field) => {
    const vtc = ids.generate();
    if (err) throw err;
    if (field === null) {
      db.collection('organisation').insertOne(
        {
          orgName,
          admins: [{ id, voteCode: vtc }],
        },
        error => {
          if (err) console.log(error);
          else
            res.status(200).send({
              Done: true,
              ID: vtc,
            });
        }
      );
    } else {
      const { admins } = field;
      console.log(admins);
      admins.push({ id });
      console.log(admins);
      db.collection('organisation').updateOne(
        { orgName },
        { $set: { admins } },
        e => {
          if (e) console.log(e);
        }
      );
    }
  });
  res.send({});
};

ids.configure({
  length: 6, // The length of the id strings to generate
  algorithm: 'sha1', // The hashing algoritm to use in generating keys
  salt: Math.random, // A salt value or function
});
