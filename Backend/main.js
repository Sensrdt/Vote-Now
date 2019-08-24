const express = require('express');
const { connect } = require('./server');
const {
  register,
  registerCheck,
  orgRegister,
  adminRegister,
} = require('./Routes/register');
const login = require('./Routes/login');
const uploads = require('./Routes/uploads');
const ongoing = require('./Routes/ongoing');
const uploadList = require('./Routes/uploadList');
const listchecking = require('./Routes/list_checking');

const app = express();
app.use(express.json());
const port = 5555;
/*
  /register => Name, Org Name, DOB, Email, Phone Number, Password (Done)
  /login => email, password (Done)
  /:id/upload1 => Photo1 (Done) 
  /:id/upload2 => Photo2 (Done)
  /:admin_id/register => Organisation_Name  orgRegister,
  adminRegister
  /:admin_id/org_name/vote_id => generate vote ID
  /:id/vote_id/check => 
  /:admin_id/org_name/upload => upload file 
*/

app.post('/register_check', registerCheck);
app.post('/register', register);
app.post('/login', login);
app.post('/admin/register', adminRegister);
app.post('/:id/upload', uploads);
app.post('/:id/org_register', orgRegister);
app.post('/:voteId/:id/ongoing', ongoing);
app.post('/:id/voter_list_checking', listchecking);
app.post('/uploadList', uploadList);

connect({
  app,
  port,
});
