const { client } = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res`
 */
module.exports = (req, res) => {
  const { command } = req.params;
  const { id: ID } = req.body;
  const db = client.db('Db');
  switch (command) {
    case 'status':
      db.collection('admin').findOne({ id: ID }, (err, dat) => {
        if (dat === null) {
          res.status(404).send({ Done: false });
          return;
        }
        const orgName = dat.Organisation[0].Name;
        db.collection('organisation').findOne({ orgName }, (e, field) => {
          if (field !== null) {
            const { admins } = field;
            let found = false;
            for (const i in admins) {
              if (admins[i].id === ID) {
                res.send({ status: admins[i].status, orgName });
                found = true;
              }
            }
            if (!found) res.status(404).send({ Done: false });
          }
        });
      });
      break;
    case 'startVote':
      db.collection('admin').findOne({ id: ID }, (err, dat) => {
        if (dat === null) {
          res.status(404).send({ Done: false });
          return;
        }
        const orgName = dat.Organisation[0].Name;
        db.collection('organisation').findOne({ orgName }, (e, field) => {
          if (field !== null) {
            const { admins } = field;
            let found = false;
            for (const i in admins)
              if (admins[i].id === ID) {
                found = true;
                const obj = admins[i];
                admins.splice(i);
                if (obj.status !== 'N') {
                  res.send({ Error: 'Create the vote first' });
                  return;
                }
                obj.status = 'o';
                console.log(obj);
                admins.push(obj);
                db.collection('organisation').updateOne(
                  { orgName },
                  { $set: { admins } }
                );
              }
            if (!found) res.send({ Done: false });
            res.send({ Done: true });
          }
        });
      });
      break;
    case 'stopVote':
      db.collection('admin').findOne({ id: ID }, (err, dat) => {
        if (dat === null) {
          res.status(404).send({ Done: false });
          return;
        }
        const orgName = dat.Organisation[0].Name;
        db.collection('organisation').findOne({ orgName }, (e, field) => {
          if (field !== null) {
            const { admins } = field;
            let found = false;
            for (const i in admins)
              if (admins[i].id === ID) {
                found = true;
                const obj = admins[i];
                admins.splice(i);
                if (obj.status !== 'o') {
                  res.send({ Error: 'Start the vote first' });
                  return;
                }
                obj.status = 'E';
                console.log(obj);
                admins.push(obj);
                db.collection('organisation').updateOne(
                  { orgName },
                  { $set: { admins } }
                );
              }
            if (!found) res.send({ Error: 'Not found' });
            res.send({});
          }
        });
      });
      break;
    default:
  }
};
