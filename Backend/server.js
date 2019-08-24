const { MongoClient } = require('mongodb');

const uri = 'mongodb://192.168.43.120/';
// 'mongodb+srv://E-Voting-App:e-voting-app@e-voting-app-7ooq0.mongodb.net/test?retryWrites=true&w=majority';

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
