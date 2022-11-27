import { Fragment, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getAllExceptPrincipal as getAllExceptPrincipalUser } from "../../api/users";
import {
  Tabs,
  TabList,
  TabPanels,
  Tab,
  TabPanel,
  Avatar,
  SimpleGrid,
  IconButton,
  useToast,
  Alert,
  AlertIcon,
  AlertDescription,
  AlertTitle,
  Box,
} from "@chakra-ui/react";
import {
  getPaginated as getDebtsWithPagination,
  getBalance as getDebtsBalance,
  markAsPaid as markDebtsAsPaid,
} from "../../api/debts";
import Sidebar from "../../components/SideBar";
import { ChevronRightIcon } from "@chakra-ui/icons";
import { CURRENCY } from "../../config";
import DashboardActions from "./DashboardActions";
import DashboardTable from "./DashboardTable";
import CreateDebtForm from "../../components/forms/CreateDebtForm";
import useAccessToken from "../../hooks/useAccessToken.js";
import { monthLabels } from "../../data/misc.js";
import {
  EDEBT_FILTER_SHOW,
  EDEBT_ORDER_BY,
  EDEBT_ORDER_DIRECTION,
} from "../../enums.js";
import DebtsSumByMonthBarChart from "./charts/DebtsSumByMonthBarChart.js";
import { rawToChartDataset } from "../../utils/charts.js";
import { getSumByMonth as getDebtsSumByMonth } from "../../api/debts";
import { chakraTheme } from "../../theme.js";
import { getBarChartDatasets } from "../../utils/charts.js";

export default function Dashboard() {
  const authUser = useSelector((state) => state.au.v);
  const accessToken = useAccessToken();
  const toast = useToast();
  const navigate = useNavigate();
  const [nonPrincipalUsers, setNonPrincipalUsers] = useState([]);
  const [currentDebtUserId, setCurrentDebtUserId] = useState(-1);
  const [chartData, setChartData] = useState({
    labels: monthLabels,
    datasets: getBarChartDatasets(),
  });
  const [currentUserDebts, setCurrentUserDebts] = useState([]);
  const [sideBarOpen, setSideBarOpen] = useState(false);
  const [markedRows, setMarkedRows] = useState([]);
  const [currentUserBalance, setCurrentUserBalance] = useState([]);
  const [tableLoading, setTableLoading] = useState(false);
  const [filters, setFilters] = useState({
    show: EDEBT_FILTER_SHOW.UNPAID,
    orderBy: EDEBT_ORDER_BY.DATE,
    orderDirection: EDEBT_ORDER_DIRECTION.DESC,
    invert: false,
  });

  useEffect(() => {
    (async function () {
      await getUsersData();
    })();
  }, []);

  useEffect(() => {
    if (currentDebtUserId !== -1) {
      (async function () {
        await getTableAndBalance();
        await getChartData();
      })();
    }
  }, [currentDebtUserId, filters]);

  // * DASHBOARD ACTIONS

  async function handleMarkAsPaid() {
    const temp = [...currentUserDebts];
    for (const markedAsPaid of markedRows) {
      const index = currentUserDebts.findIndex((d) => d.id === markedAsPaid);
      temp[index].paid = true;
    }

    await markDebtsAsPaid({
      ids: markedRows,
      token: accessToken,
    });

    await getTableAndBalance();
    setMarkedRows([]);
  }

  async function handleClearSelection() {
    setMarkedRows([]);
  }

  async function handleSelectAll() {
    const temp = [...markedRows];
    for (const debt of currentUserDebts) {
      if (temp.indexOf(debt.id) < 0) {
        temp.push(debt.id);
      }
    }
    setMarkedRows(temp);
  }

  const bulkActions = [
    {
      label: "Mark as paid",
      func: handleMarkAsPaid,
      color: "green",
    },
    {
      label: "Select all",
      func: handleSelectAll,
    },
    {
      label: "Clear selection",
      func: handleClearSelection,
    },
  ];

  // * API CALLS WRAPPERS

  async function getUsersData() {
    const { data } = await getAllExceptPrincipalUser({
      email: authUser.email,
    });
    setNonPrincipalUsers(data);
    setCurrentDebtUserId(data[0].id);
  }

  async function getChartData() {
    const data = await getDebtsSumByMonth({
      debtUserId: currentDebtUserId,
      token: accessToken,
    });
    setChartData({
      ...chartData,
      datasets: getBarChartDatasets(
        "Owed by you",
        "Owed to you",
        rawToChartDataset(data.owedByPrincipal),
        rawToChartDataset(data.owedToPrincipal),
        chakraTheme.colors.error,
        chakraTheme.colors.ok
      ),
    });
  }

  async function getBalance() {
    const amount = await getDebtsBalance({
      debtUserId: currentDebtUserId,
      token: accessToken,
      navigate,
    });

    return amount;
  }

  async function getTableAndBalance() {
    setTableLoading(true);
    const { data: tableData } = await getDebtsWithPagination({
      debtUserId: currentDebtUserId,
      filters,
      token: accessToken,
    });

    const balanceAmount = await getBalance();

    setCurrentUserDebts(tableData);
    setCurrentUserBalance(balanceAmount);
    setTableLoading(false);
  }

  // * MISC

  function getBalanceAlertMessage(balance) {
    if (balance < 0) {
      return "Pay what you owe and mark the debts as paid";
    } else if (balance > 0) {
      return "This user owes you money";
    } else {
      return "Everything is fine";
    }
  }

  function getBalanceAlertType(balance) {
    if (balance < 0) {
      return "error";
    } else if (balance > 0) {
      return "warning";
    } else {
      return "success";
    }
  }

  return (
    <Fragment>
      <Sidebar onClose={() => setSideBarOpen(false)} isOpen={sideBarOpen} />
      <Tabs
        variant="enclosed-colored"
        colorScheme="primary"
        height="100%"
        p={3}
      >
        <TabList overflowY="hidden" overflowX={["scroll", "scroll", "auto"]}>
          <IconButton
            mr={2}
            mb={1}
            variant="outline"
            colorScheme="primary"
            onClick={() => setSideBarOpen(true)}
            icon={<ChevronRightIcon />}
          />
          {nonPrincipalUsers.map((u, i) => {
            return (
              <Tab key={i} onClick={() => setCurrentDebtUserId(u.id)}>
                <Avatar mr={2} size="sm" src={u.avatarUrl} />
                {u.fullName.split(" ")[0]}
              </Tab>
            );
          })}
        </TabList>
        <TabPanels height="60%">
          {nonPrincipalUsers.map((_, i) => {
            return (
              <TabPanel key={i} height="100%">
                <SimpleGrid columns={[1, 1, 1]} gap={6}>
                  <Box>
                    {/* ACTIONS */}
                    <DashboardActions
                      onReload={async () =>
                        await Promise.all(getTableAndBalance(), getChartData())
                      }
                      bulkActions={bulkActions}
                      onFilterChange={setFilters}
                      filters={filters}
                      nonPrincipalUsers={nonPrincipalUsers}
                      currentDebtUserId={currentDebtUserId}
                    />
                    {/* TABLE */}
                    <DashboardTable
                      debts={currentUserDebts}
                      loading={tableLoading}
                      onMarkedRow={setMarkedRows}
                      markedRows={markedRows}
                    />
                    {/* BALANCE */}
                    {!tableLoading && (
                      <Alert
                        mt={5}
                        status={getBalanceAlertType(currentUserBalance)}
                      >
                        <AlertIcon />
                        <AlertTitle>
                          Your balance is{" "}
                          {currentUserBalance > 0
                            ? `+${currentUserBalance}`
                            : currentUserBalance}{" "}
                          {CURRENCY}
                        </AlertTitle>
                        <AlertDescription>
                          {getBalanceAlertMessage(currentUserBalance)}
                        </AlertDescription>
                      </Alert>
                    )}
                  </Box>
                  <SimpleGrid columns={[1, 1, 2]} gap={6}>
                    {/* BAR CHART */}
                    <DebtsSumByMonthBarChart chartData={chartData} />
                    {/* CREATE DEBT FORM */}
                    <Box mb={[5, 0, 0]} p={2}>
                      <CreateDebtForm
                        debtUserId={currentDebtUserId}
                        onSuccess={async () => {
                          await Promise.all(
                            getTableAndBalance(),
                            getChartData()
                          );
                          toast({
                            title: "Debt successfully created!",
                            duration: 2000,
                            status: "success",
                          });
                        }}
                      />
                    </Box>
                  </SimpleGrid>
                </SimpleGrid>
              </TabPanel>
            );
          })}
        </TabPanels>
      </Tabs>
    </Fragment>
  );
}
