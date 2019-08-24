const { client } = require('../../server');

/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  const { id } = req.body;

  const admin_inst = {
    Admin_Id: id,
  };
  const db = client.db('Db');
  db.collection('admin').findOne({ Admin_Id: id }, (err, user) => {
    if (user === null) {
      db.collection('admin').insertOne(admin_inst, error => {
        if (error) {
          console.log(error);
        } else {
          res.send('Done');
          console.log('Done Admin insert');
        }
      });
    } else {
      res.status(404).send({
        Done: false,
        Message: 'Please Retry',
      });
    }
  });
};
