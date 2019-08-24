const { MongoClient } = require('mongodb');

const uri = 'mongodb://172.17.44.49';

const client = new MongoClient(uri, { useNewUrlParser: true });

const connect = async ({ app, port }) => {
  await client.connect(err => {
    if (!err) {
      app.listen(port, () => {
        console.log(`Connected \nServer running on port ${port}`);
      });
    } else {
      console.log(`No ${err}`);
    }
  });
};

module.exports = { client, connect };
