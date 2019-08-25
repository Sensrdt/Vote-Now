const { client } = require('../server');
/**
 * @param {Express.Request} req
 * @param {Express.Response} res`
 */
module.exports = (req, res) => {
  const { command } = req.params;
  const { id: ID } = req.body;
  console.log(command);
  const db = client.db('Db');
  switch (command) {
    case 'status':
      db.collection('admin').findOne({ Admin_Id: ID }, (err, dat) => {
        if (dat === null) {
          console.log('Enetered');
          res.status(404).send({ Done: false, Message: 'Not yet started' });
          return;
        }
        const orgName = dat.Organisation[0].Name;
        console.log(orgName);
        db.collection('organisation').findOne({ orgName }, (e, field) => {
          if (field !== null) {
            const { admins } = field;
            let found = false;
            for (const i in admins) {
              if (admins[i].id === ID) {
                console.log('Response');
                // res
                //   .status(200)
                //   .send({ Done: true, status: admins[i].status, orgName });
                found = true;
                const x = admins[i];
                admins.splice(i);
                if (x.status === 'o') {
                  res.send({ Done: true, status: 'o', orgName });
                  return;
                }
                if (x.status === 'N') {
                  res.send({ Done: true, status: 'N', orgName });
                  return;
                }
                if (x.status === 'E') {
                  res.send({ Done: true, status: 'E', orgName });
                  return;
                }
                res.send({ Done: false, status: 'Empty', orgName });
                return;
              }
            }
            if (!found) res.status(404).send('Nothing');
          } else {
            res.status(404).send('Nothing');
            console.log('NULL@@');
          }
        });
      });
      break;
    case 'startVote':
      console.log('Entered start');
      db.collection('admin').findOne({ Admin_Id: ID }, (err, dat) => {
        if (dat === null) {
          console.log('jabsd');
          res.status(404).send({ Done: false });
          return;
        }
        const orgName = dat.Organisation[0].Name;
        console.log(orgName);
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
                  res.status(200).send({ Error: 'Create the vote first' });
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
      db.collection('admin').findOne({ Admin_Id: ID }, (err, dat) => {
        if (dat === null) {
          console.log('kjadyu');
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
            res.status(200).send({ Done: true });
          }
        });
      });
      break;
    default:
  }
};
