import { truncate2decimals } from "./format.js";

export const rawToChartDataset = (raw) => {
  const mn = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
  return mn.map((v, _) => {
    const item = raw.find((r) => r.month === v);
    if (item) {
      return truncate2decimals(item.total);
    }
    return 0;
  });
};

export const getBarChartOptions = (title) => {
  return {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: title,
      },
    },
  };
};

export const getBarChartDatasets = (label1, label2, data1, data2, bg1, bg2) => {
  return [
    {
      label: label1,
      data: data1,
      backgroundColor: bg1,
    },
    {
      label: label2,
      data: data2,
      backgroundColor: bg2,
    },
  ];
};
