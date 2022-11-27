// 1. Import `extendTheme`
import { extendTheme } from "@chakra-ui/react";

// 2. Call `extendTheme` and pass your custom values
const theme = extendTheme({
  colors: {
    primary: {
      100: "#B867F1",
      200: "#B562EF",
      300: "#B25EEE",
      400: "#AF59EC",
      500: "#AC55EB",
      600: "#A950E9",
      700: "#A64BE7",
      800: "#A347E6",
      900: "#A042E4",
    },
  },
});

export const chakraTheme = {
  colors: {
    primary: "#A042E4",
    ok: "#38A169",
    error: "#E53E3E",
  },
};

export default theme;
