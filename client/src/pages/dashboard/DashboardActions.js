import {
  Grid,
  GridItem,
  Menu,
  MenuButton,
  MenuList,
  Button,
  FormControl,
  MenuItem,
  Flex,
  Select,
  Heading,
  IconButton,
  Badge,
  Text,
} from "@chakra-ui/react";
import {
  ArrowForwardIcon,
  ChevronDownIcon,
  RepeatIcon,
} from "@chakra-ui/icons";
import {
  EDEBT_FILTER_SHOW,
  EDEBT_ORDER_BY,
  EDEBT_ORDER_DIRECTION,
} from "../../enums.js";
import { chakraTheme } from "../../theme.js";

const selects = [
  {
    enum: EDEBT_FILTER_SHOW,
    filterProperty: "show",
    label: "Status",
  },
  {
    enum: EDEBT_ORDER_BY,
    filterProperty: "orderBy",
    label: "Order by",
  },
  {
    enum: EDEBT_ORDER_DIRECTION,
    filterProperty: "orderDirection",
    label: "Order direction",
  },
];

export default function DashboardActions({
  onReload,
  onFilterChange,
  bulkActions,
  filters,
  nonPrincipalUsers,
  currentDebtUserId,
}) {
  return (
    <Grid
      mt={2}
      maxW="100%"
      alignItems="center"
      templateColumns="repeat(12, 1fr)"
    >
      <GridItem mb={5} colSpan={12}>
        <Flex justify={["center", "initial"]}>
          <Menu>
            <MenuButton
              colorScheme="primary"
              minW={[100, 150]}
              as={Button}
              rightIcon={<ChevronDownIcon />}
            >
              Actions
            </MenuButton>
            <MenuList>
              {bulkActions.map((a, i) => {
                return (
                  <MenuItem
                    color={a.color}
                    onClick={a.func}
                    key={i}
                    minH="48px"
                  >
                    <span>{a.label}</span>
                  </MenuItem>
                );
              })}
            </MenuList>
          </Menu>
          <Button
            onClick={onReload}
            variant="outline"
            ml={3}
            minW={[100, 150]}
            colorScheme="primary"
            leftIcon={<RepeatIcon />}
          >
            Reload
          </Button>
        </Flex>
      </GridItem>
      <GridItem ml={2} mr={1} mt={2} mb={5} colSpan={12}>
        <Heading mb={2} fontSize="md">
          View
        </Heading>
        <Flex>
          <Text fontSize="md">
            <Badge
              minW="80px"
              colorScheme="red"
              variant={filters.invert ? "outline" : "solid"}
              p={1}
              display="flex"
              justifyContent="center"
              size="xs"
            >
              {filters.invert
                ? nonPrincipalUsers
                    .find((u) => u.id === currentDebtUserId)
                    .fullName.split(" ")[0]
                : "You"}
            </Badge>
          </Text>
          <IconButton
            variant="ghost"
            colorScheme="blue"
            borderColor="blue.400"
            border="1px solid"
            size="xs"
            ml={3}
            mr={3}
            rounded="full"
            bg="transparent"
            onClick={() => {
              onFilterChange({
                ...filters,
                invert: !filters.invert,
              });
            }}
          >
            <ArrowForwardIcon />
          </IconButton>
          <Text fontSize="md">
            <Badge
              minW="80px"
              variant={filters.invert ? "solid" : "outline"}
              display="flex"
              justifyContent="center"
              p={1}
              colorScheme="green"
              size="xs"
            >
              {filters.invert
                ? "You"
                : nonPrincipalUsers
                    .find((u) => u.id === currentDebtUserId)
                    .fullName.split(" ")[0]}
            </Badge>
          </Text>
        </Flex>
      </GridItem>
      {selects.map((s, i) => {
        return (
          <GridItem key={i} ml={1} mr={1} mb={5} colSpan={[6, 3, 3]}>
            <Heading mb={2} fontSize="md">
              {s.label}
            </Heading>
            <FormControl display="flex" alignItems="center">
              <Select
                focusBorderColor={chakraTheme.colors.primary}
                onChange={(e) =>
                  onFilterChange({
                    ...filters,
                    [s.filterProperty]: e.target.value,
                  })
                }
              >
                {Object.keys(s.enum).map((k, i) => {
                  return (
                    <option
                      key={i}
                      value={s.enum[k]}
                      selected={filters[s.filterProperty] === s.enum[k]}
                    >
                      {k}
                    </option>
                  );
                })}
              </Select>
            </FormControl>
          </GridItem>
        );
      })}
    </Grid>
  );
}
