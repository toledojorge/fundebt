const truncate2decimals = (num) => {
  return Math.trunc(num * 100) / 100;
};

export { truncate2decimals };
