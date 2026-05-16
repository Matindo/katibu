module.exports = {
  preset: '@vue/cli-plugin-unit-jest',
  testMatch: ['**/__tests__/**/*.spec.js', '**/*.spec.js'],
  transform: {
    '^.+\\.vue$': 'vue-jest',
    '^.+\\.js$': 'babel-jest'
  },
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1'
  }
}
