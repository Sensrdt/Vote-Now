const { client } = require('../server');

exports.poll = (req, res) => {
  const { Admin_id } = req.params;
  const { voteCode, orgName, name } = req.body;

  const db = client.db('Db');
  db.collection('organisation').findOne({ orgName }, (err, field) => {
    if (field === null) res.send({ Done: false });
    else {
      const admin = field.admins[0];
      if (admin.list === null) admin.list = [];
      if (admin.list[name] === null) admin.list[name] = 0;
      admin.list[name] += 1;
      db.collection('organisation').updateOne({ orgName }, { $set: { admin } });
      res.send({ Done: true });
    }
  });
};
