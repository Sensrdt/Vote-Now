/**
 * @param {Express.Request} req
 * @param {Express.Response} res
 */
module.exports = (req, res) => {
  console.log(req.params.voteId, req.params.id);
  res.send({});
};
