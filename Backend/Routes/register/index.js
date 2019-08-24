/* eslint-disable global-require */

module.exports = {
  register: require('./user_reg'),
  registerCheck: require('./checkUser'),
  adminRegister: require('./admin'),
  orgRegister: require('./org'),
};
