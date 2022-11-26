import { Spinner, Button } from "@chakra-ui/react";

export default function LoadingButton({ isLoading, buttonTxt, ...rest }) {
  return (
    <Button colorScheme="primary" {...rest}>
      {isLoading ? <Spinner /> : buttonTxt}
    </Button>
  );
}
