module.exports = {
  env: {
    commonjs: true,
    es6: true,
    node: true,
  },
  extends: ['airbnb-base', 'prettier'],
  plugins: ['prettier'],
  globals: {
    Atomics: 'readonly',
    SharedArrayBuffer: 'readonly',
  },
  parserOptions: {
    ecmaVersion: 2018,
  },
  rules: {
    camelcase: 'off',
    'no-console': 'off',
    quotes: ['error', 'single'],
    'prettier/prettier': ['error'],
    'no-underscore-dangle': 'off',
    'no-restricted-syntax': 'off',
  },
};
