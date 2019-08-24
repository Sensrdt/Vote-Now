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
const uploadList = require('./Routes/uploadList');
const listchecking = require('./Routes/list_checking');
const voteControl = require('./Routes/voteControl');
const updateCandidates = require('./Routes/updateCandidate');

const app = express();
app.use(express.json());
const port = 5555;
/*
  /register => Name, Org Name, DOB, Email, Phone Number, Password (Done)
  /login => email, password (Done)
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
app.post('/:id/voter_list_checking', listchecking);
app.post('/uploadList', uploadList);
app.post('/voteControl/:command', voteControl);
app.post('/:id/list_checking', listchecking);
app.post('/:id/updateCandidate', updateCandidates);

connect({
  app,
  port,
});
