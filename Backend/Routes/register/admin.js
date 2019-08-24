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
  if (id != null) {
    db.collection('admin').findOne({ Admin_Id: id }, (err, user) => {
      if (user === null) {
        db.collection('admin').insertOne(admin_inst, error => {
          if (error) {
            console.log(error);
          } else {
            res.status(200).send({
              Done: true,
            });
            console.log('Done Admin insert');
          }
        });
      } else {
        res.status(200).send({
          Done: true,
          Message: 'Insert over there',
        });
      }
    });
  } else {
    res.status(400).send('Enter the id');
  }
};
