const { client } = require('../server');

exports.poll = (req, res) => {
  const { orgName, name } = req.body;

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

exports.results = (req, res) => {
  const db = client.db('Db');

  const { orgName } = req.body;
  db.collection(
    'organisation'.findOne({ orgName }, (err, f) => {
      if (f === null || f.admins[0].list === null) {
        res.send({ Done: false });
        return;
      }
      const max = a => a.reduce((m, x) => (m > x ? m : x));
      res.send({ Winner: max(list) });
    })
  );
};
