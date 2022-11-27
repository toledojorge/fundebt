import {
  TableContainer,
  Tr,
  Td,
  Th,
  Tbody,
  Thead,
  Badge,
  Checkbox,
  Center,
  Text,
  Table,
  Tooltip,
} from "@chakra-ui/react";
import { InfoIcon } from "@chakra-ui/icons";
import { CURRENCY } from "../../config";
import { Fragment, useState } from "react";
import { truncate2decimals } from "../../utils/format.js";
import SyncLoader from "react-spinners/SyncLoader.js";
import { chakraTheme } from "../../theme.js";

export default function DashboardTable({
  loading,
  debts,
  markedRows,
  onMarkedRow,
}) {
  const [toolTipDebtId, setToolTipDebtId] = useState(-1);

  return (
    <TableContainer>
      {!loading && (
        <Table size="sm">
          <Thead>
            <Tr>
              <Th>Description</Th>
              <Th>Amount</Th>
              <Th>Initial amount</Th>
              <Th>Date</Th>
              <Th>Status</Th>
            </Tr>
          </Thead>
          <Tbody>
            {debts.map((d, i) => {
              const amount = truncate2decimals(d.amount);
              const reductionAmount = truncate2decimals(d.reductionAmount);
              const initialAmount = truncate2decimals(
                d.amount + d.reductionAmount
              );
              return (
                <Tr key={i}>
                  <Td>
                    <Text color="gray">{d.description}</Text>
                  </Td>
                  <Td>
                    {d.reductionAmount !== 0 && (
                      <Fragment>
                        <Badge
                          onClick={() => {
                            setToolTipDebtId(
                              toolTipDebtId === d.id ? -1 : d.id
                            );
                          }}
                          colorScheme="yellow"
                        >
                          <Fragment>
                            <Tooltip
                              isOpen={toolTipDebtId === d.id}
                              label={`Reduced by ${reductionAmount} ${CURRENCY}`}
                            >
                              <InfoIcon mr={2} />
                            </Tooltip>
                            {amount} {CURRENCY}
                          </Fragment>
                        </Badge>
                      </Fragment>
                    )}
                    {d.reductionAmount === 0 && (
                      <Fragment>
                        {amount} {CURRENCY}
                      </Fragment>
                    )}
                  </Td>
                  <Td>
                    {d.reductionAmount !== 0 && (
                      <Fragment>
                        {initialAmount} {CURRENCY}
                      </Fragment>
                    )}
                    {d.reductionAmount === 0 && (
                      <Fragment>
                        {amount} {CURRENCY}
                      </Fragment>
                    )}
                  </Td>
                  <Td>
                    <Badge colorScheme="purple">
                      {new Date(d.createdAt).toLocaleDateString()}
                    </Badge>
                  </Td>
                  <Td>
                    <Badge colorScheme={d.paid ? "green" : "red"}>
                      {d.paid ? "PAID" : "UNPAID"}
                    </Badge>
                  </Td>
                  <Td>
                    <Checkbox
                      isChecked={Boolean(markedRows.find((m) => m === d.id))}
                      onChange={(e) => {
                        onMarkedRow(
                          e.target.checked
                            ? markedRows.concat(d.id)
                            : markedRows.filter((m) => m !== d.id)
                        );
                      }}
                    />
                  </Td>
                </Tr>
              );
            })}
          </Tbody>
        </Table>
      )}
      {loading && (
        <Center minHeight="200px">
          <SyncLoader color={chakraTheme.colors.primary} />
        </Center>
      )}
    </TableContainer>
  );
}
