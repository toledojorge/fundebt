import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar as BarChart } from "react-chartjs-2";
import { getBarChartOptions } from "../../../utils/charts.js";
import { Box } from "@chakra-ui/react";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

export default function DebtsSumByMonthBarChart({ chartData }) {
  return (
    <Box width="100%" height="100%" position="relative" mt={5}>
      <BarChart
        width="100%"
        height="50%"
        options={getBarChartOptions("")}
        data={chartData}
      />
    </Box>
  );
}
