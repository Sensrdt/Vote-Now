const { client } = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res`
 */
module.exports = (req, res) => {
  const { command } = req.params;
  const { orgName, id: ID } = req.body;
  const db = client.db('Db');
  switch (command) {
    case 'status':
      db.collection('organisation').findOne({ orgName }, (err, field) => {
        if (field !== null) {
          const { admins } = field;
          let found = false;
          for (const i in admins) {
            if (admins[i].id === ID) {
              res.send({ status: admins[i].status });
              found = true;
            }
          }
          if (!found) res.send({ ERROR: 'Not found' });
        }
      });
      break;
    case 'startVote':
      db.collection('organisation').findOne({ orgName }, (err, field) => {
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
          if (!found) res.send({ Error: 'Not found' });
          res.send({});
        }
      });
      break;
    case 'stopVote':
      db.collection('organisation').findOne({ orgName }, (err, field) => {
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
      break;
    default:
  }
};
